package org.jbiowhparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.utils.FTPFacade;
import org.jbiowhpersistence.datasets.DataSetPersistence;

/**
 * This class is
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Oct 25, 2013
 */
public class ParserFactory {

    protected boolean delete = false;

    /**
     * Clean the tables
     *
     * @param tables list of tables to be cleaned
     * @throws SQLException
     */
    public void clean(List<String> tables) throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        for (String table : tables) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
        }
    }

    /**
     * Download the files from the FTP using the extension to the temporal
     * directory
     *
     * @param extensions the file's extensions to be used
     * @param depth the depth in recursivity inside the FTP server
     */
    public void downloadFTPdata(String[] extensions, int depth) {

        if (DataSetPersistence.getInstance().isonlineFTP()) {
            FTPFacade ftp = new FTPFacade(DataSetPersistence.getInstance().getOnlineSite(),
                    DataSetPersistence.getInstance().getOnlineUser(),
                    DataSetPersistence.getInstance().getOnlinePasswd());
            List files;
            if (DataSetPersistence.getInstance().getOnlinePath().endsWith("/")) {
                files = ftp.listFilesString(DataSetPersistence.getInstance().getOnlinePath(), extensions, depth);
            } else {
                files = new ArrayList();
                files.add(DataSetPersistence.getInstance().getOnlinePath());
            }

            for (String f : (List<String>) files) {
                int index = f.lastIndexOf("/");
                try (OutputStream tmpFile = new FileOutputStream(DataSetPersistence.getInstance().getTempdir() + f.substring(index + 1))) {
                    ftp.getFtpClient().retrieveFile(f, tmpFile);
                } catch (IOException ex) {
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                    System.exit(-1);
                }
            }
            ftp.close();
            DataSetPersistence.getInstance().setDirectory(DataSetPersistence.getInstance().getTempdir());
            delete = true;
        }
    }

    /**
     * Delete all files into the temporal directory
     *
     */
    public void cleanTmpDir() {
        if (delete) {
            File dir = new File(DataSetPersistence.getInstance().getDirectory());
            for (File f : dir.listFiles()) {
                f.delete();
            }
        }
    }
}
