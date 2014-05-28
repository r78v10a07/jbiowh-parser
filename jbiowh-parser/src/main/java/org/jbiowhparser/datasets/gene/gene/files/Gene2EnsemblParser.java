package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene2Ensembl Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since Jul 5, 2011
 */
public class Gene2EnsemblParser extends GeneCommon {

    /**
     * This method load the data in gene2ensembl file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        
        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2ENSEMBLTEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2ENSEMBLTEMP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2ENSEMBL);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2ENSEMBL);
        }
        
        checkOnlineData(GeneTables.getInstance().GENE2ENSEMBLFILE, ".gz");
        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE2ENSEMBLFILE + " file");
        
        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE2ENSEMBLTEMP, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE2ENSEMBLFILE,
                "  IGNORE 1 LINES "
                + "(@TaxID,GeneID,@EnsemblGeneIdentifier,@RNANucleotideAccession,@EnsemblRNAIdentifier,"
                + "@ProteinAccession,@EnsemblProteinIdentifier)"
                + " set "
                + "EnsemblGeneIdentifier=REPLACE(@EnsemblGeneIdentifier,'-',NULL),"
                + "RNANucleotideAccession=REPLACE(@RNANucleotideAccession,'-',NULL),"
                + "EnsemblRNAIdentifier=REPLACE(@EnsemblRNAIdentifier,'-',NULL),"
                + "ProteinAccession=REPLACE(@ProteinAccession,'-',NULL),"
                + "EnsemblProteinIdentifier=REPLACE(@EnsemblProteinIdentifier,'-',NULL)");
        
        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENE2ENSEMBL
                + "(GeneInfo_WID,"
                + "EnsemblGeneIdentifier,"
                + "RNANucleotideAccession,"
                + "EnsemblRNAIdentifier,"
                + "ProteinAccession,"
                + "EnsemblProteinIdentifier) "
                + "select "
                + "g.WID,"
                + "a.EnsemblGeneIdentifier,"
                + "a.RNANucleotideAccession,"
                + "a.EnsemblRNAIdentifier,"
                + "a.ProteinAccession,"
                + "a.EnsemblProteinIdentifier "
                + "from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENE2ENSEMBLTEMP
                + " a on a.GeneID = g.GeneID");
        
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2ENSEMBLTEMP);
        
        closeOnlineData(GeneTables.getInstance().GENE2ENSEMBLFILE);
    }
}
