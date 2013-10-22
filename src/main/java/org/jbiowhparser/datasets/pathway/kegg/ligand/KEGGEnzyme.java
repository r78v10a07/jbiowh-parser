package org.jbiowhparser.datasets.pathway.kegg.ligand;

import java.util.ArrayList;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserEntry;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This class is the KEGG Enzyme parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 29, 2011
 */
public class KEGGEnzyme extends KEGGParserEntry implements KEGGParserFactory {

    /**
     * Format the Enzyme entry parsing its fields
     */
    @Override
    public void format() {
        extractPatternFromField(ENTRY, "", "\\d*\\.(\\d*|\\-)\\.(\\d*|\\-)\\.(\\d*|\\-)", " ", "", "");
        formatFieldSplit(NAME, ";");
        formatFieldReplace(COMMENT, "\n", " ");
        formatFieldSplit(CLASS, ";");
        formatFieldSplit(ALL_REAC, ";");
        formatFieldSplit(SYSNAME, ";");
        formatFieldSplit(REACTION, ";");
        extractPatternFromField(SUBSTRATE, ":", "\\w{6}", "]", "", "");
        extractPatternFromField(PRODUCT, ":", "\\w{6}", "]", "", "");
        extractPatternFromField(COFACTOR, ":", "\\w{6}", "]", "", "");
        extractPatternFromField(INHIBITOR, ":", "\\w{6}", "]", "", "");
        extractPatternFromField(EFFECTOR, ":", "\\w{6}", "]", "", "");
        formatFieldArrayListSplit(ORTHOLOGY, "\n", "^(K|k)\\d{5}.*", " ", null, null);
        formatFieldArrayListSplit(PATHWAY, "\n", "^[a-zA-Z]*\\d{5} .*", " ", null, null);
        formatFieldArrayListSplit(GENES, "\n", "^[a-zA-Z]{3}: .*", ": ", ":", "");
        formatFieldArrayListSplit(DBLINKS, "\n", ".*: .*", ": ", ":", "");
    }

    /**
     * Print the Enzyme entry on the TSV file set
     */
    @Override
    public void printOnTSVFile() {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        printOnTSVFileEnzyme(WID);
        printOnTSVFileArrayListValue(true, WID, NAME, KEGGTables.getInstance().KEGGENZYMENAME);
        printOnTSVFileArrayListValue(false, WID, PATHWAY, KEGGTables.getInstance().KEGGENZYMEPATHWAY);
        printOnTSVFileArrayListValue(false, WID, ORTHOLOGY, KEGGTables.getInstance().KEGGENZYMEORTHOLOGY);
        printOnTSVFileArrayListValue(false, WID, CLASS, KEGGTables.getInstance().KEGGENZYMECLASSTEMP);
        printOnTSVFileArrayListValue(false, WID, ALL_REAC, KEGGTables.getInstance().KEGGENZYMEALLREAC);
        printOnTSVFileArrayListValue(true, WID, SYSNAME, KEGGTables.getInstance().KEGGENZYMESYSNAME);
        printOnTSVFileArrayListValue(false, WID, REACTION, KEGGTables.getInstance().KEGGENZYMEREACTION);
        printOnTSVFileArrayListValue(false, WID, SUBSTRATE, KEGGTables.getInstance().KEGGENZYMESUBSTRATE);
        printOnTSVFileArrayListValue(false, WID, PRODUCT, KEGGTables.getInstance().KEGGENZYMEPRODUCT);
        printOnTSVFileArrayListValue(false, WID, COFACTOR, KEGGTables.getInstance().KEGGENZYMECOFACTOR);
        printOnTSVFileArrayListValue(false, WID, GENES, KEGGTables.getInstance().KEGGENZYMEGENES);
        printOnTSVFileArrayListValue(false, WID, DBLINKS, KEGGTables.getInstance().KEGGENZYMEDBLINK);
        printOnTSVFileArrayListValue(false, WID, INHIBITOR, KEGGTables.getInstance().KEGGENZYMEINHIBITOR);
        printOnTSVFileArrayListValue(false, WID, EFFECTOR, KEGGTables.getInstance().KEGGENZYMEEFFECTOR);
    }

    private void printOnTSVFileEnzyme(long WID) {
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGENZYME, WID, "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGENZYME, ((String) ((ArrayList) getFieldValue().get(ENTRY)).get(0)), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGENZYME, (String) getFieldValue().get(COMMENT), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGENZYME, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
    }

    @Override
    public String toString() {
        return "ENZYME\t"
                + getFieldValue().get(ENTRY)
                + "\n\t" + NAME
                + "\n\t\t"
                + getFieldValue().get(NAME)
                + "\n\t" + COMMENT
                + "\n\t\t"
                + getFieldValue().get(COMMENT)
                + "\n\t" + CLASS
                + "\n\t\t"
                + getFieldValue().get(CLASS)
                + "\n\t" + ALL_REAC
                + "\n\t\t"
                + getFieldValue().get(ALL_REAC)
                + "\n\t" + ORTHOLOGY
                + "\n\t\t"
                + getFieldValue().get(ORTHOLOGY)
                + "\n\t" + PATHWAY
                + "\n\t\t"
                + getFieldValue().get(PATHWAY)
                + "\n\t" + SYSNAME
                + "\n\t\t"
                + getFieldValue().get(SYSNAME)
                + "\n\t" + REACTION
                + "\n\t\t"
                + getFieldValue().get(REACTION)
                + "\n\t" + SUBSTRATE
                + "\n\t\t"
                + getFieldValue().get(SUBSTRATE)
                + "\n\t" + PRODUCT
                + "\n\t\t"
                + getFieldValue().get(PRODUCT)
                + "\n\t" + COFACTOR
                + "\n\t\t"
                + getFieldValue().get(COFACTOR)
                + "\n\t" + GENES
                + "\n\t\t"
                + getFieldValue().get(GENES)
                + "\n\t" + DBLINKS
                + "\n\t\t"
                + getFieldValue().get(DBLINKS)
                + "\n\t" + INHIBITOR
                + "\n\t\t"
                + getFieldValue().get(INHIBITOR)
                + "\n\t" + EFFECTOR
                + "\n\t\t"
                + getFieldValue().get(EFFECTOR);
    }
}
