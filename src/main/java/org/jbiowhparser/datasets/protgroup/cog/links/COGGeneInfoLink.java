package org.jbiowhparser.datasets.protgroup.cog.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.protgroup.cog.COGTables;

/**
 * This Class is the COG-GeneInfo link
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 21, 2013
 */
public class COGGeneInfoLink {

    private static COGGeneInfoLink singleton;

    private COGGeneInfoLink() {
    }

    /**
     * Return a COGGeneInfoLink instance
     *
     * @return a COGGeneInfoLink instance
     */
    public static synchronized COGGeneInfoLink getInstance() {
        if (singleton == null) {
            singleton = new COGGeneInfoLink();
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

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + COGTables.COGORTHOLOGOUSGROUP_HAS_GENEINFO);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + COGTables.COGORTHOLOGOUSGROUP_HAS_GENEINFO);

        whdbmsFactory.executeUpdate("INSERT INTO "
                + COGTables.COGORTHOLOGOUSGROUP_HAS_GENEINFO
                + " (COGOrthologousGroup_WID,GeneInfo_WID) "
                + " SELECT o.WID,t.GeneInfo_WID FROM "
                + COGTables.getInstance().COGMEMBER
                + " m inner join "
                + COGTables.getInstance().COGMEMBER_HAS_GI
                + " ct on ct.Id = m.Id inner join "
                + GeneTables.getInstance().GENE2PROTEINACCESSION
                + " t on t.ProteinGi = ct.Gi group by o.WID,t.GeneInfo_WID");
    }
}
