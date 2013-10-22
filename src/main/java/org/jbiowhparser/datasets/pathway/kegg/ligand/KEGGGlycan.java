package org.jbiowhparser.datasets.pathway.kegg.ligand;

import java.util.ArrayList;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserEntry;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This class is
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Nov 3, 2011
 */
public class KEGGGlycan extends KEGGParserEntry implements KEGGParserFactory {

    @Override
    public void format() {
        extractPatternFromField(ENTRY, "", "\\w*", " ", "", "");
        formatFieldSplit(NAME, ";");
        formatFieldReplace(COMMENT, "\n", " ");
        formatFieldReplace(COMPOSITION, "\n", " ");
        formatFieldSplit(CLASS, ";");
        formatFieldReplace(REACTION, "\n", " ");
        formatFieldSplit(REACTION, " ");
        formatFieldReplace(ENZYME, "\n", " ");
        formatFieldSplit(ENZYME, " ");
        formatFieldArrayListSplit(PATHWAY, "\n", "^[a-zA-Z]*\\d{5} .*", " ", null, null);
        formatFieldArrayListSplit(DBLINKS, "\n", ".*: .*", ": ", ":", "");
        formatFieldArrayListSplit(ORTHOLOGY, "\n", "^(K|k)\\d{5}.*", " ", null, null);
    }

    @Override
    public void printOnTSVFile() {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        printOnTSVFileGlycan(WID);
        printOnTSVFileArrayListValue(true, WID, NAME, KEGGTables.getInstance().KEGGGLYCANNAME);
        printOnTSVFileArrayListValue(false, WID, REACTION, KEGGTables.getInstance().KEGGGLYCANREACTION);
        printOnTSVFileArrayListValue(false, WID, ENZYME, KEGGTables.getInstance().KEGGGLYCANENZYME);
        printOnTSVFileArrayListValue(false, WID, PATHWAY, KEGGTables.getInstance().KEGGGLYCANPATHWAY);
        printOnTSVFileArrayListValue(false, WID, CLASS, KEGGTables.getInstance().KEGGGLYCANCLASSTEMP);
        printOnTSVFileArrayListValue(false, WID, ORTHOLOGY, KEGGTables.getInstance().KEGGGLYCANORTHOLOGY);
        printOnTSVFileArrayListValue(false, WID, DBLINKS, KEGGTables.getInstance().KEGGGLYCANDBLINK);
    }

    private void printOnTSVFileGlycan(long WID) {
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGLYCAN, WID, "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGLYCAN, ((String) ((ArrayList) getFieldValue().get(ENTRY)).get(0)), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGLYCAN, (String) getFieldValue().get(COMMENT), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGLYCAN, (String) getFieldValue().get(MASS), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGLYCAN, (String) getFieldValue().get(REMARK), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGLYCAN, (String) getFieldValue().get(COMPOSITION), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGLYCAN, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
    }

    @Override
    public String toString() {
        return "GLYCAN\t"
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
                + "\n\t" + COMPOSITION
                + "\n\t\t"
                + getFieldValue().get(COMPOSITION)
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
                + getFieldValue().get(DBLINKS)
                + "\n\t" + CLASS
                + "\n\t\t"
                + getFieldValue().get(CLASS)
                + "\n\t" + ORTHOLOGY
                + "\n\t\t"
                + getFieldValue().get(ORTHOLOGY);
    }
}
