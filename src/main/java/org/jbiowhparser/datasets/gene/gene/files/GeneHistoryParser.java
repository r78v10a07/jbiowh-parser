package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the GeneHistory Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since Jul 5, 2011
 */
public class GeneHistoryParser extends GeneCommon {

    /**
     * This method load the data in gene_history file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        
        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENEHISTORY);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEHISTORY);
        }
        
        checkOnlineData(GeneTables.getInstance().GENE_HISTORY, ".gz");
        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE_HISTORY + " file");
        
        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENEHISTORY, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE_HISTORY,
                "  IGNORE 1 LINES"
                + " (@TaxID,@GeneID,@DiscontinuedGeneID,@DiscontinuedSymbol,@DiscontinueDate) "
                + " set "
                + "TaxID=REPLACE(@TaxID,'-',NULL),"
                + "GeneID=REPLACE(@GeneID,'-',NULL),"
                + "DiscontinuedGeneID=REPLACE(@DiscontinuedGeneID,'-',NULL),"
                + "DiscontinuedSymbol=REPLACE(@DiscontinuedSymbol,'-',NULL),"
                + "DiscontinueDate=REPLACE(@DiscontinueDate,'-',NULL),"
                + "DataSetWID=" + DataSetPersistence.getInstance().getDataset().getWid());
        closeOnlineData(GeneTables.getInstance().GENE_HISTORY);
    }
}
