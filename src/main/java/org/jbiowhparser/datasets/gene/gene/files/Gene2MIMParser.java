package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene2MIM Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Jul 5, 2011
 */
public class Gene2MIMParser {

    /**
     * This method load the data in gene2mim file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().MIM2GENE);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().MIM2GENE);
        }

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().MIM2GENE + " file");

        whdbmsFactory.loadTSVFile(GeneTables.getInstance().MIM2GENE, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().MIM2GENE,
                "  IGNORE 1 LINES");
    }
}
