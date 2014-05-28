package org.jbiowhparser.datasets.protgroup.cog.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.protgroup.cog.COGTables;
import org.jbiowhpersistence.datasets.taxonomy.TaxonomyTables;

/**
 * This Class is the
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 21, 2013
 */
public class COGTaxonomyLink {

    private static COGTaxonomyLink singleton;

    private COGTaxonomyLink() {
    }

    /**
     * Return a COGTaxonomyLink instance
     *
     * @return a COGTaxonomyLink instance
     */
    public static synchronized COGTaxonomyLink getInstance() {
        if (singleton == null) {
            singleton = new COGTaxonomyLink();
        }
        return singleton;
    }

    /**
     * Create the Protein-Taxonomy relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + COGTables.COGORTHOLOGOUSGROUP_HAS_TAXONOMY);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + COGTables.COGORTHOLOGOUSGROUP_HAS_TAXONOMY);

        whdbmsFactory.executeUpdate("insert into "
                + COGTables.COGORTHOLOGOUSGROUP_HAS_TAXONOMY
                + " (COGOrthologousGroup_WID,Taxonomy_WID) "
                + " select o.WID,t.WID from  "
                + COGTables.getInstance().COGORTHOLOGOUSGROUP
                + " o inner join "
                + COGTables.getInstance().COGMEMBER
                + " m on o.WID = m.COGOrthologousGroup_WID inner join "
                + COGTables.getInstance().COGMEMBER_TAXID
                + " ct on ct.Organism = m.Organism inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t on t.TaxId = ct.TaxId group by o.WID,t.WID");
    }
}
