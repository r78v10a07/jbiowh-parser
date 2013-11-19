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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.gene.gene.entities.GeneInfo;
import org.jbiowhpersistence.datasets.gene.genebank.GeneBankTables;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBank;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankAccession;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankCDS;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankCDSDBXref;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankFeatures;

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

    /**
     * This method explore the GeneBank directory and insert into the JBioWH
     * relational schema all files *.seq.gz or *.seq
     *
     * @throws SQLException
     */
    public void load() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        whdbmsFactory.disableKeys(GeneBankTables.GENEBANKCDS_HAS_GENEINFO);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANK);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANKACCESSION);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANKCDS);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANKCDSDBXREF);
        whdbmsFactory.disableKeys(GeneBankTables.getInstance().GENEBANKFEATURES);

        long startTime = System.currentTimeMillis();
        if (dir.isDirectory()) {
            List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{".gbk", ".gbk.gz", ".seq", ".seq.gz"});
            int i = 0;
            for (File file : files) {
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneBankTables.getInstance().GENEBANKCDSTEMP);
                ParseFiles.getInstance().start(GeneBankTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
                try {
                    InputStream in;
                    VerbLogger.getInstance().log(this.getClass(), "File: " + (i++) + " of: " + files.size());
                    if (file.isFile() && file.getCanonicalPath().endsWith(".gz")) {
                        in = new GZIPInputStream(new FileInputStream(file));
                    } else {
                        in = new FileInputStream(file);
                    }
                    VerbLogger.getInstance().log(this.getClass(), "Parsing file " + i + " " + file.getCanonicalPath());
                    parser(in, file.getName());
                } catch (IOException ex) {
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
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
                String strTime = String.format("%.2f", ((float) ((((long) (System.currentTimeMillis() - startTime) / 1000)) / i) * (files.size() - i) / 3600.0));
                VerbLogger.getInstance().log(this.getClass(), "Estimated left time: " + strTime + " h");
            }

            whdbmsFactory.enableKeys(GeneBankTables.GENEBANKCDS_HAS_GENEINFO);
            whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANK);
            whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANKACCESSION);
            whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANKCDS);
            whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANKCDSDBXREF);
            whdbmsFactory.enableKeys(GeneBankTables.getInstance().GENEBANKFEATURES);

        }
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

                if (geneBank.getGeneBankAccessions() != null) {
                    for (GeneBankAccession a : geneBank.getGeneBankAccessions()) {
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKACCESSION, geneBank.getWid(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKACCESSION, a.getAccession(), "\n");
                    }
                }

                if (geneBank.getGeneBankFeatureses() != null) {
                    for (GeneBankFeatures f : geneBank.getGeneBankFeatureses()) {
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

                if (geneBank.getGeneBankCDSs() != null) {
                    for (GeneBankCDS c : geneBank.getGeneBankCDSs()) {
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

                        if (c.getGeneBankCDSDBXrefs() != null) {
                            for (GeneBankCDSDBXref r : c.getGeneBankCDSDBXrefs()) {
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSDBXREF, c.getWid(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSDBXREF, r.getdBXref(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(GeneBankTables.getInstance().GENEBANKCDSDBXREF, r.getdBIdent(), "\n");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            VerbLogger.getInstance().log(this.getClass(), "Error: " + ex.toString());
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
            System.exit(1);
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
                geneBank.setGeneBankFeatureses(new ArrayList<GeneBankFeatures>());
                geneBank.setGeneBankAccessions(new ArrayList<GeneBankAccession>());
                geneBank.setGeneBankCDSs(new HashSet<GeneBankCDS>());

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
                        geneBank.getGeneBankAccessions().add(a);
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

        reader.mark(1000000);
        while ((line = reader.readLine()) != null) {
            if (line.matches("^\\w.+")) {
                reader.reset();
                break;
            }
            if (line.matches("^ {5}\\w+ .+")) {
                if (line.trim().startsWith(SOURCE)) {
                    if (geneBank.getLocation() == null || geneBank.getLocation().isEmpty()) {
                        geneBank.setLocation(line.replace(SOURCE, "").trim());
                        map = getQualifier(reader);
                        if (map.containsKey(TAXON)) {
                            geneBank.setTaxId(Integer.parseInt((String) map.get(TAXON)));
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
                        cds.setGeneBankCDSDBXrefs(new ArrayList<GeneBankCDSDBXref>());
                        cds.setGenePTT(null);
                        cds.setProteinGi(-1);
                        cds.setGene(null);
                        cds.setLocusTag(null);
                        cds.setLocation(location);
                        if (map.containsKey(DB_XREF)) {
                            for (String db : (Set<String>) map.get(DB_XREF)) {
                                if (db.contains(GI + ":")) {
                                    cds.setProteinGi(Integer.parseInt(db.substring(db.indexOf(":") + 1)));
                                } else {
                                    GeneBankCDSDBXref ref = new GeneBankCDSDBXref();
                                    ref.setdBXref(db.substring(0, db.indexOf(":")));
                                    ref.setdBIdent(db.substring(db.indexOf(":") + 1));
                                    cds.getGeneBankCDSDBXrefs().add(ref);
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
                        }else{
                            cds.setTranslation(false);
                        }
                        geneBank.getGeneBankCDSs().add(cds);
                    } else {
                        GeneBankFeatures feature = new GeneBankFeatures();
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
                        geneBank.getGeneBankFeatureses().add(feature);
                    }
                }
            }
            reader.mark(1000000);
        }
    }

    private HashMap<String, Object> getQualifier(BufferedReader reader) throws Exception {
        HashMap<String, Object> map = new HashMap();
        String line;
        Pattern p;
        Matcher m;

        reader.mark(1000000);
        while ((line = reader.readLine()) != null) {
            if (line.matches("^ {5}\\w+ .+") || line.matches("^\\w.+")) {
                reader.reset();
                break;
            }
            if (line.trim().startsWith("/")) {
                p = Pattern.compile("taxon:(\\d+)");
                m = p.matcher(line);
                if (m.find()) {
                    map.put(TAXON, m.group(1));
                } else {
                    p = Pattern.compile("(?:" + DB_XREF + "=\")(\\w.+)(?:\")");
                    m = p.matcher(line.trim());
                    if (m.find()) {
                        if (m.groupCount() == 1) {
                            if (!map.containsKey(DB_XREF)) {
                                map.put(DB_XREF, new HashSet<String>());
                            }
                            ((Set) map.get(DB_XREF)).add(m.group(1));
                        } else {
                            throw new IOException("Bad " + DB_XREF + " line: " + line);
                        }
                    } else {
                        p = Pattern.compile("(?:" + PRODUCT + "=\")(\\w.+)(?:\")");
                        m = p.matcher(line.trim());
                        if (m.find()) {
                            if (m.groupCount() == 1) {
                                map.put(PRODUCT, m.group(1));
                            } else {
                                throw new IOException("Bad " + PRODUCT + " line: " + line);
                            }
                        } else {
                            p = Pattern.compile("(?:" + PROTEIN_ID + "=\")(\\w.+)(?:\")");
                            m = p.matcher(line.trim());
                            if (m.find()) {
                                if (m.groupCount() == 1) {
                                    map.put(PROTEIN_ID, m.group(1));
                                } else {
                                    throw new IOException("Bad " + PROTEIN_ID + " line: " + line);
                                }
                            } else {
                                p = Pattern.compile("(?:" + GENE + "=\")(\\w.+)(?:\")");
                                m = p.matcher(line.trim());
                                if (m.find()) {
                                    if (m.groupCount() == 1) {
                                        map.put(GENE, m.group(1));
                                    } else {
                                        throw new IOException("Bad " + GENE + " line: " + line);
                                    }
                                } else {
                                    p = Pattern.compile("(?:" + LOCUS_TAG + "=\")(\\w.+)(?:\")");
                                    m = p.matcher(line.trim());
                                    if (m.find()) {
                                        if (m.groupCount() == 1) {
                                            map.put(LOCUS_TAG, m.group(1));
                                        } else {
                                            throw new IOException("Bad " + LOCUS_TAG + " line: " + line);
                                        }
                                    } else {
                                        p = Pattern.compile("(?:" + TRANSLATION + "=\")(\\w.+)");
                                        m = p.matcher(line.trim());
                                        if (m.find()) {
                                            if (m.groupCount() == 1) {
                                                map.put(TRANSLATION, 1);
                                            } else {
                                                throw new IOException("Bad " + TRANSLATION + " line: " + line);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            reader.mark(1000000);
        }
        return map;
    }
}
