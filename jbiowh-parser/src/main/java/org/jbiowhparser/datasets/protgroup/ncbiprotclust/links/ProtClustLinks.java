package org.jbiowhparser.datasets.protgroup.ncbiprotclust.links;

import java.sql.SQLException;

/**
 * This Class is the the NCBI Protein Cluster database links
 *
 * $Author$
 * $LastChangedDate$
 * $LastChangedRevision$
 * @since Jan 14, 2014
 */
public class ProtClustLinks {

    private static ProtClustLinks singleton;

    private ProtClustLinks() {
    }

    /**
     * Return a ProtClustLinks instance
     *
     * @return a ProtClustLinks instance
     */
    public static synchronized ProtClustLinks getInstance() {
        if (singleton == null) {
            singleton = new ProtClustLinks();
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
         * Create the ProtClust-GeneInfo realtion table
         */
        ProtClustGeneInfoLink.getInstance().runLink();

        
    }
 }
