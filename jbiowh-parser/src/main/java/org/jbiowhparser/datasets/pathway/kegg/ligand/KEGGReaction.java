package org.jbiowhparser.datasets.pathway.kegg.ligand;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserEntry;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This class is KEGG Reaction parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
 * @since Oct 29, 2011
 */
public class KEGGReaction extends KEGGParserEntry implements KEGGParserFactory {

    /**
     * Format the Reaction entry parsing its fields
     */
    @Override
    public void format() {
        extractPatternFromField(ENTRY, "", "\\w*", " ", "", "");
        formatFieldSplit(NAME, ";");
        formatFieldReplace(COMMENT, "\n", " ");
        formatFieldReplace(DEFINITION, "\n", " ");
        formatFieldReplace(EQUATION, "\n", " ");
        formatFieldArrayListSplit(ORTHOLOGY, "\n", "^(K|k)\\d{5}.*", " ", null, null);
        formatFieldArrayListSplit(RPAIR, "\n", "^(RP|rp)\\d{5} .*", " ", null, null);
        formatFieldArrayListSplit(PATHWAY, "\n", "^[a-zA-Z]*\\d{5} .*", " ", null, null);
        formatFieldReplace(ENZYME, "\n", " ");
        formatFieldSplit(ENZYME, " ");
        parseEquationField();
    }

    /**
     * Print the Reaction entry on the TSV file set
     */
    @Override
    public void printOnTSVFile() {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        printOnTSVFileReaction(WID);
        printOnTSVFileArrayListValue(true, WID, NAME, KEGGTables.getInstance().KEGGREACTIONNAME);
        printOnTSVFileArrayListValue(false, WID, PATHWAY, KEGGTables.getInstance().KEGGREACTIONPATHWAY);
        printOnTSVFileArrayListValue(false, WID, ORTHOLOGY, KEGGTables.getInstance().KEGGREACTIONORTHOLOGY);
        printOnTSVFileArrayListValue(false, WID, RPAIR, KEGGTables.getInstance().KEGGREACTIONRPAIR);
        printOnTSVFileArrayListValue(false, WID, ENZYME, KEGGTables.getInstance().KEGGREACTIONENZYME);
        printOnTSVFileArrayListValue(false, WID, SUBSTRATE, KEGGTables.getInstance().KEGGREACTIONSUBSTRATE);
        printOnTSVFileArrayListValue(false, WID, PRODUCT, KEGGTables.getInstance().KEGGREACTIONPRODUCT);
    }

    private void printOnTSVFileReaction(long WID) {
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGREACTION, WID, "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGREACTION, ((String) ((ArrayList) getFieldValue().get(ENTRY)).get(0)), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGREACTION, (String) getFieldValue().get(COMMENT), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGREACTION, (String) getFieldValue().get(DEFINITION), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGREACTION, (String) getFieldValue().get(EQUATION), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGREACTION, (String) getFieldValue().get(REMARK), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGREACTION, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
    }

    private void parseEquationField() {
        ArrayList<String> subtrate = new ArrayList<>();
        ArrayList<String> product = new ArrayList<>();
        String value = (String) getFieldValue().get(EQUATION);

        if (value != null) {
            String[] reaction = value.split("=");

            if (reaction.length != 2) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), "Bad EQUATION format at: " + getFieldValue().get(ENTRY) + "entry");
                VerbLogger.getInstance().log(this.getClass(), "EQUATION: " + value);
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
            }

            Pattern pat = Pattern.compile("[C|G]\\d{5}");
            Matcher mat = pat.matcher(reaction[0]);
            while (mat.find()) {
                subtrate.add(mat.group());
            }
            getFieldValue().put(SUBSTRATE, subtrate);

            mat = pat.matcher(reaction[1]);
            while (mat.find()) {
                product.add(mat.group());
            }
            getFieldValue().put(PRODUCT, product);
        }
    }

    @Override
    public String toString() {
        return "REACTION\t"
                + getFieldValue().get(ENTRY)
                + "\n\t" + NAME
                + "\n\t\t"
                + getFieldValue().get(NAME)
                + "\n\t" + DEFINITION
                + "\n\t\t"
                + getFieldValue().get(DEFINITION)
                + "\n\t" + COMMENT
                + "\n\t\t"
                + getFieldValue().get(COMMENT)
                + "\n\t" + EQUATION
                + "\n\t\t"
                + getFieldValue().get(EQUATION)
                + "\n\t" + ORTHOLOGY
                + "\n\t\t"
                + getFieldValue().get(ORTHOLOGY)
                + "\n\t" + RPAIR
                + "\n\t\t"
                + getFieldValue().get(RPAIR)
                + "\n\t" + REMARK
                + "\n\t\t"
                + getFieldValue().get(REMARK)
                + "\n\t" + PATHWAY
                + "\n\t\t"
                + getFieldValue().get(PATHWAY)
                + "\n\t" + ENZYME
                + "\n\t\t"
                + getFieldValue().get(ENZYME);
    }
}
