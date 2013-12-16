package org.jbiowhparser.datasets.protein.links;

import java.sql.SQLException;
import org.jbiowhparser.datasets.domain.pfam.links.PFamProteinLink;
import org.jbiowhparser.datasets.drug.drugbank.links.DrugBankProteinLink;
import org.jbiowhparser.datasets.pathway.kegg.links.KEGGPathwayProteinLink;
import org.jbiowhparser.datasets.ppi.links.MIF25ProteinLink;
import org.jbiowhparser.datasets.protclust.links.UniRefProteinLink;
import org.jbiowhparser.datasets.protgroup.pirsf.links.PirsfProteinLink;

/**
 * This Class create all Protein external relationship tables
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Aug 6, 2011
 */
public class ProteinLinks {

    /**
     * Create all Protein external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        /*
         * Create the Protein-Taxonomy realtionship table
         */
        ProteinTaxonomyLink.getInstance().runLink();
        ProteinTaxonomyLink.getInstance().runLinkHost();

        /*
         * Create the Protein-Ontology realtionship table
         */
        ProteinOntologyLink.getInstance().runLink();

        /*
         * Create the Protein-Gene realtionship table
         */
        ProteinGeneLink.getInstance().runLink();

        /*
         * Create the Protein-GenePTT realtionship table
         */
        ProteinGenePTTLink.getInstance().runLink();

        /*
         * Create the Protein-Taxonomy realtionship table
         */
        MIF25ProteinLink.getInstance().runLink();

        /*
         * Create the UniRef-Protein realtionship table
         */
        UniRefProteinLink.getInstance().runLink();

        /*
         * Create the Protein-DrugBank realtionship table
         */
        DrugBankProteinLink.getInstance().runLink();

        /*
         * Create the Protein-KEGGEnzyme realtionship table
         */
        ProteinKEGGEnzymeLink.getInstance().runLink();

        /*
         * Create the KEGGPathway-Protein relationship table
         */
        KEGGPathwayProteinLink.getInstance().runLink();

        /**
         * Create the PFAM-Protein relationship table
         */
        PFamProteinLink.getInstance().runLink();

        /*
         * Create the PIRSF-Protein realtion table
         */
        PirsfProteinLink.getInstance().runLink();
    }
}
