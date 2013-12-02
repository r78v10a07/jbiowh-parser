package org.jbiowhparser.datasets.protein.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.GeneLocationTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML GeneLocation tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 30, 2010
 * @see
 */
public class GeneLocation extends GeneLocationTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long proteinWID = 0;
    private String name = null;
    private String type = null;
    private String status = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     */
    public GeneLocation() {
        open = false;
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param qname
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {

        if (qname.equals(getGENELOCATIONFLAGS())) {
            open = false;
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENELOCATION, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENELOCATION, proteinWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENELOCATION, name, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENELOCATION, type, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENELOCATION, status, "\n");
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param qname
     * @param depth
     * @param attributes
     * @param proteinWID
     */
    public void startElement(String qname, int depth, Attributes attributes, long proteinWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getNAMEFLAGS())) {
                    status = attributes.getValue(getSTATUSFLAGS());
                }
            }
        }
        if (qname.equals(getGENELOCATIONFLAGS())) {
            this.depth = depth;
            this.proteinWID = proteinWID;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            type = attributes.getValue(getTYPEFLAGS());
            status = null;
            name = null;
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
            if (depth == this.depth + 1) {
                if (tagname.equals(getNAMEFLAGS())) {
                    name = qname;
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isOpen() {
        return open;
    }

    /**
     *
     * @param open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }
}
