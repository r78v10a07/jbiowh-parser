package org.jbiowhparser.datasets.pathway.kegg.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This Class create the KEGGPathway_has_GeneInfo relationship table
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Jul 5, 2012
 */
public class KEGGPathwayGeneInfoLink {

    private static KEGGPathwayGeneInfoLink singleton;

    private KEGGPathwayGeneInfoLink() {
    }

    /**
     * Return a KEGGPathwayGeneInfoLink
     *
     * @return a KEGGPathwayGeneInfoLink
     */
    public static synchronized KEGGPathwayGeneInfoLink getInstance() {
        if (singleton == null) {
            singleton = new KEGGPathwayGeneInfoLink();
        }
        return singleton;
    }

    /**
     * Create the KEGGPathway-GeneInfo relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + KEGGTables.KEGGPATHWAY_HAS_GENEINFO);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.KEGGPATHWAY_HAS_GENEINFO);

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGPATHWAY_HAS_GENEINFO
                + " (KEGGPathway_WID,GeneInfo_WID) "
                + " SELECT kp.KEGGPathway_WID,pe.GeneInfo_WID FROM "
                + GeneTables.GENEINFO_HAS_KEGGGENE
                + " pe inner join "
                + KEGGTables.KEGGGENE_HAS_KEGGPATHWAY
                + " kp on kp.KEGGGene_WID = pe.KEGGgene_WID "
                + " group by kp.KEGGPathway_WID,pe.GeneInfo_WID");
    }
}
