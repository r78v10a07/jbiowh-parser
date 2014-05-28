package org.jbiowhparser.datasets.gene.gene.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This Class is the Gene KEGGGene link
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since May 18, 2012
 */
public class GeneKEGGGeneLink {

    private static GeneKEGGGeneLink singleton;

    private GeneKEGGGeneLink() {
    }

    /**
     * Return a GeneKEGGGeneLink
     *
     * @return a GeneKEGGGeneLink
     */
    public static synchronized GeneKEGGGeneLink getInstance() {
        if (singleton == null) {
            singleton = new GeneKEGGGeneLink();
        }
        return singleton;
    }

    /**
     * Create the Gene-Ontology relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + GeneTables.GENEINFO_HAS_KEGGGENE);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENEINFO_HAS_KEGGGENE);

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.GENEINFO_HAS_KEGGGENE
                + " (GeneInfo_WID,KEGGGene_WID) "
                + " SELECT g.WID,k.KEGGGene_WID FROM "
                + KEGGTables.getInstance().KEGGGENEDBLINK
                + " k inner join "
                + GeneTables.getInstance().GENEINFO
                + " g on g.GeneId = k.Id where DB = 'NCBI-GeneID'");
    }
}
