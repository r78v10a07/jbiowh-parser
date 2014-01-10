package org.jbiowhparser.datasets.protgroup.cog.links;

import java.sql.SQLException;

/**
 * This Class is the the COG database links
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 19, 2013
 */
public class COGLinks {

    private static COGLinks singleton;

    private COGLinks() {
    }

    /**
     * Return a COGLinks instance
     *
     * @return a COGLinks instance
     */
    public static synchronized COGLinks getInstance() {
        if (singleton == null) {
            singleton = new COGLinks();
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
         * Create the COG-Taxonomy realtion table
         */
        COGTaxonomyLink.getInstance().runLink();

        /*
         * Create the COG-GeneInfo table
         */
        COGGeneInfoLink.getInstance().runLink();

        /*
         * Create the COG-Protein table
         */
        COGProteinLink.getInstance().runLink();
    }
}
