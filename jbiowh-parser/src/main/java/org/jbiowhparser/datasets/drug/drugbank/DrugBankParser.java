package org.jbiowhparser.datasets.drug.drugbank;

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
import org.jbiowhparser.datasets.drug.drugbank.links.DrugBankLinks;
import org.jbiowhparser.datasets.drug.drugbank.xml.DrugBankDefaultHandler;
import org.jbiowhparser.datasets.drug.drugbank.xml.Drugs;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.drug.drugbank.DrugBankTables;
import org.xml.sax.SAXException;

/**
 * This class is the DrugBank Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Sep 9, 2011
 */
public class DrugBankParser extends ParserFactory implements JBioWHParser {

    /**
     * Run the DrugBank Parser
     *
     * @throws java.sql.SQLException
     */
    @Override
    public void runLoader() throws SQLException {
        SAXParser saxParser;

        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(DrugBankTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : DrugBankTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }

        try {
            Drugs drugs = new Drugs();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            if (dir.isDirectory()) {
                List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{"xml", ".xml.gz"});
                for (File file : files) {
                    saxParser = factory.newSAXParser();
                    if (file.isFile()) {
                        if (file.getCanonicalPath().endsWith(".gz")) {
                            saxParser.parse(new GZIPInputStream(new FileInputStream(file)), new DrugBankDefaultHandler(drugs));
                        } else {
                            saxParser.parse(file, new DrugBankDefaultHandler(drugs));
                        }
                    }
                }
            } else {
                saxParser = factory.newSAXParser();

                if (DataSetPersistence.getInstance().getDirectory().endsWith(".gz")) {
                    saxParser.parse(new GZIPInputStream(new FileInputStream(DataSetPersistence.getInstance().getDirectory())), new DrugBankDefaultHandler(drugs));
                } else {
                    saxParser.parse(new File(DataSetPersistence.getInstance().getDirectory()), new DrugBankDefaultHandler(drugs));
                }

            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
            DataSetPersistence.getInstance().getDataset().setStatus("Error");
            DataSetPersistence.getInstance().updateDataSet();
            WIDFactory.getInstance().updateWIDTable();
            System.exit(-1);
        }

        ParseFiles.getInstance().closeAllPrintWriter();
        whdbmsFactory.loadTSVTables(DrugBankTables.getInstance().getTables());
        VerbLogger.getInstance().log(this.getClass(), "Running SQL internal processing");

        whdbmsFactory.executeUpdate("ALTER TABLE " + DrugBankTables.getInstance().DRUGBANKPATENTS + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("insert into "
                + DrugBankTables.getInstance().DRUGBANKPATENTS
                + " (Number,Country,Approved,Expires)"
                + " SELECT Number,Country,Approved,Expires FROM "
                + DrugBankTables.getInstance().DRUGBANKPATENTSTEMP
                + " group by Number,Country,Approved,Expires");

        whdbmsFactory.executeUpdate("insert into "
                + DrugBankTables.DRUGBANK_HAS_DRUGBANKPATENTS
                + " (DrugBank_WID,DrugBankPatent_WID) select st.DrugBank_WID,s.WID from "
                + DrugBankTables.getInstance().DRUGBANKPATENTSTEMP
                + " st inner join "
                + DrugBankTables.getInstance().DRUGBANKPATENTS
                + " s on s.Number = st.Number and"
                + " s.Country = st.Country and"
                + " s.Approved = st.Approved and"
                + " s.Expires = st.Expires");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + DrugBankTables.getInstance().DRUGBANKPATENTS, "WID"));

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + DrugBankTables.getInstance().DRUGBANKPATENTSTEMP);

        whdbmsFactory.executeUpdate("ALTER TABLE " + DrugBankTables.getInstance().DRUGBANKCATEGORIES + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("insert into "
                + DrugBankTables.getInstance().DRUGBANKCATEGORIES
                + " (Category)"
                + " SELECT Category FROM "
                + DrugBankTables.getInstance().DRUGBANKCATEGORIESTEMP
                + " group by Category");

        whdbmsFactory.executeUpdate("insert into "
                + DrugBankTables.DRUGBANK_HAS_DRUGBANKCATEGORIES
                + " (DrugBank_WID,DrugBankCategory_WID) select st.DrugBank_WID,s.WID from "
                + DrugBankTables.getInstance().DRUGBANKCATEGORIESTEMP
                + " st inner join "
                + DrugBankTables.getInstance().DRUGBANKCATEGORIES
                + " s on s.Category = st.Category");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + DrugBankTables.getInstance().DRUGBANKCATEGORIES, "WID"));

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + DrugBankTables.getInstance().DRUGBANKCATEGORIESTEMP);

        whdbmsFactory.executeUpdate("insert into "
                + DrugBankTables.getInstance().DRUGBANKDRUGINTERACTIONS
                + " (DrugBank_WID,Drug,Description)"
                + " SELECT t.DrugBank_WID,d.WID,t.Description FROM "
                + DrugBankTables.getInstance().DRUGBANKDRUGINTERACTIONSTEMP
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANK
                + " d on d.Id = t.Id"
                + " group by DrugBank_WID,d.WID");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + DrugBankTables.getInstance().DRUGBANKDRUGINTERACTIONSTEMP);

        if (DataSetPersistence.getInstance().isRunlinks()) {
            (new DrugBankLinks()).runLink();
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(DrugBankTables.getInstance().getTables());
    }
}
