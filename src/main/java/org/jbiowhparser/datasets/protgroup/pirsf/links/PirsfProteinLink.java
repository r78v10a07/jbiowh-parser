package org.jbiowhparser.datasets.protgroup.pirsf.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.jbiowhpersistence.datasets.protgroup.pirsf.PirsfTables;

/**
 * This Class is the
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 11, 2013
 */
public class PirsfProteinLink {

    private static PirsfProteinLink singleton;

    private PirsfProteinLink() {
    }

    /**
     * Return a PirsfProteinLink instance
     *
     * @return a PirsfProteinLink instance
     */
    public static synchronized PirsfProteinLink getInstance() {
        if (singleton == null) {
            singleton = new PirsfProteinLink();
        }
        return singleton;
    }

    /**
     * Create the PIRSF-Protein relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + PirsfTables.PIRSF_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + PirsfTables.PIRSF_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("insert IGNORE into "
                + PirsfTables.PIRSF_HAS_PROTEIN
                + " (PIRSF_WID,Protein_WID,Status,Seed) "
                + " select f.WID,a.Protein_WID,fp.Status,fp.Seed from "
                + PirsfTables.getInstance().PIRSF
                + " f inner join "
                + PirsfTables.PIRSFPROTEIN
                + " fp on fp.PIRSF_WID = f.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " a on a.AccessionNUmber = fp.AccessionNumber");
    }
}
