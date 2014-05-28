package org.jbiowhparser.datasets.gene.genome.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.gene.genome.GenePTTTables;
import org.jbiowhpersistence.datasets.taxonomy.TaxonomyTables;

/**
 * This Class is populates the GENEPTT_HAS_TAXONOMY relationship table 
 *
 * $Author$
 * $LastChangedDate$
 * $LastChangedRevision$
 * @since Mar 15, 2013
 */
public class GenePTTTaxonomyLink {

    private static GenePTTTaxonomyLink singleton;

    private GenePTTTaxonomyLink() {
    }

    /**
     * Return a GenePTTTaxonomyLink instance
     *
     * @return a GenePTTTaxonomyLink instance
     */
    public static synchronized GenePTTTaxonomyLink getInstance() {
        if (singleton == null) {
            singleton = new GenePTTTaxonomyLink();
        }
        return singleton;
    }
    
    /**
     * Create the GenePTT-Taxonomy relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + GenePTTTables.GENEPTT_HAS_TAXONOMY);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GenePTTTables.GENEPTT_HAS_TAXONOMY);
        
        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + GenePTTTables.GENERNT_HAS_TAXONOMY);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GenePTTTables.GENERNT_HAS_TAXONOMY);
        
        whdbmsFactory.executeUpdate("insert into "
                + GenePTTTables.GENEPTT_HAS_TAXONOMY
                + "(GenePTT_ProteinGi,Taxonomy_WID) "
                + "select gptt.GenePTT_ProteinGi,t.WID from "
                + GeneTables.GENEINFO_HAS_GENEPTT
                + " gptt inner join  "
                + GeneTables.getInstance().GENEINFO
                + " g on g.WID = gptt.GeneInfo_WID inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t on t.TaxId = g.TaxId group by gptt.GenePTT_ProteinGi,t.WID");
        
        whdbmsFactory.executeUpdate("insert into "
                + GenePTTTables.GENERNT_HAS_TAXONOMY
                + "(GeneRNT_WID,Taxonomy_WID) "
                + "select gptt.GeneRNT_WID,t.WID from "
                + GeneTables.GENEINFO_HAS_GENERNT
                + " gptt inner join  "
                + GeneTables.getInstance().GENEINFO
                + " g on g.WID = gptt.GeneInfo_WID inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t on t.TaxId = g.TaxId group by gptt.GeneRNT_WID,t.WID");
    }
 }
