package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.SourceTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Source Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class Source extends SourceTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFEntryWID = 0;
    private String release = null;
    private String releaseDate = null;
    private Names names = null;
    private Bibref bibref = null;
    private Xref xref = null;
    private AttributeList attributeList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Source() {
        open = false;

        names = new Names();
        attributeList = new AttributeList();
        bibref = new Bibref();
        xref = new Xref();
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.endElement(qname, depth, WID, MIF25Tables.getInstance().MIFOTHERALIAS);
                }
                if (attributeList.isOpen()) {
                    attributeList.endElement(qname, depth);
                }
                if (bibref.isOpen()) {
                    bibref.endElement(qname, depth);
                }
                if (xref.isOpen()) {
                    xref.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getSOURCEFLAGS())) {

            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSOURCE, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSOURCE, MIFEntryWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSOURCE, release, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSOURCE, releaseDate, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSOURCE, names.getShortLabel(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSOURCE, names.getFullName(), "\n");
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
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(names.getNAMESFLAGS())) {
                    names.setOpen(true);
                }
                if (qname.equals(attributeList.getATTRIBUTELISTFLAGS())) {
                    attributeList.setOpen(true);
                }
                if (qname.equals(bibref.getBIBREFFLAGS())) {
                    bibref.setOpen(true);
                }
                if (qname.equals(xref.getXREFFLAGS())) {
                    xref.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.startElement(qname, depth, attributes);
                }
                if (attributeList.isOpen()) {
                    attributeList.startElement(qname, depth, attributes, WID);
                }
                if (bibref.isOpen()) {
                    bibref.startElement(qname, depth, attributes, WID);
                }
                if (xref.isOpen()) {
                    xref.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getSOURCEFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFEntryWID = MIFEntryWID;

            release = attributes.getValue(getRELEASEFLAGS());
            if (attributes.getValue(getRELEASEDATEFLAGS()).length() > 10) {
                releaseDate = attributes.getValue(getRELEASEDATEFLAGS()).substring(0, 10);
            } else {
                releaseDate = attributes.getValue(getRELEASEDATEFLAGS());
            }

            names.setOpen(false);
            bibref.setOpen(false);
            attributeList.setOpen(false);
            xref.setOpen(false);
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
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.characters(tagname, qname, depth);
                }
                if (attributeList.isOpen()) {
                    attributeList.characters(tagname, qname, depth);
                }
                if (bibref.isOpen()) {
                    bibref.characters(tagname, qname, depth);
                }
                if (xref.isOpen()) {
                    xref.characters(tagname, qname, depth);
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
