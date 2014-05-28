package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene2GO Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since Jul 5, 2011
 */
public class Gene2GOParser extends GeneCommon {

    /**
     * This method load the data in gene2go file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        
        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2GOTEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2GOTEMP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2GO);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2GO);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.GENEINFO_HAS_ONTOLOGY);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENEINFO_HAS_ONTOLOGY);
        }
        
        checkOnlineData(GeneTables.getInstance().GENE2GOFILE, ".gz");
        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE2GOFILE + " file");
        
        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE2GOTEMP, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE2GOFILE,
                "  IGNORE 1 LINES "
                + "(@TaxID,GeneID,@GOID,@Evidence,@Qualifier,@GOTerm,@PubMed,@Category) "
                + " set "
                + "GOID=REPLACE(@GOID,'-',NULL)");
        
        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENE2GO
                + "(GeneInfo_WID,"
                + "GOID) "
                + "select "
                + "g.WID,"
                + "a.GOID "
                + "from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENE2GOTEMP
                + " a on a.GeneID = g.GeneID where a.GOID is not NULL"
                + " group by g.WID,a.GOID");
        
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2GOTEMP);
        
        closeOnlineData(GeneTables.getInstance().GENE2GOFILE);
    }
}
