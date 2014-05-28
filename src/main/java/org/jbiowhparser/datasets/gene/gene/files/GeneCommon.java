package org.jbiowhparser.datasets.gene.gene.files;

import java.io.File;
import java.io.IOException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.utils.FTPFacade;
import org.jbiowhpersistence.datasets.DataSetPersistence;

/**
 * This class is used to check if the Data set is online
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Mar 5, 2014
 */
public class GeneCommon {

    String directory = null;
    FTPFacade ftp = null;

    /**
     * Check if it is an online dataset and copyAndCloseGzip the compress file
     * to the local temporary directory
     *
     * @param fileName the file to be donwloaded from the FTP site
     * @param ext the file extension
     */
    public void checkOnlineData(String fileName, String ext) {
        if (DataSetPersistence.getInstance().isonlineFTP()) {
            directory = DataSetPersistence.getInstance().getDirectory();
            DataSetPersistence.getInstance().setDirectory(DataSetPersistence.getInstance().getTempdir());
            ftp = new FTPFacade(DataSetPersistence.getInstance().getOnlineSite(),
                    DataSetPersistence.getInstance().getOnlineUser(),
                    DataSetPersistence.getInstance().getOnlinePasswd());

            try {
                if (ext.equals(".gz")) {
                    ParseFiles.getInstance().copyAndCloseGzip(ftp.getFtpClient().retrieveFileStream(DataSetPersistence.getInstance().getOnlinePath() + fileName + ext),
                            DataSetPersistence.getInstance().getDirectory() + fileName);
                } else {
                    ParseFiles.getInstance().copyAndClose(ftp.getFtpClient().retrieveFileStream(DataSetPersistence.getInstance().getOnlinePath() + fileName + ext),
                            DataSetPersistence.getInstance().getDirectory() + fileName);
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                System.exit(-1);
            }
        }
    }

    /**
     * Close the FTP connection and delete the temporal file
     *
     * @param fileName the file to be delete
     */
    public void closeOnlineData(String fileName) {
        if (directory != null && ftp != null) {
            new File(DataSetPersistence.getInstance().getDirectory() + fileName).delete();
            DataSetPersistence.getInstance().setDirectory(directory);
            ftp.close();
        }
    }
}
