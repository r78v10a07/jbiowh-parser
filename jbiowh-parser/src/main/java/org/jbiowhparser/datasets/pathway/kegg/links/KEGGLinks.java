package org.jbiowhparser.datasets.pathway.kegg.links;

import java.sql.SQLException;
import org.jbiowhparser.datasets.gene.gene.links.GeneKEGGGeneLink;
import org.jbiowhparser.datasets.protein.links.ProteinKEGGEnzymeLink;

/**
 * This class create all KEGG external relationship tables
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Dec 13, 2011
 */
public class KEGGLinks {

    private static KEGGLinks singleton;

    private KEGGLinks() {
    }

    /**
     * Return a KEGGLinks
     *
     * @return a KEGGLinks
     */
    public static synchronized KEGGLinks getInstance() {
        if (singleton == null) {
            singleton = new KEGGLinks();
        }
        return singleton;
    }

    /**
     * Create all KEGG external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {

        /*
         * Create the KEGGPathway-Taxonomy relationship table
         */
        KEGGPathwayTaxonomyLink.getInstance().runLink();

        /*
         * Create the Protein-KEGGEnzyme realtionship table
         */
        ProteinKEGGEnzymeLink.getInstance().runLink();

        /*
         * Create the KEGGPathway-Protein relationship table
         */
        KEGGPathwayProteinLink.getInstance().runLink();

        /*
         * Create the Gene-KEGGGene realtionship table
         */
        GeneKEGGGeneLink.getInstance().runLink();

        /*
         * Create the KEGGPathway-GeneInfo relationship table
         */
        KEGGPathwayGeneInfoLink.getInstance().runLink();

        /*
         * Create the KEGGCompund-DrugBank relationship table
         */
        KEGGCompoundDrugBankLink.getInstance().runLink();
    }
}
