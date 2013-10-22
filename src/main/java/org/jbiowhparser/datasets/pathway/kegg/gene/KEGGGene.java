package org.jbiowhparser.datasets.pathway.kegg.gene;

import java.util.ArrayList;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserEntry;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This class is the KEGG Gene parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Nov 10, 2011
 */
public class KEGGGene extends KEGGParserEntry implements KEGGParserFactory {

    @Override
    public void format() {
        extractPatternFromField(ENTRY, "", "\\w*", " ", "", "");
        formatFieldReplace(NAME, "\n", " ");
        formatFieldSplit(NAME, ",");
        formatFieldArrayListSplit(ORTHOLOGY, "\n", "^(K|k)\\d{5}.*", " ", null, null);
        formatFieldArrayListSplit(DBLINKS, "\n", ".*: .*", ": ", ":", "");
        formatFieldArrayListSplit(MOTIF, "\n", ".*: .*", ": ", ":", "");
        formatFieldArrayListSplit(STRUCTURE, "\n", ".*: .*", ": ", ":", "");
        formatFieldArrayListSplit(PATHWAY, "\n", "^[a-zA-Z]*\\d{5} .*", " ", null, null);
        formatFieldArrayListSplit(DISEASE, "\n", "^[a-zA-Z]*\\d{5} .*", " ", null, null);
        formatFieldArrayListSplit(DRUG_TARGET, "\n", ".*: .*", ": ", ":", "");
        extractPatternFromField(POSITION, "", "\\d*", "", "", "");
    }

    @Override
    public void printOnTSVFile() {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        printOnTSVFileGene(WID);
        printOnTSVFileArrayListValue(true, WID, NAME, KEGGTables.getInstance().KEGGGENENAME);
        printOnTSVFileArrayListValue(false, WID, ORTHOLOGY, KEGGTables.getInstance().KEGGGENEORTHOLOGY);
        printOnTSVFileArrayListValue(false, WID, DBLINKS, KEGGTables.getInstance().KEGGGENEDBLINK);
        printOnTSVFileArrayListValue(false, WID, MOTIF, KEGGTables.getInstance().KEGGGENEDBLINK);
        printOnTSVFileArrayListValue(false, WID, STRUCTURE, KEGGTables.getInstance().KEGGGENEDBLINK);
        printOnTSVFileArrayListValue(false, WID, PATHWAY, KEGGTables.getInstance().KEGGGENEPATHWAY);
        printOnTSVFileArrayListValue(true, WID, DISEASE, KEGGTables.getInstance().KEGGGENEDISEASE);
        printOnTSVFileArrayListValue(false, WID, DRUG_TARGET, KEGGTables.getInstance().KEGGGENEDRUGTARGET);
    }

    private void printOnTSVFileGene(long WID) {
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGENE, WID, "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGENE, ((String) ((ArrayList) getFieldValue().get(ENTRY)).get(0)), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGENE, (String) getFieldValue().get(DEFINITION), "\t");
        int count = 0;
        if (getFieldValue().get(POSITION) != null) {
            for (String s : ((ArrayList<String>) getFieldValue().get(POSITION))) {
                if (count > 2) {
                    break;
                }
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGENE, s, "\t");
                count++;
            }
        } else {
            ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGENE, (String) null, "\t");
            ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGENE, (String) null, "\t");
        }
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGGENE, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
    }

    @Override
    public String toString() {
        return "GENE\t"
                + ((String) ((ArrayList) getFieldValue().get(ENTRY)).get(0))
                + "\n\t" + NAME
                + "\n\t\t"
                + getFieldValue().get(NAME)
                + "\n\t" + DEFINITION
                + "\n\t\t"
                + getFieldValue().get(DEFINITION)
                + "\n\t" + POSITION
                + "\n\t\t"
                + getFieldValue().get(POSITION)
                + "\n\t" + ORTHOLOGY
                + "\n\t\t"
                + getFieldValue().get(ORTHOLOGY);
    }
}
