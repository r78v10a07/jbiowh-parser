package org.jbiowhparser.datasets.gene.gene.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.disease.omim.OMIMTables;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene OMIM link
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Aug 20, 2012
 */
public class GeneOMIMLink {

    private static GeneOMIMLink sigleton;

    private GeneOMIMLink() {
    }

    /**
     * Return a GeneOMIMLink
     *
     * @return a GeneOMIMLink
     */
    public static synchronized GeneOMIMLink getInstance() {
        if (sigleton == null) {
            sigleton = new GeneOMIMLink();
        }
        return sigleton;
    }

    /**
     * Create the Gene-OMIM relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + GeneTables.GENEINFO_HAS_OMIM);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENEINFO_HAS_OMIM);

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.GENEINFO_HAS_OMIM
                + "(GeneInfo_WID, OMIM_WID) "
                + "select g.WID,o.WID from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().MIM2GENE
                + " m on m.GeneID = g.GeneId inner join "
                + OMIMTables.getInstance().OMIM
                + " o on m.MIM = o.OMIM_ID group by g.WID,o.WID");
    }
}
