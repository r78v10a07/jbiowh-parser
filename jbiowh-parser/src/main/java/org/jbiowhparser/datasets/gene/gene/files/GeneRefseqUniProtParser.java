package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is GeneRefseqUniProt Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Jul 5, 2011
 */
public class GeneRefseqUniProtParser {

    /**
     * This method load the data in gene_refseq_uniprotkb_collab file to its
     * relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE_REFSEQ_UNIPROTKB_COLLAB);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE_REFSEQ_UNIPROTKB_COLLAB);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENEREFSEQUNIPROT);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEREFSEQUNIPROT);
        }

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE_REFSEQ_UNIPROTKB_COLLAB + " file");

        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE_REFSEQ_UNIPROTKB_COLLAB, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE_REFSEQ_UNIPROTKB_COLLAB,
                "  IGNORE 1 LINES");

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENEREFSEQUNIPROT
                + "(ProteinAccession,"
                + "UniProtKBProteinAccession,"
                + "DataSetWID) "
                + "select "
                + "ProteinAccession,"
                + "UniProtKBProteinAccession,"
                + DataSetPersistence.getInstance().getDataset().getWid()
                + " from "
                + GeneTables.getInstance().GENE_REFSEQ_UNIPROTKB_COLLAB
                + " group by ProteinAccession,UniProtKBProteinAccession");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE_REFSEQ_UNIPROTKB_COLLAB);
    }
}
