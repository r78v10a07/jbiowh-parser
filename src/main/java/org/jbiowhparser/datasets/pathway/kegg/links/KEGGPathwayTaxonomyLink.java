package org.jbiowhparser.datasets.pathway.kegg.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;
import org.jbiowhpersistence.datasets.taxonomy.TaxonomyTables;

/**
 * This class is the KEGGPathway Taxonomy link
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Dec 13, 2011
 */
public class KEGGPathwayTaxonomyLink {

    private static KEGGPathwayTaxonomyLink singleton;

    private KEGGPathwayTaxonomyLink() {
    }

    /**
     * Return a KEGGPathwayTaxonomyLink
     *
     * @return a KEGGPathwayTaxonomyLink
     */
    public static synchronized KEGGPathwayTaxonomyLink getInstance() {
        if (singleton == null) {
            singleton = new KEGGPathwayTaxonomyLink();
        }
        return singleton;
    }

    /**
     * Create the KEGGPathway-Taxonomy relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + KEGGTables.KEGGPATHWAY_HAS_TAXONOMY);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.KEGGPATHWAY_HAS_TAXONOMY);

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGPATHWAY_HAS_TAXONOMY
                + "(KEGGPathway_WID,Taxonomy_WID) "
                + " SELECT p.KEGGPathway_WID,t.WID FROM  "
                + KEGGTables.KEGGGENE_HAS_KEGGPATHWAY
                + " p inner join "
                + KEGGTables.getInstance().KEGGGENEDBLINK
                + " k on k.KEGGGene_WID = p.KEGGGene_WID inner join "
                + GeneTables.getInstance().GENEINFO
                + " g on g.GeneId = k.Id inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t on g.TaxId = t.TaxId"
                + " where k.DB = 'NCBI-GeneID'"
                + " group by p.KEGGPathway_WID,t.WID");
    }
}
