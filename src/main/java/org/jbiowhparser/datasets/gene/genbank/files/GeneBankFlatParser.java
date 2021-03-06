package org.jbiowhparser.datasets.gene.genbank.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.apache.commons.net.ftp.FTPFile;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.utils.FTPFacade;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.gene.gene.entities.GeneInfo;
import org.jbiowhpersistence.datasets.gene.genebank.GeneBankTables;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBank;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankAccession;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankCDS;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankCDSDBXref;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankCDSLocation;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankCOG;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankFeature;

/**
 * This class is the Genbank parser for the SEQ flat files
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 656 $
 *
 * @since Apr 30, 2013
 */
public class GeneBankFlatParser {

    private final String LOCUS = "LOCUS";
    private final String DEFINITION = "DEFINITION";
    private final String ACCESSION = "ACCESSION";
    private final String VERSION = "VERSION";
    private final String FEATURES = "FEATURES";
    private final String GI = "GI";
    private final String CDS = "CDS";
    private final String SOURCE = "source";
    private final String DB_XREF = "db_xref";
    private final String PRODUCT = "product";
    private final String PROTEIN_ID = "protein_id";
    private final String ENDRECORD = "//";
    private final String CONTIG = "CONTIG";
    private final String ORIGIN = "ORIGIN";
    private final String GENE = "gene";
    private final String TAXON = "taxon";
    private final String ORGANISM = "ORGANISM";
    private final String LOCUS_TAG = "locus_tag";
    private final String TRANSLATION = "translation";
    public final String NOTE = "note";

