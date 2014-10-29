package org.jbiowhparser.datasets.drug.drugbank.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.drug.drugbank.DrugBankTables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class populates the PROTEIN_HAS_DRUGBANK relationship table
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Mar 13, 2013
 */
public class DrugBankProteinLink {

    private static DrugBankProteinLink singleton;

    private DrugBankProteinLink() {
    }

    /**
     * Return a DrugBankProteinLink instance
     *
     * @return a DrugBankProteinLink instance
     */
    public static synchronized DrugBankProteinLink getInstance() {
        if (singleton == null) {
            singleton = new DrugBankProteinLink();
        }
        return singleton;
    }

    /**
     * Create the Protein-DrugBank relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_DRUGBANK);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_DRUGBANK);
        whdbmsFactory.disableKeys(ProteinTables.PROTEIN_HAS_DRUGBANK);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_DRUGBANK
                + " (Protein_WID,DrugBank_WID)"
                + " (SELECT pa.Protein_WID,t.DrugBank_WID FROM "
                + DrugBankTables.getInstance().DRUGBANKTARGETS
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANKTARGETSPOLYPEPTIDE
                + " p on p.DrugBankTarget_WID = t.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = p.Id "
                + " group by pa.Protein_WID,t.DrugBank_WID) "
                + " UNION "
                + " (SELECT p.Protein_WID,d.WID FROM "
                + ProteinTables.getInstance().PROTEINDRUGBANK
                + "  p inner join "
                + DrugBankTables.getInstance().DRUGBANK
                + "  d on d.Id = p.Id group by p.Protein_WID,d.WID)");

        whdbmsFactory.enableKeys(ProteinTables.PROTEIN_HAS_DRUGBANK);

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME);
        whdbmsFactory.disableKeys(ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME
                + " (Protein_WID,DrugBank_WID)"
                + " SELECT pa.Protein_WID,t.DrugBank_WID FROM "
                + DrugBankTables.getInstance().DRUGBANKENZYMES
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANKENZYMESPOLYPEPTIDE
                + " p on p.DrugBankEnzyme_WID = t.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = p.Id"
                + " group by pa.Protein_WID,t.DrugBank_WID");

        whdbmsFactory.enableKeys(ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME);

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS);
        whdbmsFactory.disableKeys(ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS
                + " (Protein_WID,DrugBank_WID)"
                + " SELECT pa.Protein_WID,t.DrugBank_WID FROM "
                + DrugBankTables.getInstance().DRUGBANKTRANSPORTERS
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANKTRANSPORTERSPOLYPEPTIDE
                + " p on p.DrugBankTransporter_WID = t.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = p.Id"
                + " group by pa.Protein_WID,t.DrugBank_WID");

        whdbmsFactory.enableKeys(ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS);

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS);
        whdbmsFactory.disableKeys(ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS
                + " (Protein_WID,DrugBank_WID)"
                + " SELECT pa.Protein_WID,t.DrugBank_WID FROM "
                + DrugBankTables.getInstance().DRUGBANKCARRIERS
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANKCARRIERSPOLYPEPTIDE
                + " p on p.DrugBankCarrier_WID = t.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = p.Id"
                + " group by pa.Protein_WID,t.DrugBank_WID");

        whdbmsFactory.enableKeys(ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS);
        
    }
}
