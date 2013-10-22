package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.AvailabilityTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Availability Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 25, 2010
 * @see
 */
public class Availability extends AvailabilityTags {

    private int depth = 0;
    private boolean open = false;
    private long MIFEntryWID = 0;
    private String availability = null;
    private String id = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Availability() {
        open = false;
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (qname.equals(getAVAILABILITYFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERAVAILABILITY, MIFEntryWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERAVAILABILITY, availability, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERAVAILABILITY, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERAVAILABILITY, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFEntryWID) {
        if (qname.equals(getAVAILABILITYFLAGS())) {
            this.depth = depth;
            open = true;

            this.MIFEntryWID = MIFEntryWID;
            id = attributes.getValue(getIDFLAGS());
            availability = null;
        }
    }

    /**
     * This is the characters method
     *
     * @param tagname the tags name
     * @param qname text value
     * @param depth the depth on the XML file
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (tagname.equals(getAVAILABILITYFLAGS())) {
                availability = qname;
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
