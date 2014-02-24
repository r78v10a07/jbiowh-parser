package org.jbiowhparser.datasets.protclust.links;

import java.sql.SQLException;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.protclust.UniRefTables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class create the UniRef-Protein relationship tables
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-20 10:29:19 +0100
 * (Wed, 20 Mar 2013) $ $LastChangedRevision: 515 $
 *
 * @since Aug 19, 2011
 */
public class UniRefProteinLink {

    private static UniRefProteinLink singleton;

    private UniRefProteinLink() {
    }

    /**
     * Return a UniRefProteinLink
     *
     * @return a UniRefProteinLink
     */
    public static synchronized UniRefProteinLink getInstance() {
        if (singleton == null) {
            singleton = new UniRefProteinLink();
        }
        return singleton;
    }

    /**
     * Create the Protein-Ontology relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + UniRefTables.UNIREFENTRY_HAS_PROTEIN);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + UniRefTables.UNIREFMEMBER);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + UniRefTables.getInstance().UNIREFMEMBERTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + UniRefTables.getInstance().UNIREFMEMBERTEMP1);
        whdbmsFactory.disableKeys(UniRefTables.UNIREFENTRY_HAS_PROTEIN);
        whdbmsFactory.disableKeys(UniRefTables.UNIREFMEMBER);
        whdbmsFactory.disableKeys(UniRefTables.getInstance().UNIREFMEMBERTEMP);
        whdbmsFactory.disableKeys(UniRefTables.getInstance().UNIREFMEMBERTEMP1);

        whdbmsFactory.executeUpdate("insert into "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP1
                + " (WID,UniRefEntry_WID,Protein_WID,TaxId,Type,Id,IsRepresentative) "
                + " select * from "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP2
                + " group by UniRefEntry_WID,Id");

        whdbmsFactory.enableKeys(UniRefTables.getInstance().UNIREFMEMBERTEMP1);

        whdbmsFactory.executeUpdate("insert into "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP
                + " (WID,UniRefEntry_WID,Protein_WID,TaxId,Type,Id,IsRepresentative) "
                + " (select r.WID,r.UniRefEntry_WID,n.Protein_WID,r.TaxId,r.Type,r.Id,r.IsRepresentative from "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP1
                + " r inner join "
                + ProteinTables.getInstance().PROTEINNAME
                + " n on n.Name = r.Id"
                + " group by r.UniRefEntry_WID,n.Protein_WID)"
                + " UNION "
                + " (select r.WID,r.UniRefEntry_WID,n.Protein_WID,r.TaxId,r.Type,r.Id,r.IsRepresentative from "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP1
                + " r inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " n on n.AccessionNumber = r.Id"
                + " group by r.UniRefEntry_WID,n.Protein_WID)");

        whdbmsFactory.executeUpdate("insert into "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP
                + " (WID,UniRefEntry_WID,Protein_WID,TaxId,Type,Id,IsRepresentative) "
                + " select r.* from "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP1
                + " r left join "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP
                + " n on r.WID = n.WID where n.WID is null"
                + " group by r.UniRefEntry_WID,Id");

        whdbmsFactory.enableKeys(UniRefTables.getInstance().UNIREFMEMBERTEMP);

        whdbmsFactory.executeUpdate("insert IGNORE into "
                + UniRefTables.UNIREFMEMBER
                + " (WID,UniRefEntry_WID,Protein_WID,TaxId,Type,Id,IsRepresentative) "
                + " select * from "
                + UniRefTables.getInstance().UNIREFMEMBERTEMP
                + " group by UniRefEntry_WID,Protein_WID");

        whdbmsFactory.enableKeys(UniRefTables.UNIREFMEMBER);

        whdbmsFactory.executeUpdate("insert IGNORE into "
                + UniRefTables.UNIREFENTRY_HAS_PROTEIN
                + " (UniRefEntry_WID, Protein_WID) "
                + " select e.WID,m.Protein_WID from "
                + UniRefTables.getInstance().UNIREFENTRY
                + " e inner join "
                + UniRefTables.UNIREFMEMBER
                + " m on m.UniRefEntry_WID = e.WID");

        whdbmsFactory.enableKeys(UniRefTables.UNIREFENTRY_HAS_PROTEIN);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + UniRefTables.getInstance().UNIREFMEMBERTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + UniRefTables.getInstance().UNIREFMEMBERTEMP1);
    }
}
