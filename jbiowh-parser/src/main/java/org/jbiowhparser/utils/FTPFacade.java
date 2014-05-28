package org.jbiowhparser.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.jbiowhcore.logger.VerbLogger;

/**
 * This class is the FTP client facade
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Mar 5, 2014
 */
public class FTPFacade {

    int reply;
    FTPClient ftpClient;
    /**
     * The recursivity depth limit
     */
    private int depth;

    /**
     * Creates a FTP connection
     *
     * @param hostname the FTP hostname
     * @param user the FTP user
     * @param passwd the FTP password
     */
    public FTPFacade(String hostname, String user, String passwd) {
        try {
            ftpClient = new FTPClient();

            VerbLogger.getInstance().log(this.getClass(), "Connection to " + hostname + " as " + user);
            ftpClient.connect(hostname);

            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), "FTP server refused connection.");
                System.exit(-1);
            }
            if (!ftpClient.login(user, passwd)) {
                ftpClient.logout();
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), "Problem login the FTP server");
                System.exit(-1);
            }

            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            depth = -1;
        } catch (IOException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            System.exit(-1);
        }
    }

    /**
     * The FTP client object
     *
     * @return the FTP client object
     */
    public FTPClient getFtpClient() {
        return ftpClient;
    }

    /**
     * Close the connection
     */
    public void close() {
        try {
            VerbLogger.getInstance().log(this.getClass(), "Closing FTP connection");
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Return a list of the files in the path filtered by the extensions
     *
     * @param path the path to find the files
     * @param extensions the extension to filter
     * @return a list of files
     */
    public List listFiles(String path, String[] extensions) {
        try {
            FTPFileFilterwithExt filter = new FTPFileFilterwithExt(extensions);
            return Arrays.asList(ftpClient.listFiles(path, filter));
        } catch (IOException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            System.exit(-1);
        }
        return new ArrayList();
    }

    /**
     * Return a list of the files in the working directory filtered by the
     * extensions
     *
     * @param path the path to find the files
     * @param extensions the extension to filter
     * @param varDepth the depth in recursivity
     * @return a list of files
     */
    public List listFiles(String path, String[] extensions, int varDepth) {
        List files = new ArrayList();
        if (depth == -1) {
            depth = varDepth;
            varDepth = 0;
        }
        if (varDepth < depth) {
            varDepth++;            
            try {
                ftpClient.changeWorkingDirectory(path);
                FTPFileFilterwithExt filter = new FTPFileFilterwithExt(extensions);
                FTPFile[] dirs = ftpClient.listDirectories();
                files.addAll(Arrays.asList(ftpClient.listFiles("./", filter)));
                for (FTPFile d : dirs) {
                    files.addAll(listFiles("./" + d.getName(), extensions, varDepth));
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                System.exit(-1);
            }
        }
        return files;
    }
    
    /**
     * Return a list of the files in the working directory filtered by the
     * extensions
     *
     * @param path the path to find the files
     * @param extensions the extension to filter
     * @param varDepth the depth in recursivity
     * @return a list of files
     */
    public List listFilesString(String path, String[] extensions, int varDepth) {
        List files = new ArrayList();
        if (depth == -1) {
            depth = varDepth;
            varDepth = 0;
        }
        if (varDepth < depth) {
            varDepth++;            
            try {
                ftpClient.changeWorkingDirectory(path);
                FTPFileFilterwithExt filter = new FTPFileFilterwithExt(extensions);
                FTPFile[] dirs = ftpClient.listDirectories();
                for(FTPFile f : ftpClient.listFiles(path, filter)){
                    files.add(path + f.getName());
                }
                for (FTPFile d : dirs) {
                    files.addAll(listFilesString(path + d.getName() + "/", extensions, varDepth));
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                System.exit(-1);
            }
        }
        return files;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
