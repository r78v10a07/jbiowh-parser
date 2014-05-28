package org.jbiowhparser.datasets.gene.genome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.gene.genome.links.GenePTTLinks;
import org.jbiowhparser.utils.FTPFacade;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.gene.genome.GenePTTTables;

/**
 * This Class is the Gene PTT parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-02-07 09:07:45 +0100
 * (Thu, 07 Feb 2013) $ $LastChangedRevision: 656 $
 *
 * @since Aug 5, 2011
 */
public class GenePTTParser extends ParserFactory implements JBioWHParser {

    /**
     * This method load the data in gene_info file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        List files;
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GenePTTTables.GENEPTT);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GenePTTTables.GENEPTT);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GenePTTTables.getInstance().GENEPTTTAXONOMY);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GenePTTTables.getInstance().GENEPTTTAXONOMY);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.GENEINFO_HAS_GENEPTT);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENEINFO_HAS_GENEPTT);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GenePTTTables.GENERNT);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GenePTTTables.GENERNT);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GenePTTTables.GENERNT_HAS_TAXONOMY);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GenePTTTables.GENERNT_HAS_TAXONOMY);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.GENEINFO_HAS_GENERNT);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENEINFO_HAS_GENERNT);
        }

        if (DataSetPersistence.getInstance().isonlineFTP()) {
            FTPFacade ftp = new FTPFacade(DataSetPersistence.getInstance().getOnlineSite(),
                    DataSetPersistence.getInstance().getOnlineUser(),
                    DataSetPersistence.getInstance().getOnlinePasswd());
            files = ftp.listFilesString(DataSetPersistence.getInstance().getOnlinePath(), new String[]{".ptt", ".rnt"}, 2);
            ftp.close();
        } else {
            files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{".ptt", ".rnt"});
        }
        int i = 0;

        whdbmsFactory.executeUpdate("ALTER TABLE " + GenePTTTables.GENERNT + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        for (Object ofile : files) {
            try {
                File file;
                if (ofile instanceof File) {
                    file = ((File) ofile);
                } else {
                    FTPFacade ftp = new FTPFacade(DataSetPersistence.getInstance().getOnlineSite(),
                            DataSetPersistence.getInstance().getOnlineUser(),
                            DataSetPersistence.getInstance().getOnlinePasswd());
                    int index = ((String) ofile).lastIndexOf("/");
                    try (OutputStream tmpFile = new FileOutputStream(DataSetPersistence.getInstance().getTempdir() + ((String) ofile).substring(index + 1))) {
                        ftp.getFtpClient().retrieveFile((String) ofile, tmpFile);
                    }
                    ftp.close();
                    file = new File(DataSetPersistence.getInstance().getTempdir() + ((String) ofile).substring(index + 1));
                }

                if (file.isFile() && file.getCanonicalPath().endsWith(".ptt")) {
                    VerbLogger.getInstance().log(this.getClass(), "File: " + (i++) + " of: " + files.size());
                    VerbLogger.getInstance().log(this.getClass(), "Inserting file: " + file.getCanonicalPath());
                    whdbmsFactory.loadTSVFileIgnore(GenePTTTables.GENEPTT, file,
                            "  IGNORE 3 LINES "
                            + "(Location,Strand,PLength,ProteinGi,@GeneSymbol,@GeneLocusTag,@Code,@COG,Product) "
                            + "set PTTFile='" + file.getName().substring(0, file.getName().length() - 4) + "',"
                            + "pFrom=SUBSTRING(REPLACE(Location,'..',' '),1,LOCATE(' ',REPLACE(Location,'..',' ')) - 1),"
                            + "pTo=SUBSTRING(REPLACE(Location,'..',' '),LOCATE(' ',REPLACE(Location,'..',' ')) + 1),"
                            + "GeneSymbol=REPLACE(@GeneSymbol,'-',NULL),"
                            + "GeneLocusTag=REPLACE(@GeneLocusTag,'-',NULL),"
                            + "Code=REPLACE(@Code,'-',NULL),"
                            + "COG=REPLACE(@COG,'-',NULL),"
                            + "DataSetWID=" + DataSetPersistence.getInstance().getDataset().getWid());
                } else if (file.isFile() && file.getCanonicalPath().endsWith(".rnt")) {
                    VerbLogger.getInstance().log(this.getClass(), "File: " + (i++) + " of: " + files.size());
                    VerbLogger.getInstance().log(this.getClass(), "Inserting file: " + file.getCanonicalPath());
                    whdbmsFactory.loadTSVFileIgnore(GenePTTTables.GENERNT, file,
                            "  IGNORE 3 LINES "
                            + "(Location,Strand,PLength,GenomicNucleotideGi,@GeneSymbol,@GeneLocusTag,@Code,@COG,Product) "
                            + "set PTTFile='" + file.getName().substring(0, file.getName().length() - 4) + "',"
                            + "pFrom=SUBSTRING(REPLACE(Location,'..',' '),1,LOCATE(' ',REPLACE(Location,'..',' ')) - 1),"
                            + "pTo=SUBSTRING(REPLACE(Location,'..',' '),LOCATE(' ',REPLACE(Location,'..',' ')) + 1),"
                            + "GeneSymbol=REPLACE(@GeneSymbol,'-',NULL),"
                            + "GeneLocusTag=REPLACE(@GeneLocusTag,'-',NULL),"
                            + "Code=REPLACE(@Code,'-',NULL),"
                            + "COG=REPLACE(@COG,'-',NULL),"
                            + "DataSetWID=" + DataSetPersistence.getInstance().getDataset().getWid());
                }
                if (ofile instanceof String) {
                    file.delete();
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
        }

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + GenePTTTables.GENERNT, "WID"));

        if (DataSetPersistence.getInstance().isRunlinks()) {
            GenePTTLinks.getInstance().runLink();
        }
    }

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(GenePTTTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());

        loader();

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(GenePTTTables.getInstance().getTables());
    }
}
