package org.jbiowhparser.datasets.protclust.links;

import java.sql.SQLException;

/**
 * This Class create all UniRef external relationship tables
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Aug 19, 2011
 */
public class UniRefLinks {

    private static UniRefLinks singleton;

    private UniRefLinks() {
    }

    /**
     * Return a UniRefLinks
     *
     * @return a UniRefLinks
     */
    public static synchronized UniRefLinks getInstance() {
        if (singleton == null) {
            singleton = new UniRefLinks();
        }
        return singleton;
    }

    /**
     * Create all Protein external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        /*
         * Create the UniRef-Protein realtionship table
         */
        UniRefProteinLink.getInstance().runLink();
    }
}
