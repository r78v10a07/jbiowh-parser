package org.jbiowhparser.datasets.drug.drugbank.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.drug.drugbank.utility.DrugBankSplitReference;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.BondTypeTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.TargetBondTypeTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.xml.sax.Attributes;

/**
 * This class is the BondType XML parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 12, 2011
 */
public class BondType {

    private int depth = 0;
    private boolean open = false;
    private long drug_WID = 0;
    private long WID = 0;
    private String partner = null;
    private String position = null;
    private String references = null;
    private String knownAction = null;
    private TwoFieldXMLParser actions = null;
    private String mainTag = null;
    private String mainTable = null;
    private String refTable = null;
    private boolean knownActionFlags = false;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param mainTag
     * @param mainTable
     * @param refTable
     * @param actionTable
     * @param knownActionFlags
     */
    public BondType(String mainTag, String mainTable, String refTable, String actionTable, boolean knownActionFlags) {
        open = false;

        this.mainTag = mainTag;
        this.mainTable = mainTable;
        this.refTable = refTable;
        this.knownActionFlags = knownActionFlags;
        actions = new TwoFieldXMLParser(actionTable,
                BondTypeTags.getInstance().ACTIONS, BondTypeTags.getInstance().ACTION);
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param qname
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (actions.isOpen()) {
                    actions.endElement(qname, depth, true);
                }
            }
        }
        if (qname.equals(mainTag) && depth == this.depth) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(mainTable, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(mainTable, drug_WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(mainTable, partner, "\t");
            if (knownActionFlags) {
                ParseFiles.getInstance().printOnTSVFile(mainTable, position, "\t");
                ParseFiles.getInstance().printOnTSVFile(mainTable, knownAction, "\n");
            } else {
                ParseFiles.getInstance().printOnTSVFile(mainTable, position, "\n");
            }

            (new DrugBankSplitReference()).split(references, WID, refTable);
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param qname
     * @param depth
     * @param attributes
     * @param drug_WID
     */
    public void startElement(String qname, int depth, Attributes attributes, long drug_WID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(BondTypeTags.getInstance().ACTIONS)) {
                    actions.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (actions.isOpen()) {
                    actions.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(mainTag)) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.drug_WID = drug_WID;
            partner = attributes.getValue(BondTypeTags.getInstance().PARTNER);
            position = attributes.getValue(BondTypeTags.getInstance().POSITION);
            references = null;
            knownAction = null;
        }
    }

    /**
     *
     * @param tagname
     * @param qname
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (actions.isOpen()) {
                    actions.characters(tagname, qname, depth);
                }
            }
            if (depth == this.depth + 1) {
                if (tagname.equals(BondTypeTags.getInstance().REFERENCES)) {
                    references = qname;
                }
                if (tagname.equals(TargetBondTypeTags.getInstance().KNOWNACTION)) {
                    knownAction = qname;
                }
            }
        }

    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
