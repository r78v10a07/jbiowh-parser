package org.jbiowhparser.datasets.domain.pfam.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.domain.pfam.PFamTables;
import org.jbiowhpersistence.datasets.ontology.OntologyTables;

/**
 * This Class is the PFAM-Ontology external links
 *
 * $Author: r78v10a07@gmail.com $ 
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $ 
 * $LastChangedRevision: 396 $
 * @since Nov 16, 2012
 */
public class PFamOntologyLink {

    private static PFamOntologyLink singleton;

    private PFamOntologyLink() {
    }

    /**
     * Return a PFamOntologyLink instance
     *
     * @return a PFamOntologyLink instance
     */
    public static synchronized PFamOntologyLink getInstance() {
        if (singleton == null) {
            singleton = new PFamOntologyLink();
        }
        return singleton;
    }

    /**
     * Create the PFAM-Ontology relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + PFamTables.PFAMA_HAS_ONTOLOGY);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + PFamTables.PFAMA_HAS_ONTOLOGY);

        whdbmsFactory.executeUpdate("insert into "
                + PFamTables.PFAMA_HAS_ONTOLOGY
                + "(PfamA_WID,Ontology_WID) "
                + "select p.WID,l.WID from "
                + PFamTables.getInstance().GENE_ONTOLOGY
                + " o inner join "
                + PFamTables.getInstance().PFAMA_ORIG
                + " a on a.auto_pfamA = o.auto_pfamA inner join "
                + OntologyTables.getInstance().ONTOLOGY
                + " l on l.Id = o.go_id inner join "
                + PFamTables.getInstance().PFAMABIOWH
                + " p ON p.pfamA_acc = a.pfamA_acc");
    }
}
