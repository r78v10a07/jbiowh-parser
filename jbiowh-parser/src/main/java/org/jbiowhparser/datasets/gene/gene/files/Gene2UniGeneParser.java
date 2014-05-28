package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene2UniGene Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since Jul 5, 2011
 */
public class Gene2UniGeneParser extends GeneCommon {

    /**
     * This method load the data in gene2unigene file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        
        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2UNIGENETEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2UNIGENETEMP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2UNIGENE);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2UNIGENE);
        }
        
        checkOnlineData(GeneTables.getInstance().GENE2UNIGENEFILE, "");
        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE2UNIGENEFILE + " file");
        
        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE2UNIGENETEMP, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE2UNIGENEFILE,
                "  IGNORE 1 LINES "
                + " (GeneID,@UniGene)"
                + " set "
                + "UniGene=REPLACE(@UniGene,'-',NULL)");
        
        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENE2UNIGENE
                + "(GeneInfo_WID,"
                + "UniGene) "
                + "select "
                + "g.WID,"
                + "a.UniGene "
                + "from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENE2UNIGENETEMP
                + " a on a.GeneID = g.GeneID where a.UniGene is not NULL");
        
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2UNIGENETEMP);
        closeOnlineData(GeneTables.getInstance().GENE2UNIGENEFILE);
    }
}
