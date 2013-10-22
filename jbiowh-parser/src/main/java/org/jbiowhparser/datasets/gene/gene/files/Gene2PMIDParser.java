package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene2PMID Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Jul 5, 2011
 */
public class Gene2PMIDParser {

    /**
     * This method load the data in gene2pmid file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2PUBMEDTEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2PUBMEDTEMP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2PMID);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2PMID);
        }

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE2PMIDFILE + " file");

        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE2PUBMEDTEMP, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE2PMIDFILE,
                "  IGNORE 1 LINES"
                + " (@TaxID,GeneID,@PMID)"
                + " set "
                + "PMID=REPLACE(@PMID,'-',NULL)");

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENE2PMID
                + "(GeneInfo_WID,"
                + "PMID) "
                + "select "
                + "g.WID,"
                + "a.PMID "
                + "from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENE2PUBMEDTEMP
                + " a on a.GeneID = g.GeneID where a.PMID is not NULL");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2PUBMEDTEMP);
    }
}
