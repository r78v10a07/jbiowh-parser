package org.jbiowhparser.datasets.protgroup.ncbiprotclust;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.protgroup.ncbiprotclust.files.NCBIBCPParser;
import org.jbiowhparser.datasets.protgroup.ncbiprotclust.links.ProtClustLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.ncbiprotclust.ProtClustTables;

/**
 * This class is the NCBI Protein cluster database parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Jan 13, 2014
 */
public class NCBIProtClustParser extends ParserFactory implements JBioWHParser {

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : ProtClustTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }

        if (dir.isDirectory()) {
            List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{"_Clusters.bcp", "_Clusters.bcp.gz"});
            int i = 0;
            for (File file : files) {
                InputStream in;
                NCBIBCPParser parser = new NCBIBCPParser();
                try {
                    VerbLogger.getInstance().log(this.getClass(), "File: " + (i++) + " of: " + files.size());
                    if (file.isFile() && file.getCanonicalPath().endsWith(".gz")) {
                        in = new GZIPInputStream(new FileInputStream(file));
                    } else {
                        in = new FileInputStream(file);
                    }
                    VerbLogger.getInstance().log(this.getClass(), "Parsing file " + i + " " + file.getCanonicalPath());
                    parser.parser(in);
                } catch (IOException ex) {
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                }
            }
            if (DataSetPersistence.getInstance().isRunlinks()) {
                ProtClustLinks.getInstance().runLink();
            }
        }
        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(ProtClustTables.getInstance().getTables());
    }

}
