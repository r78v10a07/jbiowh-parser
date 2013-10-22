package org.jbiowhparser.datasets.protein.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.ontology.OntologyTables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class create the Protein_has_Ontology relationship table
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
 * @since Aug 6, 2011
 */
public class ProteinOntologyLink {

    private static ProteinOntologyLink singleton;

    private ProteinOntologyLink() {
    }

    /**
     * Return a ProteinOntologyLink
     *
     * @return a ProteinOntologyLink
     */
    public static synchronized ProteinOntologyLink getInstance() {
        if (singleton == null) {
            singleton = new ProteinOntologyLink();
        }
        return singleton;
    }

    /**
     * Create the Protein-Ontology relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_ONTOLOGY);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_ONTOLOGY);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_ONTOLOGY
                + " (Protein_WID,Ontology_WID) "
                + " select p.Protein_WID,o.WID from "
                + ProteinTables.getInstance().PROTEINGO
                + " p inner join "
                + OntologyTables.getInstance().ONTOLOGY
                + " o on o.Id = p.Id");
    }
}
