package org.jbiowhparser.datasets.gene.gene.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.ontology.OntologyTables;

/**
 * This Class is the Gene Ontology link
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Aug 6, 2011
 */
public class GeneOntologyLink {

    private static GeneOntologyLink singleton;

    private GeneOntologyLink() {
    }

    /**
     * Return a GeneOntologyLink
     *
     * @return a GeneOntologyLink
     */
    public static synchronized GeneOntologyLink getInstance() {
        if (singleton == null) {
            singleton = new GeneOntologyLink();
        }
        return singleton;
    }

    /**
     * Create the Gene-Ontology relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + GeneTables.GENEINFO_HAS_ONTOLOGY);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENEINFO_HAS_ONTOLOGY);

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.GENEINFO_HAS_ONTOLOGY
                + "(GeneInfo_WID, Ontology_WID) "
                + "select g.GeneInfo_WID,o.WID from "
                + GeneTables.getInstance().GENE2GO
                + " g inner join "
                + OntologyTables.getInstance().ONTOLOGY
                + " o on g.GOID = o.Id");
    }
}
