package org.jbiowhparser.datasets.taxonomy.links;

import java.sql.SQLException;
import org.jbiowhparser.datasets.domain.pfam.links.PFamTaxonomyLink;
import org.jbiowhparser.datasets.gene.genome.links.GenePTTTaxonomyLink;
import org.jbiowhparser.datasets.pathway.kegg.links.KEGGPathwayTaxonomyLink;
import org.jbiowhparser.datasets.protein.links.ProteinTaxonomyLink;
import org.jbiowhparser.datasets.protgroup.cog.links.COGTaxonomyLink;

/**
 * This Class create all Taxonomy external relationship tables
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-10-03 22:11:05 +0200
 * (Wed, 03 Oct 2012) $ $LastChangedRevision: 591 $
 *
 * @since Aug 6, 2011
 */
public class TaxonomyLinks {

    private static TaxonomyLinks singleton;

    private TaxonomyLinks() {
    }

    /**
     * Return a
     *
     * @return a
     */
    public static synchronized TaxonomyLinks getInstance() {
        if (singleton == null) {
            singleton = new TaxonomyLinks();
        }
        return singleton;
    }

    /**
     * Create all Taxonomy external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {

        /*
         * Create the GenePTT-Taxonomy realtionship table
         */
        GenePTTTaxonomyLink.getInstance().runLink();

        /*
         * Create the Protein-Taxonomy realtionship table
         */
        ProteinTaxonomyLink.getInstance().runLink();
        ProteinTaxonomyLink.getInstance().runLinkHost();

        /*
         * Create the KEGGPathway-Taxonomy relationship table
         */
        KEGGPathwayTaxonomyLink.getInstance().runLink();

        /*
         * Create the PFam-Taxonomy realtionship table
         */
        PFamTaxonomyLink.getInstance().runLink();

        /*
         * Create the COG-Taxonomy realtion table
         */
        COGTaxonomyLink.getInstance().runLink();
    }
}
