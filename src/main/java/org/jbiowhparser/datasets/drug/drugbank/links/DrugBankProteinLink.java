package org.jbiowhparser.datasets.drug.drugbank.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
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
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_DRUGBANK);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_DRUGBANK);
        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_DRUGBANK, false);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_DRUGBANK
                + " (Protein_WID,DrugBank_WID)"
                + " SELECT pa.Protein_WID,t.DrugBank_WID FROM "
                + DrugBankTables.getInstance().DRUGBANKTARGETS
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANKPARTNERS
                + " p on p.Id = t.Partner inner join "
                + DrugBankTables.getInstance().DRUGBANKPARTNEREXTERNALIDENTIFIERS
                + " e on e.DrugBankPartners_WID = p.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = e.Identifier where e.Resource = 'UniProtKB'"
                + " group by pa.Protein_WID,t.DrugBank_WID");

        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_DRUGBANK, true);

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME);
        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME, false);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME
                + " (Protein_WID,DrugBank_WID)"
                + " SELECT pa.Protein_WID,t.DrugBank_WID FROM "
                + DrugBankTables.getInstance().DRUGBANKENZYMES
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANKPARTNERS
                + " p on p.Id = t.Partner inner join "
                + DrugBankTables.getInstance().DRUGBANKPARTNEREXTERNALIDENTIFIERS
                + " e on e.DrugBankPartners_WID = p.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = e.Identifier where e.Resource = 'UniProtKB'"
                + " group by pa.Protein_WID,t.DrugBank_WID");

        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_DRUGBANKASENZYME, true);

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS);
        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS, false);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS
                + " (Protein_WID,DrugBank_WID)"
                + " SELECT pa.Protein_WID,t.DrugBank_WID FROM "
                + DrugBankTables.getInstance().DRUGBANKTRANSPORTERS
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANKPARTNERS
                + " p on p.Id = t.Partner inner join "
                + DrugBankTables.getInstance().DRUGBANKPARTNEREXTERNALIDENTIFIERS
                + " e on e.DrugBankPartners_WID = p.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = e.Identifier where e.Resource = 'UniProtKB'"
                + " group by pa.Protein_WID,t.DrugBank_WID");

        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_DRUGBANKASTRANSPORTERS, true);

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS);
        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS, false);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS
                + " (Protein_WID,DrugBank_WID)"
                + " SELECT pa.Protein_WID,t.DrugBank_WID FROM "
                + DrugBankTables.getInstance().DRUGBANKCARRIERS
                + " t inner join "
                + DrugBankTables.getInstance().DRUGBANKPARTNERS
                + " p on p.Id = t.Partner inner join "
                + DrugBankTables.getInstance().DRUGBANKPARTNEREXTERNALIDENTIFIERS
                + " e on e.DrugBankPartners_WID = p.WID inner join "
                + ProteinTables.getInstance().PROTEINACCESSIONNUMBER
                + " pa on pa.AccessionNumber = e.Identifier where e.Resource = 'UniProtKB'"
                + " group by pa.Protein_WID,t.DrugBank_WID");

        whdbmsFactory.indexManagement(ProteinTables.PROTEIN_HAS_DRUGBANKASCARRIERS, true);
    }
}
