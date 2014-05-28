package org.jbiowhparser.datasets.gene.genbank.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.gene.genebank.GeneBankTables;

/**
 * This Class create the GeneBankCDS_has_GeneInfo relationship table
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since May 2, 2013
 */
public class GeneBankGeneLink {

    private static GeneBankGeneLink singleton;

    private GeneBankGeneLink() {
    }

    /**
     * Return a GeneBankGeneLink instance
     *
     * @return a GeneBankGeneLink instance
     */
    public static synchronized GeneBankGeneLink getInstance() {
        if (singleton == null) {
            singleton = new GeneBankGeneLink();
        }
        return singleton;
    }

    /**
     * Create the GeneBank CDS-Gene relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Inserting into table: " + GeneBankTables.GENEBANKCDS_HAS_GENEINFO);

        whdbmsFactory.executeUpdate("insert ignore into "
                + GeneBankTables.GENEBANKCDS_HAS_GENEINFO
                + " (GeneBankCDS_WID,GeneInfo_WID) "
                + " (select c.WID,a.GeneInfo_WID from "
                + GeneBankTables.getInstance().GENEBANKCDS
                + " c inner join "
                + GeneTables.getInstance().GENE2PROTEINACCESSION
                + " a on c.ProteinGi = a.ProteinGi)");
    }
}
