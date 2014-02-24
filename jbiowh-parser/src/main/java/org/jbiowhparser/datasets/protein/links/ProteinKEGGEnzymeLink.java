package org.jbiowhparser.datasets.protein.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class create the Protein_has_KEGGEnzyme relationship table
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since May 18, 2012
 */
public class ProteinKEGGEnzymeLink {

    private static ProteinKEGGEnzymeLink singleton;

    private ProteinKEGGEnzymeLink() {
    }

    /**
     * Return a ProteinKEGGEnzymeLink
     *
     * @return a ProteinKEGGEnzymeLink
     */
    public static synchronized ProteinKEGGEnzymeLink getInstance() {
        if (singleton == null) {
            singleton = new ProteinKEGGEnzymeLink();
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

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_KEGGENZYME);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_KEGGENZYME);
        whdbmsFactory.disableKeys(ProteinTables.PROTEIN_HAS_KEGGENZYME);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_KEGGENZYME
                + " (Protein_WID,KEGGEnzyme_WID) "
                + " SELECT p.Protein_WID,k.WID FROM "
                + ProteinTables.getInstance().PROTEINEC
                + " p inner join "
                + KEGGTables.getInstance().KEGGENZYME
                + " k on k.Entry = p.Id");

        whdbmsFactory.enableKeys(ProteinTables.PROTEIN_HAS_KEGGENZYME);
    }
}
