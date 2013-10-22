package org.jbiowhparser.datasets.gene.gene.links;

import java.sql.SQLException;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.datasets.gene.genbank.links.GeneBankGeneLink;
import org.jbiowhparser.datasets.gene.genome.links.GeneGenePTTLink;
import org.jbiowhparser.datasets.pathway.kegg.links.KEGGPathwayGeneInfoLink;
import org.jbiowhparser.datasets.protein.links.ProteinGeneLink;
import org.jbiowhpersistence.datasets.gene.genebank.GeneBankTables;

/**
 * This Class create all Gene external relationship tables
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Aug 6, 2011
 */
public class GeneLinks {

    private static GeneLinks singleton;

    private GeneLinks() {
    }

    /**
     * Return a GeneLinks
     *
     * @return a GeneLinks
     */
    public static synchronized GeneLinks getInstance() {
        if (singleton == null) {
            singleton = new GeneLinks();
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
         * Create the Gene-Ontology realtionship table
         */
        GeneOntologyLink.getInstance().runLink();

        /*
         * Create the Protein-Gene realtionship table
         */
        ProteinGeneLink.getInstance().runLink();

        /*
         * Create the Gene-KEGGGene realtionship table
         */
        GeneKEGGGeneLink.getInstance().runLink();

        /*
         * Create the KEGGPathway-GeneInfo relationship table
         */
        KEGGPathwayGeneInfoLink.getInstance().runLink();

        /*
         * Create the GeneInfo-OMIM relationship table
         */
        GeneOMIMLink.getInstance().runLink();
        
        /*
         * Create the GeneBank CDS-Gene realtionship table
         */
        JBioWHDBMS.getInstance().getWhdbmsFactory().executeUpdate("TRUNCATE TABLE " + GeneBankTables.GENEBANKCDS_HAS_GENEINFO);
        GeneBankGeneLink.getInstance().runLink();
    }
}
