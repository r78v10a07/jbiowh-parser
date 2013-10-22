package org.jbiowhparser.datasets.drug.drugbank.links;

import java.sql.SQLException;
import org.jbiowhparser.datasets.pathway.kegg.links.KEGGCompoundDrugBankLink;

/**
 * This class is create all DrugBank external relationship tables
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
 * @since Sep 15, 2011
 */
public class DrugBankLinks {

    /**
     * Create all DrugBank external relationship tables
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {

        /*
         * Create the Protein-DrugBank realtionship table
         */
        DrugBankProteinLink.getInstance().runLink();
        
        /*
         * Create the KEGGCompund-DrugBank relationship table
         */
        KEGGCompoundDrugBankLink.getInstance().runLink();
    }
}
