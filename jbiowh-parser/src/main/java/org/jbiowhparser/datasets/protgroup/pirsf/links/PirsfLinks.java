package org.jbiowhparser.datasets.protgroup.pirsf.links;

import java.sql.SQLException;

/**
 * This Class is the PIRSF database links
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 11, 2013
 */
public class PirsfLinks {

    private static PirsfLinks singleton;

    private PirsfLinks() {
    }

    /**
     * Return a PirsfLinks instance
     *
     * @return a PirsfLinks instance
     */
    public static synchronized PirsfLinks getInstance() {
        if (singleton == null) {
            singleton = new PirsfLinks();
        }
        return singleton;
    }

    /**
     * Create all PIRSF external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        /*
         * Create the PIRSF-Protein realtion table
         */
        PirsfProteinLink.getInstance().runLink();
    }
}
