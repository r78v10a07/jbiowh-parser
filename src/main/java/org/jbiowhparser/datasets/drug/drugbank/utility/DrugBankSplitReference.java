package org.jbiowhparser.datasets.drug.drugbank.utility;

import org.jbiowhcore.utility.utils.ParseFiles;

/**
 * This class split the DrugBank reference and print it on the relational table
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 9, 2011
 */
public class DrugBankSplitReference {

    /**
     * This method split the DrugBank reference and print it on the relational
     * table
     *
     * @param reference String object with the original list of references
     * @param refWID Warehouse Id to print into the relational table
     * @param tableName the relational table name
     */
    public void split(String reference, long refWID, String tableName) {
        if (reference != null) {
            reference = reference.trim().replace("\n", "");
            String[] ref = reference.split("#");
            for (String ref1 : ref) {
                if (!ref1.isEmpty()) {
                    String[] split = ref1.trim().split("http:");
                    if (split.length == 1) {
                        ParseFiles.getInstance().printOnTSVFile(tableName, refWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(tableName, split[0].trim(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(tableName, "\\N", "\n");
                    } else if (split.length == 2) {
                        ParseFiles.getInstance().printOnTSVFile(tableName, refWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(tableName, split[0].trim(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(tableName, "http:" + split[1].trim(), "\n");
                    } else {
                        ParseFiles.getInstance().printOnTSVFile(tableName, refWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(tableName, reference.trim(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(tableName, "\\N", "\n");
                    }
                }
            }
        }
    }
}
