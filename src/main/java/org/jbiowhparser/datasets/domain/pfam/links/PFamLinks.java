package org.jbiowhparser.datasets.domain.pfam.links;

import java.sql.SQLException;

/**
 * This Class is the PFAM external links
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-11-27 12:19:50 +0100 (Tue, 27 Nov 2012) $ $LastChangedRevision: 344 $
 *
 * @since Nov 16, 2012
 */
public class PFamLinks {

    private static PFamLinks singleton;

    private PFamLinks() {
    }

    /**
     * Return a PFamLinks instance
     *
     * @return a PFamLinks instance
     */
    public static synchronized PFamLinks getInstance() {
        if (singleton == null) {
            singleton = new PFamLinks();
        }
        return singleton;
    }

    /**
     * Create all PFam external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {

        /*
         * Create the PFam-Ontology realtionship table
         */
        PFamOntologyLink.getInstance().runLink();

        /*
         * Create the PFam-Taxonomy realtionship table
         */
        PFamTaxonomyLink.getInstance().runLink();

        /**
         * Create the PFAM-Protein relationship table
         * I run this into the load script
         */
        //PFamProteinLink.getInstance().runLink();
    }
}
