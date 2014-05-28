package org.jbiowhparser.datasets.ppi;

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
import org.jbiowhparser.datasets.ppi.links.MIF25Links;
import org.jbiowhparser.datasets.ppi.xml.MIF25;
import org.jbiowhparser.datasets.ppi.xml.MIF25DefaultHandler;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.SAXException;

/**
 * This Class is the MIF25 parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Aug 15, 2011
 */
public class MIF25Parser extends ParserFactory implements JBioWHParser {

    /**
     * Run the MIF25 Parser
     *
     * @throws java.sql.SQLException
     */
    @Override
    public void runLoader() throws SQLException {
        SAXParser saxParser;

        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(MIF25Tables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : MIF25Tables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }

        downloadFTPdata(new String[]{"xml", ".xml.gz"}, 2);

        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        try {
            MIF25 MIF25 = new MIF25();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            if (dir.isDirectory()) {
                List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{"xml", ".xml.gz"});
                for (File file : files) {
                    saxParser = factory.newSAXParser();
                    if (file.isFile()) {
                        if (file.getCanonicalPath().endsWith(".gz")) {
                            saxParser.parse(new GZIPInputStream(new FileInputStream(file)), new MIF25DefaultHandler(MIF25));
                        } else {
                            saxParser.parse(file, new MIF25DefaultHandler(MIF25));
                        }
                    }
                }
            } else {
                saxParser = factory.newSAXParser();

                if (DataSetPersistence.getInstance().getDirectory().endsWith(".gz")) {
                    saxParser.parse(new GZIPInputStream(new FileInputStream(DataSetPersistence.getInstance().getDirectory())), new MIF25DefaultHandler(MIF25));
                } else {
                    saxParser.parse(new File(DataSetPersistence.getInstance().getDirectory()), new MIF25DefaultHandler(MIF25));
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
        whdbmsFactory.loadTSVTables(MIF25Tables.getInstance().getTables());
        VerbLogger.getInstance().log(this.getClass(), "Running SQL internal processing");

        if (DataSetPersistence.getInstance().isRunlinks()) {
            MIF25Links.getInstance().runLink();
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
        cleanTmpDir();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(MIF25Tables.getInstance().getTables());
    }
}
