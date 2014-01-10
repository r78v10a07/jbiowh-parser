package org.jbiowhparser.datasets.protein.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.jbiowhpersistence.datasets.taxonomy.TaxonomyTables;

/**
 * This Class create the Protein_has_Taxonomy relationship table
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Aug 6, 2011
 */
public class ProteinTaxonomyLink {

    private static ProteinTaxonomyLink singleton;

    private ProteinTaxonomyLink() {
    }

    /**
     * Return a ProteinTaxonomyLink
     *
     * @return a ProteinTaxonomyLink
     */
    public static synchronized ProteinTaxonomyLink getInstance() {
        if (singleton == null) {
            singleton = new ProteinTaxonomyLink();
        }
        return singleton;
    }

    /**
     * Create the Protein-Taxonomy relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_TAXONOMY);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_TAXONOMY);
        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_TAXONOMY, false);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_TAXONOMY
                + " (Protein_WID,Taxonomy_WID) "
                + " select p.Protein_WID,t.WID from "
                + ProteinTables.getInstance().PROTEINTAXID
                + " p inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t on t.TaxId = p.TaxId where p.IsHost = 0");

        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_TAXONOMY, true);
    }

    /**
     * Create the Protein-Taxonomy Host relationship table
     *
     * @throws SQLException
     */
    public void runLinkHost() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_TAXONOMYHOST);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_TAXONOMYHOST);
        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_TAXONOMYHOST, false);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_TAXONOMYHOST
                + " (Protein_WID,Taxonomy_WID) "
                + " select p.Protein_WID,t.WID from "
                + ProteinTables.getInstance().PROTEINTAXID
                + " p inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t on t.TaxId = p.TaxId where p.IsHost = 1");

        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_TAXONOMYHOST, true);
    }
}
