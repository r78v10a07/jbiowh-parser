package org.jbiowhparser.utils;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

/**
 * This class is the FTPFileFilterwithExt
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Mar 6, 2014
 */
public class FTPFileFilterwithExt implements FTPFileFilter {

    private String[] extensions;

    public FTPFileFilterwithExt(String[] extensions) {
        this.extensions = extensions;
    }

    /**
     * Get the value of extensions
     *
     * @return the value of extensions
     */
    public String[] getExtensions() {
        return extensions;
    }

    /**
     * Set the value of extensions
     *
     * @param extensions new value of extensions
     */
    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    @Override
    public boolean accept(FTPFile file) {
        if (extensions == null || extensions.length == 0) {
            return true;
        }
        for (String ext : extensions) {
            if (file.getName().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

}
