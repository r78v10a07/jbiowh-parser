package org.jbiowhparser.datasets.protein.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.FeatureTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Feature tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 2, 2010
 * @see
 */
public class Feature extends FeatureTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long ProteinWID = 0;
    private Location location = null;
    private String original = null;
    private ArrayList<String> variation = null;
    private String type = null;
    private String status = null;
    private String id = null;
    private String description = null;
    private String evidence = null;
    private String ref = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Feature() {
        open = false;

        location = new Location();
        variation = new ArrayList<>();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (location.isOpen()) {
                    location.endElement(qname, depth);
                }
            }
            if (qname.equals(getFEATUREFLAGS())) {
                open = false;

                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, ProteinWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, type, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, status, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, id, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, description, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, evidence, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, ref, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATURE, original, "\n");

                if (!variation.isEmpty()) {
                    for (Iterator<String> it = variation.iterator(); it.hasNext();) {
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATUREVARIATION, WIDFactory.getInstance().getWid(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATUREVARIATION, WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINFEATUREVARIATION, it.next(), "\n");
                        WIDFactory.getInstance().increaseWid();
                    }
                    variation.clear();
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
    public void startElement(String qname, int depth, Attributes attributes, long WID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(location.getLOCATIONFLAGS())) {
                    location.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (location.isOpen()) {
                    location.startElement(qname, depth, attributes, this.WID);
                }
            }
        }
        if (qname.equals(getFEATUREFLAGS())) {
            this.depth = depth;
            this.ProteinWID = WID;
            open = true;

            this.WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            original = null;
            variation.clear();
            type = attributes.getValue(getTYPEFLAGS());
            status = attributes.getValue(getSTATUSFLAGS());
            id = attributes.getValue(getIDFLAGS());
            description = attributes.getValue(getDESCRIPTIONFLAGS());
            evidence = attributes.getValue(getEVIDENCEFLAGS());
            ref = attributes.getValue(getREFFLAGS());
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
            if (depth == this.depth + 1) {
                if (tagname.equals(getORIGINALFLAGS())) {
                    original = qname;
                }
                if (tagname.equals(getVARIATIONFLAGS())) {
                    variation.add(qname);
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
