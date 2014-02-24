package org.jbiowhparser.datasets.protgroup.ncbiprotclust.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.protgroup.ncbiprotclust.ProtClustTables;

/**
 * This Class is the ProtClust-GeneInfo link
 *
 * $Author$
 * $LastChangedDate$
 * $LastChangedRevision$
 * @since Jan 14, 2014
 */
public class ProtClustGeneInfoLink {

    private static ProtClustGeneInfoLink singleton;

    private ProtClustGeneInfoLink() {
    }

    /**
     * Return a ProtClustGeneInfoLink instance
     *
     * @return a ProtClustGeneInfoLink instance
     */
    public static synchronized ProtClustGeneInfoLink getInstance() {
        if (singleton == null) {
            singleton = new ProtClustGeneInfoLink();
        }
        return singleton;
    }
    
    /**
     * Create the Protein-GeneInfo relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProtClustTables.PROTCLUST_HAS_GENEINFO);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProtClustTables.PROTCLUST_HAS_GENEINFO);

        whdbmsFactory.enableKeys(ProtClustTables.PROTCLUST_HAS_GENEINFO);
        whdbmsFactory.executeUpdate("INSERT INTO "
                + ProtClustTables.PROTCLUST_HAS_GENEINFO
                + " (ProtClust_WID,GeneInfo_WID) "
                + " SELECT cp.ProtClust_WID,g.WID FROM "
                + ProtClustTables.getInstance().PROTCLUSTPROTEINS
                + "  cp INNER JOIN  "
                + GeneTables.getInstance().GENEINFO
                + "  g ON g.GeneID = cp.GeneGi GROUP BY cp.ProtClust_WID,g.WID");
    }
 }