    /**
     * This method explore the GeneBank directory and insert into the JBioWH
     * relational schema all files *.seq.gz or *.seq
     *
     * @throws SQLException
     */
    public void load() throws SQLException {
        FTPFacade ftp = null;
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        whdbmsFactory.disableKeys(GeneBankTables.GENEBANKCDS_HAS_GENEINFO);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANK);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANKACCESSION);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANKCDS);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANKCDSDBXREF);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANKFEATURES);

        Float esTime;
        long startTime = System.nanoTime();
        List files;
        try {
            if (DataSetPersistence.getInstance().isonlineFTP()) {
                ftp = new FTPFacade(DataSetPersistence.getInstance().getOnlineSite(),
                        DataSetPersistence.getInstance().getOnlineUser(),
                        DataSetPersistence.getInstance().getOnlinePasswd());
                ftp.getFtpClient().changeWorkingDirectory(DataSetPersistence.getInstance().getOnlinePath());
                files = ftp.listFiles("./", new String[]{".gbk", ".gbk.gz", ".seq", ".seq.gz", ".gbff", ".gbff.gz"});
                ftp.close();
            } else {
                files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{".gbk", ".gbk.gz", ".seq", ".seq.gz", ".gbff", ".gbff.gz"});
            }

            int i = 0;

            for (Object file : files) {
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneBankTables.getInstance().GENEBANKCDSTEMP);
                ParseFiles.getInstance().start(GeneBankTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
                String name = null;
                InputStream in = null;

                if (file instanceof File) {
                    name = ((File) file).getName();
                    if (((File) file).isFile() && name.endsWith(".gz")) {
                        in = new GZIPInputStream(new FileInputStream(((File) file)));
                    } else {
                        in = new FileInputStream(((File) file));
                    }
                    VerbLogger.getInstance().log(this.getClass(), "Parsing file " + i + " " + ((File) file).getCanonicalPath());
                } else if (file instanceof FTPFile && ftp != null) {
                    name = ((FTPFile) file).getName();
                    ftp = new FTPFacade(DataSetPersistence.getInstance().getOnlineSite(),
                            DataSetPersistence.getInstance().getOnlineUser(),
                            DataSetPersistence.getInstance().getOnlinePasswd());
                    ftp.getFtpClient().changeWorkingDirectory(DataSetPersistence.getInstance().getOnlinePath());
                    if (name.endsWith(".gz")) {
                        in = new GZIPInputStream(ftp.getFtpClient().retrieveFileStream(name));
                    } else {
                        in = ftp.getFtpClient().retrieveFileStream(name);
                    }
                }
                if (in != null && name != null) {
                    VerbLogger.getInstance().log(this.getClass(), "File: " + (i++) + " of: " + files.size() + " with name: " + name);
                    parser(in, name);
                    in.close();
                }

                if (ftp != null) {
                    VerbLogger.getInstance().log(this.getClass(), "Closing connection to " + DataSetPersistence.getInstance().getOnlineSite());
                    ftp.close();
                }

                ParseFiles.getInstance().closeAllPrintWriter();
                whdbmsFactory.loadTSVTables(GeneBankTables.getInstance().getTables());
                ParseFiles.getInstance().end();

                VerbLogger.getInstance().log(this.getClass(), "Inserting into table: " + GeneBankTables.GENEBANKCDS_HAS_GENEINFO);

                whdbmsFactory.executeUpdate("insert ignore into "
                        + GeneBankTables.GENEBANKCDS_HAS_GENEINFO
                        + " (GeneBankCDS_WID,GeneInfo_WID) "
                        + " (select c.WID,a.GeneInfo_WID from "
                        + GeneBankTables.getInstance().GENEBANKCDSTEMP
                        + " c inner join "
                        + GeneTables.getInstance().GENE2PROTEINACCESSION
                        + " a on c.ProteinGi = a.ProteinGi)");

                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneBankTables.getInstance().GENEBANKCDSTEMP);
                esTime = ((float) ((float) TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) * (files.size() - i) / 3600.0));
                String strTime = String.format("%.2f", esTime);
                VerbLogger.getInstance().log(this.getClass(), "Estimated left time: " + strTime + " h");
            }

        } catch (IOException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
            DataSetPersistence.getInstance().getDataset().setStatus("Error");
            DataSetPersistence.getInstance().updateDataSet();
            WIDFactory.getInstance().updateWIDTable();
            System.exit(-1);
        }

        whdbmsFactory.enableKeys(GeneBankTables.GENEBANKCDS_HAS_GENEINFO);
        whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANK);
        whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANKACCESSION);
        whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANKCDS);
        whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANKCDSDBXREF);
        whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANKFEATURES);

    }

    private void parser(InputStream in, String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            GeneBank geneBank;

            while ((geneBank = readGeneBankEntry(reader, fileName)) != null) {
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getWid(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getLocusName(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getSeqLengh(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getMolType(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getDivision(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, (new SimpleDateFormat("yyyy-MM-dd")).format(geneBank.getModDate()), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getDefinition(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getVersion(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getGi(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getTaxId(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getLocation(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getSource(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getOrganism(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getSeq(), "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, fileName, "\t");
                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANK, geneBank.getDataSetWID(), "\n");

                if (geneBank.getGeneBankAccession() != null) {
                    for (GeneBankAccession a : geneBank.getGeneBankAccession()) {
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKACCESSION, geneBank.getWid(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKACCESSION, a.getAccession(), "\n");
                    }
                }

                if (geneBank.getGeneBankFeature() != null) {
                    for (GeneBankFeature f : geneBank.getGeneBankFeature()) {
                        if (f.getKeyName().toUpperCase().contains("RNA") && !f.getKeyName().toUpperCase().contains("misc")) {
                            ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKFEATURES, geneBank.getWid(), "\t");
                            ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKFEATURES, f.getKeyName(), "\t");
                            ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKFEATURES, f.getLocation(), "\t");
                            ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKFEATURES, f.getGi(), "\t");
                            ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKFEATURES, f.getProduct(), "\t");
                            ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKFEATURES, f.getGene(), "\n");
                        }
                    }
                }

                if (geneBank.getGeneBankCDS() != null) {
                    for (GeneBankCDS c : geneBank.getGeneBankCDS()) {
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.getWid(), "\t");
                        if (c.getProteinGi() == -1) {
                            ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, (String) null, "\t");
                        } else {
                            ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.getProteinGi(), "\t");
                        }
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.getLocation(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.getProduct(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.getProteinId(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.getGene(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.getLocusTag(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.isTranslation(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDS, c.getGeneBankWID(), "\n");

                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSTEMP, c.getWid(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSTEMP, c.getProteinGi(), "\n");

                        if (c.getGeneBankCDSLocation() != null) {
                            for (GeneBankCDSLocation r : c.getGeneBankCDSLocation()) {
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSLOCATION, c.getWid(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSLOCATION, r.getpFrom(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSLOCATION, r.getpTo(), "\n");
                            }
                        }

                        if (c.getGeneBankCDSDBXref() != null) {
                            for (GeneBankCDSDBXref r : c.getGeneBankCDSDBXref()) {
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSDBXREF, c.getWid(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSDBXREF, r.getdBXref(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSDBXREF, r.getdBIdent(), "\n");
                            }
                        }

                        if (c.getGeneBankCOG() != null) {
                            for (GeneBankCOG cog : c.getGeneBankCOG()) {
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.GENEBANKCOG, c.getWid(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.GENEBANKCOG, cog.getCogId(), "\n");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
            DataSetPersistence.getInstance().getDataset().setStatus("Error");
            DataSetPersistence.getInstance().updateDataSet();
            WIDFactory.getInstance().updateWIDTable();
            System.exit(-1);
        }

    }

    /**
     * Reads a GeneBank entry from the BufferedReader opened for the Genebank
     * file
     *
     * @param reader the BufferedReader opened for the Genebank file
     * @param fileName the geneBank file Name
     * @return a GeneBank entity
     * @throws IOException
     * @throws ParseException
     * @throws Exception
     */
    public GeneBank readGeneBankEntry(BufferedReader reader, String fileName) throws IOException, ParseException, Exception {
        GeneBank geneBank = null;
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith(LOCUS)) {
                geneBank = new GeneBank(WIDFactory.getInstance().getWid());
                WIDFactory.getInstance().increaseWid();
                geneBank.setGeneBankFeature(new ArrayList<GeneBankFeature>());
                geneBank.setGeneBankAccession(new ArrayList<GeneBankAccession>());
                geneBank.setGeneBankCDS(new HashSet<GeneBankCDS>());

                geneBank.setLocusName(line.substring(12, 27).trim());
                geneBank.setSeqLengh(Integer.parseInt(line.substring(29, 40).trim()));
                geneBank.setMolType(line.substring(47, 53).trim());
                geneBank.setDivision(line.substring(64, 67).trim());
                geneBank.setModDate(new SimpleDateFormat("dd-MMM-yyyy").parse(line.substring(68, 79)));
                geneBank.setFileName(fileName);

                if (DataSetPersistence.getInstance().getDataset() != null) {
                    geneBank.setDataSetWID(DataSetPersistence.getInstance().getDataset().getWid());
                } else {
                    geneBank.setDataSetWID(-1);
                }
            }
            if (geneBank != null) {
                if (line.startsWith(DEFINITION)) {
                    geneBank.setDefinition(getPatterList(reader, DEFINITION, line));
                }
                if (line.startsWith(ACCESSION)) {
                    for (String s : line.replace(ACCESSION, "").trim().split(" +")) {
                        GeneBankAccession a = new GeneBankAccession();
                        a.setAccession(s);
                        geneBank.getGeneBankAccession().add(a);
                    }
                }
                if (line.startsWith(VERSION)) {
                    String[] fields = getPatterList(reader, VERSION, line).split(" +");
                    if (fields.length == 2) {
                        if (fields[0].lastIndexOf(".") != -1) {
                            geneBank.setVersion(fields[0].substring(fields[0].lastIndexOf(".") + 1));
                        }
                        if (fields[1].lastIndexOf(GI + ":") != -1) {
                            geneBank.setGi(Integer.parseInt(fields[1].substring(fields[1].lastIndexOf(GI + ":") + 3)));
                        }
                    } else if (fields.length == 1) {
                        if (fields[0].lastIndexOf(GI + ":") != -1) {
                            geneBank.setGi(Integer.parseInt(fields[0].substring(fields[0].lastIndexOf(GI + ":") + 3)));
                        } else if (fields[0].lastIndexOf(".") != -1) {
                            geneBank.setVersion(fields[0].substring(fields[0].lastIndexOf(".") + 1));
                        }
                    } else {
                        throw new IOException("Bad VERSION line: " + line);
                    }
                }
                if (line.startsWith(SOURCE.toUpperCase())) {
                    StringBuilder lineage = new StringBuilder(line.replace(SOURCE.toUpperCase(), "").trim());
                    reader.mark(1000000);
                    while ((line = reader.readLine()) != null) {
                        if (!line.substring(0, 12).trim().isEmpty()) {
                            reader.reset();
                            break;
                        }
                        lineage.append(line.trim()).append(" ");
                        if (line.endsWith(".")) {
                            break;
                        }
                        reader.mark(1000000);
                    }
                    if (!lineage.toString().isEmpty()) {
                        geneBank.setSource(lineage.toString().trim());
                    }
                }
                if (line.trim().startsWith(ORGANISM)) {
                    StringBuilder lineage = new StringBuilder();
                    reader.mark(1000000);
                    while ((line = reader.readLine()) != null) {
                        if (!line.substring(0, 12).trim().isEmpty()) {
                            reader.reset();
                            break;
                        }
                        if (line.contains(";") || line.trim().endsWith(".")) {
                            lineage.append(line.trim()).append(" ");
                        }
                        reader.mark(1000000);
                    }
                    if (!lineage.toString().isEmpty()) {
                        geneBank.setOrganism(lineage.toString().trim());
                    }
                }
                if (line.startsWith(FEATURES)) {
                    getFeatures(reader, geneBank);
                }
                if (line.startsWith(ORIGIN)) {
                    StringBuilder seq = new StringBuilder();
                    reader.mark(1000000);
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith("  ")) {
                            reader.reset();
                            break;
                        }
                        seq.append(line.substring(10).replace(" ", "").trim());
                        reader.mark(1000000);
                    }
                    geneBank.setSeq(seq.toString().replace(" ", ""));
                }
            }
            if (geneBank != null && line.startsWith(ENDRECORD)) {
                return geneBank;
            }
        }
        return geneBank;
    }

    private String getPatterList(BufferedReader reader, String pattern, String outline) throws IOException {
        String line;
        StringBuilder builder = new StringBuilder(outline.replace(pattern, "").trim());

        reader.mark(1000000);
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith(" ") && !line.startsWith(pattern)) {
                reader.reset();
                break;
            }
            builder.append(line);
            reader.mark(1000000);
        }
        return builder.toString().trim().replaceAll(" +", " ").replaceAll("\t+", " ");
    }

    private void getFeatures(BufferedReader reader, GeneBank geneBank) throws Exception {
        String line;
        HashMap<String, Object> map;
        Pattern p1 = Pattern.compile("(COG\\d{4})");
        Matcher m1 = p1.matcher("");

        reader.mark(1000000);
        while ((line = reader.readLine()) != null) {
            if (line.matches("^\\w+.+")) {
                reader.reset();
                break;
            }
            if (line.matches("^ {5}\\w+ .+")) {
                if (line.trim().startsWith(SOURCE)) {
                    if (geneBank.getLocation() == null || geneBank.getLocation().isEmpty()) {
                        geneBank.setLocation(line.replace(SOURCE, "").trim());
                        map = getQualifier(reader);
                        if (map.containsKey(DB_XREF)) {
                            for (String db : (Set<String>) map.get(DB_XREF)) {
                                if (db.contains(TAXON + ":")) {
                                    geneBank.setTaxId(Integer.parseInt(db.substring(db.indexOf(":") + 1)));
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    String key = null;
                    String location = null;
                    Pattern p = Pattern.compile("[a-zA-Z_0-9\\.\\(\\)<>\\^,:]+");
                    Matcher m = p.matcher(line);
                    int i = 0;
                    while (m.find()) {
                        if (i == 0) {
                            key = m.group(0);
                        }
                        if (i == 1) {
                            location = m.group(0);
                        }
                        i++;
                    }
                    if (key == null || location == null) {
                        throw new IOException("Bad FEATURE line: " + line);
                    }
                    map = getQualifier(reader);
                    if (key.equals(CDS)) {
                        GeneBankCDS cds = new GeneBankCDS(WIDFactory.getInstance().getWid());
                        WIDFactory.getInstance().increaseWid();
                        cds.setGeneBank(null);
                        cds.setGeneBankWID(geneBank.getWid());
                        cds.setGeneInfo(new HashSet<GeneInfo>());
                        cds.setGeneBankCDSDBXref(new HashSet<GeneBankCDSDBXref>());
                        cds.setGeneBankCOG(new HashSet<GeneBankCOG>());
                        cds.setGenePTT(null);
                        cds.setProteinGi(-1);
                        cds.setGene(null);
                        cds.setLocusTag(null);
                        cds.setLocation(location);
                        cds.setGeneBankCDSLocation(parseLocation(location));
                        if (map.containsKey(DB_XREF)) {
                            for (String db : (Set<String>) map.get(DB_XREF)) {
                                if (db.contains(GI + ":")) {
                                    cds.setProteinGi(Integer.parseInt(db.substring(db.indexOf(":") + 1)));
                                } else {
                                    GeneBankCDSDBXref ref = new GeneBankCDSDBXref();
                                    ref.setdBXref(db.substring(0, db.indexOf(":")));
                                    ref.setdBIdent(db.substring(db.indexOf(":") + 1));
                                    cds.getGeneBankCDSDBXref().add(ref);
                                }
                            }

                        }
                        if (map.containsKey(PRODUCT)) {
                            cds.setProduct((String) map.get(PRODUCT));
                        }
                        if (map.containsKey(PROTEIN_ID)) {
                            cds.setProteinId((String) map.get(PROTEIN_ID));
                        }
                        if (map.containsKey(GENE)) {
                            cds.setGene((String) map.get(GENE));
                        }
                        if (map.containsKey(LOCUS_TAG)) {
                            cds.setLocusTag((String) map.get(LOCUS_TAG));
                        }
                        if (map.containsKey(TRANSLATION)) {
                            cds.setTranslation(true);
                        } else {
                            cds.setTranslation(false);
                        }
                        if (map.containsKey(NOTE)) {
                            m1.reset((String) map.get(NOTE));
                            while (m1.find()) {
                                GeneBankCOG cog = new GeneBankCOG(m1.group(1));
                                cds.getGeneBankCOG().add(cog);
                            }
                        }
                        geneBank.getGeneBankCDS().add(cds);
                    } else {
                        GeneBankFeature feature = new GeneBankFeature();
                        feature.setKeyName(key);
                        feature.setLocation(location);
                        if (map.containsKey(DB_XREF)) {
                            for (String db : (Set<String>) map.get(DB_XREF)) {
                                if (db.contains(GI + ":")) {
                                    feature.setGi(Long.parseLong(db.substring(db.indexOf(":") + 1)));
                                }
                            }

                        }
                        if (map.containsKey(PRODUCT)) {
                            feature.setProduct((String) map.get(PRODUCT));
                        }
                        if (map.containsKey(GENE)) {
                            feature.setGene((String) map.get(GENE));
                        }
                        geneBank.getGeneBankFeature().add(feature);
                    }
                }
            }
            reader.mark(1000000);
        }
    }

    private HashMap<String, Object> getQualifier(BufferedReader reader) {

        HashMap<String, Object> map = new HashMap();
        String line = null;
        String value;
        Pattern p1 = Pattern.compile("/([a-zA-Z_0-9]+)=*\"*([a-zA-Z_0-9\\.\\(\\)<>\\^,;:\\s\\-\"/]+)*");
        Matcher m1 = p1.matcher("");

        try {
            reader.mark(1000000);
            try {
                while ((line = reader.readLine()) != null) {
                    if (!line.matches("\\s{21}.+")) {
                        reader.reset();
                        break;
                    }
                    if (line.trim().startsWith("/")) {
                        StringBuilder builder = new StringBuilder(line.trim());
                        reader.mark(1000000);
                        while ((line = reader.readLine()) != null) {
                            if (line.trim().matches("/[a-zA-Z_0-9]+.*")
                                    || !line.matches("\\s{21}.+")) {
                                reader.reset();
                                break;
                            }
                            builder.append(" ").append(line.trim());
                            reader.mark(1000000);
                        }
                        m1.reset(builder.toString());
                        m1.find();
                        value = "";
                        if (m1.group(2) != null) {
                            if (m1.group(2).endsWith("\"")) {
                                value = m1.group(2).substring(0, m1.group(2).length() - 1);
                            } else {
                                value = m1.group(2);
                            }
                        }
                        switch (m1.group(1)) {
                            case DB_XREF:
                                if (!map.containsKey(DB_XREF)) {
                                    map.put(DB_XREF, new HashSet<String>());
                                }
                                ((Set) map.get(DB_XREF)).add(value);
                                break;
                            case TRANSLATION:
                                map.put(m1.group(1), value.replace(" ", ""));
                                break;
                            default:
                                map.put(m1.group(1), value);
                                break;
                        }
                    }
                    reader.mark(1000000);
                }
            } catch (IllegalStateException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                DataSetPersistence.getInstance().getDataset().setStatus("Error");
                DataSetPersistence.getInstance().updateDataSet();
                WIDFactory.getInstance().updateWIDTable();
                System.exit(-1);
            }
        } catch (IOException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
            DataSetPersistence.getInstance().getDataset().setStatus("Error");
            DataSetPersistence.getInstance().updateDataSet();
            WIDFactory.getInstance().updateWIDTable();
            System.exit(-1);
        }
        return map;
    }

    private Set<GeneBankCDSLocation> parseLocation(String location) {
        Set<GeneBankCDSLocation> cdsLocation = new HashSet<>();

        if (location != null) {
            Pattern p = Pattern.compile("([a-zA-Z_0-9]*.[a-zA-Z_0-9]*:)?(\\d+)[.\\-\\^><]+(\\d+)?");
            Matcher m = p.matcher(location);

            while (m.find()) {
                GeneBankCDSLocation loc;
                Integer from = Integer.valueOf(m.group(2));
                String to = m.group(3);
                if (to != null) {
                    if (Integer.valueOf(to) < from) {
                        from = Integer.valueOf(to);
                        to = m.group(2);
                    }
                    loc = new GeneBankCDSLocation(from, Integer.valueOf(to));
                } else {
                    loc = new GeneBankCDSLocation(from, null);
                }
                cdsLocation.add(loc);
            }
        }

        return cdsLocation;
    }
}
