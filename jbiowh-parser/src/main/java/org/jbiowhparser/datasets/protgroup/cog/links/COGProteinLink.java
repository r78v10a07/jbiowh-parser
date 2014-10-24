package org.jbiowhparser.datasets.protgroup.cog.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.jbiowhpersistence.datasets.protgroup.cog.COGTables;

/**
 * This Class is the COG-Protein link
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Jan 10, 2014
 */
public class COGProteinLink {

    private static COGProteinLink singleton;

    private COGProteinLink() {
    }

    /**
     * Return a COGProteinLink instance
     *
     * @return a COGProteinLink instance
     */
    public static synchronized COGProteinLink getInstance() {
        if (singleton == null) {
            singleton = new COGProteinLink();
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

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + COGTables.COGORTHOLOGOUSGROUP_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + COGTables.COGORTHOLOGOUSGROUP_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("INSERT IGNORE INTO "
                + COGTables.COGORTHOLOGOUSGROUP_HAS_PROTEIN
                + " (COGOrthologousGroup_WID,Protein_WID) "
                + " SELECT COGOrthologousGroup_WID,Protein_WID FROM "
                + COGTables.getInstance().COGMEMBER
                + " m inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + "  n on n.AccessionNumber = m.Id");

        whdbmsFactory.executeUpdate("INSERT IGNORE INTO "
                + COGTables.COGORTHOLOGOUSGROUP_HAS_PROTEIN
                + " (COGOrthologousGroup_WID,Protein_WID) "
                + " SELECT COGOrthologousGroup_WID,Protein_WID FROM "
                + COGTables.getInstance().COGMEMBER
                + " m inner join "
                + ProteinTables.getInstance().PROTEINDBREFERENCE
                + "  n on n.Id = m.Id");
        
        whdbmsFactory.executeUpdate("INSERT IGNORE INTO "
                + COGTables.COGORTHOLOGOUSGROUP_HAS_PROTEIN
                + " (COGOrthologousGroup_WID,Protein_WID) "
                + " SELECT  c.WID,r.Protein_WID FROM "
                + ProteinTables.getInstance().PROTEINDBREFERENCE
                + " r inner join "
                + COGTables.getInstance().COGORTHOLOGOUSGROUP
                + " c on r.Id = c.Id where r.Type = 'eggNOG'");
    }
}
