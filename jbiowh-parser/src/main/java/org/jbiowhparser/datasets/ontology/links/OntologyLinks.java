package org.jbiowhparser.datasets.ontology.links;

import java.sql.SQLException;
import org.jbiowhparser.datasets.domain.pfam.links.PFamOntologyLink;
import org.jbiowhparser.datasets.gene.gene.links.GeneOntologyLink;
import org.jbiowhparser.datasets.protein.links.ProteinOntologyLink;

/**
 * This Class create all Ontology external relationship tables
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Aug 6, 2011
 */
public class OntologyLinks {

    private static OntologyLinks singleton;

    private OntologyLinks() {
    }

    /**
     * Return a OntologyLinks
     *
     * @return a OntologyLinks
     */
    public static synchronized OntologyLinks getInstance() {
        if (singleton == null) {
            singleton = new OntologyLinks();
        }
        return singleton;
    }

    /**
     * Create all Ontology external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {

        /*
         * Create the Gene-Ontology realtionship table
         */
        GeneOntologyLink.getInstance().runLink();

        /*
         * Create the Protein-Ontology realtionship table
         */
        ProteinOntologyLink.getInstance().runLink();
        
        /*
         * Create the PFam-Ontology realtionship table
         */
        PFamOntologyLink.getInstance().runLink();
    }
}
