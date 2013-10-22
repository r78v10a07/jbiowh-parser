package org.jbiowhparser.datasets.gene.genbank.links;

import java.sql.SQLException;

/**
 * This Class is the
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since Apr 30, 2013
 */
public class GeneBankLinks {

    private static GeneBankLinks singleton;

    private GeneBankLinks() {
    }

    /**
     * Return a GeneBankLinks instance
     *
     * @return a GeneBankLinks instance
     */
    public static synchronized GeneBankLinks getInstance() {
        if (singleton == null) {
            singleton = new GeneBankLinks();
        }
        return singleton;
    }

    /**
     * Create all GeneBank external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {

    }
}
