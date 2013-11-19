package org.jbiowhparser.datasets.gene.genome.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.gene.genome.GenePTTTables;
import org.jbiowhpersistence.datasets.taxonomy.TaxonomyTables;

/**
 * This Class populates the GENEINFO_HAS_GENEPTT relationship table
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Mar 13, 2013
 */
public class GeneGenePTTLink {

    private static GeneGenePTTLink singleton;

    private GeneGenePTTLink() {
    }

    /**
     * Return a GeneGenePTTLink instance
     *
     * @return a GeneGenePTTLink instance
     */
    public static synchronized GeneGenePTTLink getInstance() {
        if (singleton == null) {
            singleton = new GeneGenePTTLink();
        }
        return singleton;
    }

    /**
     * Create the Gene-GenePTT relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + GeneTables.GENEINFO_HAS_GENEPTT);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENEINFO_HAS_GENEPTT);
        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + GeneTables.GENEINFO_HAS_GENERNT);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENEINFO_HAS_GENERNT);
        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + GenePTTTables.getInstance().GENEPTTTAXONOMY);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GenePTTTables.getInstance().GENEPTTTAXONOMY);
        
        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.GENEINFO_HAS_GENEPTT
                + "(GeneInfo_WID,GenePTT_ProteinGi) "
                + "select a.GeneInfo_WID,p.ProteinGI from "
                + GeneTables.getInstance().GENE2PROTEINACCESSION
                + " a inner join "
                + GenePTTTables.GENEPTT
                + " p on p.ProteinGi = a.ProteinGi group by a.GeneInfo_WID,p.ProteinGI");
        /*
        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.GENEINFO_HAS_GENERNT
                + "(GeneInfo_WID,GeneRNT_WID) "
                + "select a.GeneInfo_WID,p.WID from "
                + GeneTables.GENE2ACCESSION
                + " a inner join "
                + GenePTTTables.GENERNT
                + " p on p.GenomicNucleotideGi = a.GenomicNucleotideGi group by a.GeneInfo_WID,p.WID");
*/
        whdbmsFactory.executeUpdate("insert into "
                + GenePTTTables.getInstance().GENEPTTTAXONOMY
                + "(TaxId,Synonym) "
                + "select g.TaxId,s.Synonym from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.GENEINFO_HAS_GENEPTT
                + " a on g.WID = a.GeneInfo_WID inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t on t.TaxId = g.TaxId inner join "
                + TaxonomyTables.getInstance().TAXONOMYSYNONYM
                + " s on s.Taxonomy_WID = t.WID inner join "
                + TaxonomyTables.getInstance().TAXONOMYSYNONYMNAMECLASS
                + " n on n.WID = s.TaxonomySynonymNameClass_WID "
                + "where n.NameClass  = 'scientific name' group by g.TaxId");
    }
}
