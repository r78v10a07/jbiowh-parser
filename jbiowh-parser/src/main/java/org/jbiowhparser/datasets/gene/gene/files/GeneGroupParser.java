package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the GeneGroup Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Jul 5, 2011
 */
public class GeneGroupParser {

    /**
     * This method load the data in gene_group file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE_GROUP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE_GROUP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENEGROUP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEGROUP);
        }

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE_GROUP + " file");

        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE_GROUP, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE_GROUP,
                "  IGNORE 1 LINES"
                + " (@TaxID,GeneID,@Relationship,@OtherTaxID,OtherGeneID)"
                + " set "
                + "Relationship=REPLACE(@Relationship,'-',NULL)");

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENEGROUP
                + " (GeneInfo_WID,"
                + "Relationship,"
                + "OtherGeneInfo_WID) "
                + "select "
                + "g.WID,"
                + "a.Relationship,"
                + "g1.WID "
                + "from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENE_GROUP
                + " a on a.GeneID = g.GeneID inner join "
                + GeneTables.getInstance().GENEINFO
                + " g1 on g1.GeneID = a.OtherGeneID where a.Relationship is not NULL");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE_GROUP);
    }
}
