package org.jbiowhparser.datasets.drug.drugbank.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.TaxonomyTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.drug.drugbank.DrugBankTables;
import org.xml.sax.Attributes;

/**
 * This class is the Taxonomy XML parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 10, 2011
 */
public class Taxonomy extends TaxonomyTags {

    private int depth = 0;
    private boolean open = false;
    private long drug_WID = 0;
    private String kingdom = null;
    private Substructure substructure = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Taxonomy() {
        open = false;
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth == this.depth + 2) {
                if (qname.equals(SUBSTRUCTURE)) {
                    if (substructure.substructure != null) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTAXONOMYSUBSTRUCTURES, WIDFactory.getInstance().getWid(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTAXONOMYSUBSTRUCTURES, drug_WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTAXONOMYSUBSTRUCTURES, substructure.substructure, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTAXONOMYSUBSTRUCTURES, substructure.classValue, "\n");
                        WIDFactory.getInstance().increaseWid();
                    }
                    substructure = null;
                }
            }
            if (qname.equals(TAXONOMY) && depth == this.depth) {
                open = false;

                if (kingdom != null) {
                    if (!kingdom.isEmpty()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTAXONOMY, WIDFactory.getInstance().getWid(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTAXONOMY, drug_WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTAXONOMY, kingdom, "\n");
                        WIDFactory.getInstance().increaseWid();
                    }
                }
            }
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long drug_WID) {
        if (open) {
            if (depth == this.depth + 2) {
                if (qname.equals(SUBSTRUCTURE)) {
                    substructure = new Substructure();
                    substructure.classValue = attributes.getValue("class");
                }
            }
        }
        if (qname.equals(TAXONOMY)) {
            this.depth = depth;
            open = true;



            this.drug_WID = drug_WID;
            kingdom = null;
            substructure = null;
        }
    }

    /**
     *
     * @param tagname
     * @param name
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (tagname.equals(SUBSTRUCTURE)) {
                    substructure.substructure = qname;
                }
            }
            if (depth == this.depth + 1) {
                if (tagname.equals(KINGDOM)) {
                    kingdom = qname;
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

    private class Substructure {

        private String substructure = null;
        private String classValue = null;
    }
}
