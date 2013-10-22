package org.jbiowhparser.datasets.gene.genome.links;

import java.sql.SQLException;
import org.jbiowhparser.datasets.protein.links.ProteinGenePTTLink;

/**
 * This Class create all GenePTT external relationship tables
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Mar 13, 2013
 */
public class GenePTTLinks {

    private static GenePTTLinks singleton;

    private GenePTTLinks() {
    }

    /**
     * Return a GenePTTLinks instance
     *
     * @return a GenePTTLinks instance
     */
    public static synchronized GenePTTLinks getInstance() {
        if (singleton == null) {
            singleton = new GenePTTLinks();
        }
        return singleton;
    }

    /**
     * Create all Gene external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        /*
         * Create the Gene-GenePTT realtionship table
         */
        GeneGenePTTLink.getInstance().runLink();
        /*
         * Create the Protein-GenePTT realtionship table
         */
        ProteinGenePTTLink.getInstance().runLink();
        /*
         * Create the GenePTT-Taxonomy realtionship table
         */
        GenePTTTaxonomyLink.getInstance().runLink();
    }
}
