package org.jbiowhparser.datasets.protein.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class create the Protein_has_Gene relationship table
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since Aug 6, 2011
 */
public class ProteinGeneLink {

    private static ProteinGeneLink singleton;

    private ProteinGeneLink() {
    }

    /**
     * Return a ProteinGeneLink
     *
     * @return a ProteinGeneLink
     */
    public static synchronized ProteinGeneLink getInstance() {
        if (singleton == null) {
            singleton = new ProteinGeneLink();
        }
        return singleton;
    }

    /**
     * Create the Protein-Gene relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_GENEINFO);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_GENEINFO);
        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_GENEINFO, false);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_GENEINFO
                + " (Protein_WID,GeneInfo_WID) "
                + " (select p.Protein_WID,g.WID from "
                + ProteinTables.getInstance().PROTEINGENE
                + " p inner join "
                + GeneTables.getInstance().GENEINFO
                + " g on g.GeneId = p.Id)"
                + " UNION "
                + " (select p.Protein_WID,g.GeneInfo_WID from "
                + ProteinTables.getInstance().PROTEINREFSEQ
                + " p inner join "
                + GeneTables.getInstance().GENE2PROTEINACCESSION
                + " g on p.Id = g.ProteinAccession)"
                + " UNION "
                + " (select pa.Protein_WID,a.GeneInfo_WID from "
                + GeneTables.getInstance().GENE2PROTEINACCESSION
                + " a inner join "
                + GeneTables.getInstance().GENEREFSEQUNIPROT
                + " gp on gp.ProteinAccession = a.ProteinAccession inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = gp.UniProtKBProteinAccession)");

        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_GENEINFO, true);
    }
}
