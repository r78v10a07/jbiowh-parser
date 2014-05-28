package org.jbiowhparser.datasets.domain.pfam;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.domain.pfam.files.PFamSQLfiles;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.domain.pfam.PFamTables;

/**
 * This class is the Pfam Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Oct 24, 2012
 */
public class PFamParser extends ParserFactory implements JBioWHParser {

    final static int BUFFER = 2048;

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(PFamTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        PFamSQLfiles pfam = new PFamSQLfiles();

        downloadFTPdata(pfam.pfamFiles(), 1);

        if (delete) {
            try {
                File dir = new File(DataSetPersistence.getInstance().getDirectory());
                List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{".gz"});
                for (File f : files) {
                    VerbLogger.getInstance().log(this.getClass(), "Uncompressing file: " + f.getCanonicalPath());
                    try (GZIPInputStream zFile = new GZIPInputStream(new FileInputStream(f))) {
                        int count;
                        byte data[] = new byte[BUFFER];
                        int index = f.getCanonicalPath().lastIndexOf(".gz");
                        try (FileOutputStream fos = new FileOutputStream(f.getCanonicalPath().substring(0, index)); BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)) {
                            while ((count = zFile.read(data, 0, BUFFER)) != -1) {
                                dest.write(data, 0, count);
                            }
                        }
                    }
                    f.delete();
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                DataSetPersistence.getInstance().getDataset().setStatus("Error");
                DataSetPersistence.getInstance().updateDataSet();
                WIDFactory.getInstance().updateWIDTable();
                ex.printStackTrace(System.err);
                System.exit(-1);
            }
        }

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : PFamTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        } else {
            dropTemporalTables();
        }

        pfam.loadData();

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
    }

    private void dropTemporalTables() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        for (String table : PFamTables.getInstance().getTables()) {
            if (table.endsWith("Temp")) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(PFamTables.getInstance().getTables());
    }
}
