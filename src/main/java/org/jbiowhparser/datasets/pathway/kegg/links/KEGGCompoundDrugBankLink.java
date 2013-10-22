package org.jbiowhparser.datasets.pathway.kegg.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.drug.drugbank.DrugBankTables;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This Class is the KEGGCompound DrugBank link
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
 * @since Apr 20, 2012
 */
public class KEGGCompoundDrugBankLink {

    private static KEGGCompoundDrugBankLink singleton;

    private KEGGCompoundDrugBankLink() {
    }

    /**
     * Return a KEGGCompoundDrugBankLink
     *
     * @return a KEGGCompoundDrugBankLink
     */
    public static synchronized KEGGCompoundDrugBankLink getInstance() {
        if (singleton == null) {
            singleton = new KEGGCompoundDrugBankLink();
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

        VerbLogger.getInstance().log(this.getClass(), "Creating table: " + KEGGTables.KEGGCOMPOUND_HAS_DRUGBANK);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.KEGGCOMPOUND_HAS_DRUGBANK);

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGCOMPOUND_HAS_DRUGBANK
                + "(KEGGCompound_WID,DrugBank_WID) "
                + " SELECT k.WID,d.Drugbank_WID FROM  "
                + DrugBankTables.getInstance().DRUGBANKEXTERNALIDENTIFIERS
                + " d inner join "
                + KEGGTables.getInstance().KEGGCOMPOUND
                + " k on k.Entry = d.Identifier where Resource = 'KEGG Compound' "
                + " group by d.Drugbank_WID,k.WID");
    }
}
