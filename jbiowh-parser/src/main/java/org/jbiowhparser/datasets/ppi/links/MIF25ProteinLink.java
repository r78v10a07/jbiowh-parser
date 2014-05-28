package org.jbiowhparser.datasets.ppi.links;

import java.sql.SQLException;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class create the MIFInteraction_has_Protein relationship table
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-11-08 14:37:19 +0100
 * (Thu, 08 Nov 2012) $ $LastChangedRevision: 322 $
 *
 * @since Aug 20, 2011
 */
public class MIF25ProteinLink {

    private static MIF25ProteinLink singleton;

    private MIF25ProteinLink() {
    }

    /**
     * Return a MIF25ProteinLink
     *
     * @return a MIF25ProteinLink
     */
    public static synchronized MIF25ProteinLink getInstance() {
        if (singleton == null) {
            singleton = new MIF25ProteinLink();
        }
        return singleton;
    }

    /**
     * Create the MIF25-Protein relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + MIF25Tables.MIFINTERACTION_HAS_PROTEIN);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + MIF25Tables.getInstance().MIFINTERACTION_HAS_PROTEIN_TEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + MIF25Tables.getInstance().MIFINTERACTIONCOUNT);
        whdbmsFactory.disableKeys(MIF25Tables.MIFINTERACTION_HAS_PROTEIN);
        whdbmsFactory.disableKeys(MIF25Tables.getInstance().MIFINTERACTION_HAS_PROTEIN_TEMP);
        whdbmsFactory.disableKeys(MIF25Tables.getInstance().MIFINTERACTIONCOUNT);

        whdbmsFactory.executeUpdate("insert into "
                + MIF25Tables.getInstance().MIFINTERACTION_HAS_PROTEIN_TEMP
                + " (MIFEntryInteraction_WID,Protein_WID)"
                + " (select i.WID,pa.Protein_WID from "
                + MIF25Tables.getInstance().MIFENTRYINTERACTION
                + " i inner join "
                + MIF25Tables.getInstance().MIFINTERACTIONPARTICIPANT
                + " p on p.MIFEntryInteraction_WID = i.WID inner join "
                + MIF25Tables.getInstance().MIFENTRYINTERACTOR
                + " mi on i.MIFEntrySetEntry_WID = mi.MIFEntrySetEntry_WID and "
                + " p.InteractorRef = mi.Id inner join "
                + MIF25Tables.getInstance().MIFOTHERXREFUNIPROT
                + " u on u.MIFOtherWID = mi.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = u.Id "
                + " where u.RefType = 'identity')");

        whdbmsFactory.enableKeys(MIF25Tables.getInstance().MIFINTERACTION_HAS_PROTEIN_TEMP);

        whdbmsFactory.executeUpdate("insert into "
                + MIF25Tables.MIFINTERACTION_HAS_PROTEIN
                + " (MIFEntryInteraction_WID,Protein_WID)"
                + " select MIFEntryInteraction_WID,Protein_WID from "
                + MIF25Tables.getInstance().MIFINTERACTION_HAS_PROTEIN_TEMP
                + " group by MIFEntryInteraction_WID,Protein_WID");

        whdbmsFactory.enableKeys(MIF25Tables.MIFINTERACTION_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + MIF25Tables.getInstance().MIFINTERACTION_HAS_PROTEIN_TEMP);

        whdbmsFactory.executeUpdate("insert into "
                + MIF25Tables.getInstance().MIFINTERACTIONCOUNT
                + " (MIFEntryInteraction_WID,Count)"
                + " select MIFEntryInteraction_WID,count(MIFEntryInteraction_WID) from "
                + MIF25Tables.MIFINTERACTION_HAS_PROTEIN
                + " group by MIFEntryInteraction_WID");

        whdbmsFactory.enableKeys(MIF25Tables.getInstance().MIFINTERACTIONCOUNT);
    }
}
