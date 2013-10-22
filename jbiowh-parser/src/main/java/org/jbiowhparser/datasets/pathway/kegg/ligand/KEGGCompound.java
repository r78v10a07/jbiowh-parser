package org.jbiowhparser.datasets.pathway.kegg.ligand;

import java.util.ArrayList;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserEntry;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This class is KEGG Compound parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 30, 2011
 */
public class KEGGCompound extends KEGGParserEntry implements KEGGParserFactory {

    /**
     * Format the Compound entry parsing its fields
     */
    @Override
    public void format() {
        extractPatternFromField(ENTRY, "", "\\w*", " ", "", "");
        formatFieldSplit(NAME, ";");
        formatFieldReplace(COMMENT, "\n", " ");
        formatFieldReplace(FORMULA, "\n", " ");
        formatFieldReplace(REACTION, "\n", " ");
        formatFieldSplit(REACTION, " ");
        formatFieldReplace(ENZYME, "\n", " ");
        formatFieldSplit(ENZYME, " ");
        formatFieldArrayListSplit(PATHWAY, "\n", "^[a-zA-Z]*\\d{5} .*", " ", null, null);
        formatFieldArrayListSplit(DBLINKS, "\n", ".*: .*", ": ", ":", "");
    }

    /**
     * Print the Compound entry on the TSV file set
     */
    @Override
    public void printOnTSVFile() {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        printOnTSVFileCompound(WID);
        printOnTSVFileArrayListValue(true, WID, NAME, KEGGTables.getInstance().KEGGCOMPOUNDNAME);
        printOnTSVFileArrayListValue(false, WID, REACTION, KEGGTables.getInstance().KEGGCOMPOUNDREACTION);
        printOnTSVFileArrayListValue(false, WID, ENZYME, KEGGTables.getInstance().KEGGCOMPOUNDENZYME);
        printOnTSVFileArrayListValue(false, WID, PATHWAY, KEGGTables.getInstance().KEGGCOMPOUNDPATHWAY);
        printOnTSVFileArrayListValue(false, WID, DBLINKS, KEGGTables.getInstance().KEGGCOMPOUNDDBLINK);
    }

    private void printOnTSVFileCompound(long WID) {
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGCOMPOUND, WID, "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGCOMPOUND, ((String) ((ArrayList) getFieldValue().get(ENTRY)).get(0)), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGCOMPOUND, (String) getFieldValue().get(COMMENT), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGCOMPOUND, (String) getFieldValue().get(MASS), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGCOMPOUND, (String) getFieldValue().get(REMARK), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGCOMPOUND, (String) getFieldValue().get(FORMULA), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGCOMPOUND, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
    }

    @Override
    public String toString() {
        return "COMPOUND\t"
                + getFieldValue().get(ENTRY)
                + "\n\t" + NAME
                + "\n\t\t"
                + getFieldValue().get(NAME)
                + "\n\t" + COMMENT
                + "\n\t\t"
                + getFieldValue().get(COMMENT)
                + "\n\t" + MASS
                + "\n\t\t"
                + getFieldValue().get(MASS)
                + "\n\t" + REMARK
                + "\n\t\t"
                + getFieldValue().get(REMARK)
                + "\n\t" + REACTION
                + "\n\t\t"
                + getFieldValue().get(REACTION)
                + "\n\t" + ENZYME
                + "\n\t\t"
                + getFieldValue().get(ENZYME)
                + "\n\t" + PATHWAY
                + "\n\t\t"
                + getFieldValue().get(PATHWAY)
                + "\n\t" + DBLINKS
                + "\n\t\t"
                + getFieldValue().get(DBLINKS);
    }
}
