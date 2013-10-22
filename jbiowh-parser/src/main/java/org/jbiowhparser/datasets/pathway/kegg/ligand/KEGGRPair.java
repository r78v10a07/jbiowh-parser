package org.jbiowhparser.datasets.pathway.kegg.ligand;

import java.util.ArrayList;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserEntry;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This class is is KEGG Rpair parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Nov 10, 2011
 */
public class KEGGRPair extends KEGGParserEntry implements KEGGParserFactory {

    /**
     * Format the Rpair entry parsing its fields
     */
    @Override
    public void format() {
        extractPatternFromField(ENTRY, "", "\\w*", " ", "", "");
        formatFieldReplace(REACTION, "\n", " ");
        formatFieldSplit(REACTION, " ");
        formatFieldReplace(ENZYME, "\n", " ");
        formatFieldSplit(ENZYME, " ");
        formatFieldReplace(RELATEDPAIR, "\n", " ");
        formatFieldSplit(RELATEDPAIR, " ");
        extractPatternFromField(COMPOUND, "", "\\w{6}", " ", "", "");
    }

    /**
     * Print the Rpair entry on the TSV file set
     */
    @Override
    public void printOnTSVFile() {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        printOnTSVFileRPair(WID);
        printOnTSVFileArrayListValue(false, WID, COMPOUND, KEGGTables.getInstance().KEGGRPAIRCOMPOUND);
        printOnTSVFileArrayListValue(false, WID, REACTION, KEGGTables.getInstance().KEGGRPAIRREACTION);
        printOnTSVFileArrayListValue(false, WID, ENZYME, KEGGTables.getInstance().KEGGRPAIRENZYME);
        printOnTSVFileArrayListValue(false, WID, RELATEDPAIR, KEGGTables.getInstance().KEGGRPAIRRELATEDPAIRTEMP);
    }

    private void printOnTSVFileRPair(long WID) {
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGRPAIR, WID, "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGRPAIR, ((String) ((ArrayList) getFieldValue().get(ENTRY)).get(0)), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGRPAIR, (String) getFieldValue().get(NAME), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGRPAIR, (String) getFieldValue().get(TYPE), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGRPAIR, (String) getFieldValue().get(RCLASS), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGRPAIR, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
    }

    public String toString() {
        return "RPAIR\t"
                + getFieldValue().get(ENTRY)
                + "\n\t" + NAME
                + "\n\t\t"
                + getFieldValue().get(NAME)
                + "\n\t" + TYPE
                + "\n\t\t"
                + getFieldValue().get(TYPE)
                + "\n\t" + RCLASS
                + "\n\t\t"
                + getFieldValue().get(RCLASS)
                + "\n\t" + REACTION
                + "\n\t\t"
                + getFieldValue().get(REACTION)
                + "\n\t" + ENZYME
                + "\n\t\t"
                + getFieldValue().get(ENZYME)
                + "\n\t" + RELATEDPAIR
                + "\n\t\t"
                + getFieldValue().get(RELATEDPAIR);
    }
}
