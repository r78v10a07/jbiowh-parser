package org.jbiowhparser.datasets.domain.pfam.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.domain.pfam.PFamTables;
import org.jbiowhpersistence.datasets.taxonomy.TaxonomyTables;

/**
 * This Class is the PFAM-Taxonomy external link
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Nov 16, 2012
 */
public class PFamTaxonomyLink {

    private static PFamTaxonomyLink singleton;

    private PFamTaxonomyLink() {
    }

    /**
     * Return a PFamTaxonomyLink instance
     *
     * @return a PFamTaxonomyLink instance
     */
    public static synchronized PFamTaxonomyLink getInstance() {
        if (singleton == null) {
            singleton = new PFamTaxonomyLink();
        }
        return singleton;
    }

    /**
     * Create the PFAM-Taxonomy relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + PFamTables.PFAMA_HAS_TAXONOMY);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + PFamTables.PFAMA_HAS_TAXONOMY);

        whdbmsFactory.executeUpdate("insert into "
                + PFamTables.PFAMA_HAS_TAXONOMY
                + "(PfamA_WID,Taxonomy_WID) "
                + "select p.WID,l.WID from "
                + PFamTables.getInstance().PFAMA_NCBI
                + " o inner join "
                + PFamTables.getInstance().PFAMA_ORIG
                + " a on a.auto_pfamA = o.auto_pfamA inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " l on l.TaxId = o.ncbi_taxid inner join "
                + PFamTables.getInstance().PFAMABIOWH
                + " p ON p.pfamA_acc = a.pfamA_acc");
    }
}
