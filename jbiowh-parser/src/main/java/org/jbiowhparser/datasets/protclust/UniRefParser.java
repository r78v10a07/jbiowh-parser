package org.jbiowhparser.datasets.protclust;

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
import org.jbiowhparser.datasets.protclust.links.UniRefLinks;
import org.jbiowhparser.datasets.protclust.xml.UniRef;
import org.jbiowhparser.datasets.protclust.xml.UniRefDefaultHandler;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protclust.UniRefTables;
import org.xml.sax.SAXException;

/**
 * This Class is the UniRef parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Aug 19, 2011
 */
public class UniRefParser extends ParserFactory implements JBioWHParser {

    /**
     * Run the UniRef Parser
     *
     * @throws java.sql.SQLException
     */
    @Override
    public void runLoader() throws SQLException {
        SAXParser saxParser;

        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(UniRefTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        downloadFTPdata(new String[]{"xml", ".xml.gz"}, 2);

        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : UniRefTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }
        try {
            UniRef UniRef = new UniRef();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            if (dir.isDirectory()) {
                List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{"xml", ".xml.gz"});
                for (File file : files) {

                    saxParser = factory.newSAXParser();
                    if (file.isFile()) {
                        if (file.getCanonicalPath().endsWith(".gz")) {
                            saxParser.parse(new GZIPInputStream(new FileInputStream(file)), new UniRefDefaultHandler(UniRef));
                        } else {
                            saxParser.parse(file, new UniRefDefaultHandler(UniRef));
                        }
                    }

                }
            } else {
                saxParser = factory.newSAXParser();

                if (DataSetPersistence.getInstance().getDirectory().endsWith(".gz")) {
                    saxParser.parse(new GZIPInputStream(new FileInputStream(DataSetPersistence.getInstance().getDirectory())), new UniRefDefaultHandler(UniRef));
                } else {
                    saxParser.parse(new File(DataSetPersistence.getInstance().getDirectory()), new UniRefDefaultHandler(UniRef));
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
        whdbmsFactory.loadTSVTables(UniRefTables.getInstance().getTables());
        VerbLogger.getInstance().log(this.getClass(), "Running SQL internal processing");

        if (DataSetPersistence.getInstance().isRunlinks()) {
            UniRefLinks.getInstance().runLink();
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
        clean(UniRefTables.getInstance().getTables());
    }
}
