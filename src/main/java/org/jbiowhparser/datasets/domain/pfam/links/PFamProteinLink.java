package org.jbiowhparser.datasets.domain.pfam.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.domain.pfam.PFamTables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class is the PFam-Protein external link
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Nov 19, 2012
 */
public class PFamProteinLink {

    private static PFamProteinLink singleton;

    private PFamProteinLink() {
    }

    /**
     * Return a PFamProteinLink instance
     *
     * @return a PFamProteinLink instance
     */
    public static synchronized PFamProteinLink getInstance() {
        if (singleton == null) {
            singleton = new PFamProteinLink();
        }
        return singleton;
    }

    /**
     * Create the PFAM-Protein relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + PFamTables.PFAMSEQ_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + PFamTables.PFAMSEQ_HAS_PROTEIN);
        whdbmsFactory.disableKeys(PFamTables.PFAMSEQ_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("insert into "
                + PFamTables.PFAMSEQ_HAS_PROTEIN
                + "(auto_pfamseq,Protein_WID) "
                + " select u.auto_pfamseq,n.Protein_WID from "
                + PFamTables.getInstance().PFAMSEQ_HAS_UNIPROTID
                + " u inner join "
                + ProteinTables.getInstance().PROTEINNAME
                + "  n on n.Name = u.UniProt_Id ");

        whdbmsFactory.enableKeys(PFamTables.PFAMSEQ_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("UPDATE "
                + PFamTables.PFAMAREGFULLSIGNIFICANT
                + " s INNER JOIN "
                + PFamTables.PFAMSEQ_HAS_PROTEIN
                + "  p on s.auto_pfamseq = p.auto_pfamseq SET s.Protein_WID = p.Protein_WID");

        whdbmsFactory.executeUpdate("UPDATE "
                + PFamTables.PFAMAREGFULLINSIGNIFICANT
                + " s INNER JOIN "
                + PFamTables.PFAMSEQ_HAS_PROTEIN
                + "  p on s.auto_pfamseq = p.auto_pfamseq SET s.Protein_WID = p.Protein_WID");

    }
}
