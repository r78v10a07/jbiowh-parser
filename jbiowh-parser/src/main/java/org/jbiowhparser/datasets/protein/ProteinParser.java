package org.jbiowhparser.datasets.protein;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.protein.links.ProteinLinks;
import org.jbiowhparser.datasets.protein.xml.Uniprot;
import org.jbiowhparser.datasets.protein.xml.UniprotDefaultHandler;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.SAXException;

/**
 * This Class is the Protein Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Aug 5, 2011
 */
public class ProteinParser extends ParserFactory implements JBioWHParser {

    /**
     * Run the Protein Parser
     *
     * @throws java.sql.SQLException
     */
    @Override
    public void runLoader() throws SQLException {
        SAXParser saxParser;

        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(ProteinTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : ProteinTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        } else {
            dropTemporalTables();
        }

        downloadFTPdata(new String[]{"xml", ".xml.gz"}, 2);

        File dir = new File(DataSetPersistence.getInstance().getDirectory());
        try {
            Uniprot uniprot = new Uniprot();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            if (dir.isDirectory()) {
                List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{"xml", ".xml.gz"});
                for (File file : files) {

                    saxParser = factory.newSAXParser();
                    if (file.isFile()) {
                        if (file.getCanonicalPath().endsWith(".gz")) {
                            saxParser.parse(new GZIPInputStream(new FileInputStream(file)), new UniprotDefaultHandler(uniprot));
                        } else {
                            saxParser.parse(file, new UniprotDefaultHandler(uniprot));
                        }
                    }

                }
            } else {
                saxParser = factory.newSAXParser();

                if (DataSetPersistence.getInstance().getDirectory().endsWith(".gz")) {
                    saxParser.parse(new GZIPInputStream(new FileInputStream(DataSetPersistence.getInstance().getDirectory())), new UniprotDefaultHandler(uniprot));
                } else {
                    saxParser.parse(new File(DataSetPersistence.getInstance().getDirectory()), new UniprotDefaultHandler(uniprot));
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
            DataSetPersistence.getInstance().getDataset().setStatus("Error");
            DataSetPersistence.getInstance().updateDataSet();
            WIDFactory.getInstance().updateWIDTable();
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

        ParseFiles.getInstance().closeAllPrintWriter();
        whdbmsFactory.loadTSVTables(ProteinTables.getInstance().getTables());
        VerbLogger.getInstance().log(this.getClass(), "Running SQL internal processing");

        whdbmsFactory.disableKeys(ProteinTables.getInstance().PROTEINKEYWORD);
        whdbmsFactory.executeUpdate("ALTER TABLE " + ProteinTables.getInstance().PROTEINKEYWORD + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.getInstance().PROTEINKEYWORD
                + " (Id,Keyword) "
                + "select Id,Keyword from "
                + ProteinTables.getInstance().PROTEINWIDKEYWORDTEMP
                + " group by Id");

        whdbmsFactory.enableKeys(ProteinTables.getInstance().PROTEINKEYWORD);

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + ProteinTables.getInstance().PROTEINKEYWORD, "WID"));

        whdbmsFactory.disableKeys(ProteinTables.PROTEIN_HAS_PROTEINKEYWORD);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_PROTEINKEYWORD
                + " (Protein_WID, ProteinKeyword_WID, Evidence) "
                + "select p.ProteinWID,k.WID,p.Evidence from "
                + ProteinTables.getInstance().PROTEINWIDKEYWORDTEMP
                + " p inner join "
                + ProteinTables.getInstance().PROTEINKEYWORD
                + " k on k.Id = p.Id");

        whdbmsFactory.enableKeys(ProteinTables.PROTEIN_HAS_PROTEINKEYWORD);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.getInstance().PROTEINWIDKEYWORDTEMP);

        for (String table : new String[]{
            ProteinTables.getInstance().PROTEINPDB,
            ProteinTables.getInstance().PROTEINPMID,
            ProteinTables.getInstance().PROTEINKEGG,
            ProteinTables.getInstance().PROTEINREFSEQ,
            ProteinTables.getInstance().PROTEINMINT,
            ProteinTables.getInstance().PROTEINGENE,
            ProteinTables.getInstance().PROTEINDIP,
            ProteinTables.getInstance().PROTEINGO,
            ProteinTables.getInstance().PROTEINPFAM,
            ProteinTables.getInstance().PROTEININTACT,
            ProteinTables.getInstance().PROTEINEC,
            ProteinTables.getInstance().PROTEINBIOCYC,
            ProteinTables.getInstance().PROTEINDRUGBANK}) {

            whdbmsFactory.disableKeys(table);
            whdbmsFactory.executeUpdate("insert into "
                    + table
                    + " (Protein_WID, Id) "
                    + "select Protein_WID, Id from "
                    + table + "Temp"
                    + " group by Protein_WID, Id");

            whdbmsFactory.enableKeys(table);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table + "Temp");
        }

        if (DataSetPersistence.getInstance().isRunlinks()) {
            (new ProteinLinks()).runLink();
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
        cleanTmpDir();
    }

    private void dropTemporalTables() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        for (String table : ProteinTables.getInstance().getTables()) {
            if (table.endsWith("Temp")) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(ProteinTables.getInstance().getTables());
    }
}
