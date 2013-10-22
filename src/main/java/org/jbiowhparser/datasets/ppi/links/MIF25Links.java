package org.jbiowhparser.datasets.ppi.links;

import java.sql.SQLException;

/**
 * This Class create all MIF25 external relationship tables
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Aug 20, 2011
 */
public class MIF25Links {

    private static MIF25Links singleton;

    private MIF25Links() {
    }

    /**
     * Return a MIF25Links
     *
     * @return a MIF25Links
     */
    public static synchronized MIF25Links getInstance() {
        if (singleton == null) {
            singleton = new MIF25Links();
        }
        return singleton;
    }

    /**
     * Create all MIF25 external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        /*
         * Create the Protein-Taxonomy realtionship table
         */
        MIF25ProteinLink.getInstance().runLink();
    }
}
