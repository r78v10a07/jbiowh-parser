package org.jbiowhparser.datasets.pathway.kegg.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class create the KEGGPathway_has_Protein relationship table
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Jul 5, 2012
 */
public class KEGGPathwayProteinLink {

    private static KEGGPathwayProteinLink singleton;

    private KEGGPathwayProteinLink() {
    }

    /**
     * Return a KEGGPathwayProteinLink
     *
     * @return a KEGGPathwayProteinLink
     */
    public static synchronized KEGGPathwayProteinLink getInstance() {
        if (singleton == null) {
            singleton = new KEGGPathwayProteinLink();
        }
        return singleton;
    }

    /**
     * Create the KEGGPathway-Protein relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + KEGGTables.KEGGPATHWAY_HAS_PROTEN);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.KEGGPATHWAY_HAS_PROTEN);
        whdbmsFactory.disableKeys(KEGGTables.KEGGPATHWAY_HAS_PROTEN);

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGPATHWAY_HAS_PROTEN
                + " (KEGGPathway_WID,Protein_WID) "
                + " SELECT kp.KEGGPathway_WID,pe.Protein_WID FROM "
                + ProteinTables.PROTEIN_HAS_KEGGENZYME
                + " pe inner join "
                + KEGGTables.KEGGENZYME_HAS_KEGGPATHWAY
                + " kp on kp.KEGGEnzyme_WID = pe.KEGGEnzyme_WID "
                + " group by kp.KEGGPathway_WID,pe.Protein_WID");

        whdbmsFactory.enableKeys(KEGGTables.KEGGPATHWAY_HAS_PROTEN);
    }
}
