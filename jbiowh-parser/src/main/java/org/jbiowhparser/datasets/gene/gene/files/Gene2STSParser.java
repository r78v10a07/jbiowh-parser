package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene2STS Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Jul 5, 2011
 */
public class Gene2STSParser {

    /**
     * This method load the data in gene2sts file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2STSTEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2STSTEMP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2STS);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2STS);
        }

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE2STSFILE + " file");

        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE2STSTEMP, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE2STSFILE,
                "  IGNORE 1 LINES"
                + " (GeneID,@UniSTSID)"
                + " set "
                + "UniSTSID=REPLACE(@UniSTSID,'-',NULL)");

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENE2STS
                + "(GeneInfo_WID,"
                + "UniSTSID) "
                + "select "
                + "g.WID,"
                + "a.UniSTSID "
                + "from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENE2STSTEMP
                + " a on a.GeneID = g.GeneID where a.UniSTSID is not NULL");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2STSTEMP);
    }
}
