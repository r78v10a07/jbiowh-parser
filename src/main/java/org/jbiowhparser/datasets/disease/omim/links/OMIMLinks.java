package org.jbiowhparser.datasets.disease.omim.links;

import java.sql.SQLException;
import org.jbiowhparser.datasets.gene.gene.links.GeneOMIMLink;

/**
 * This Class create all OMIM external relationship tables
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Aug 20, 2012
 */
public class OMIMLinks {

    private static OMIMLinks sigleton;

    private OMIMLinks() {
    }

    /**
     * Return a OMIMLinks
     *
     * @return a OMIMLinks
     */
    public static synchronized OMIMLinks getInstance() {
        if (sigleton == null) {
            sigleton = new OMIMLinks();
        }
        return sigleton;
    }

    /**
     * Create all Gene external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {

        /*
         * Create the GeneInfo-OMIM relationship table
         */
        GeneOMIMLink.getInstance().runLink();
    }
}
