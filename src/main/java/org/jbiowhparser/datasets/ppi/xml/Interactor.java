package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.InteractorTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Interactor Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class Interactor extends InteractorTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFEntryWID = 0;
    private String id = null;
    private Names names = null;
    private Xref xref = null;
    private CVType interactorType = null;
    private BioSourceType organism = null;
    private String sequence = null;
    private AttributeList attributeList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Interactor() {
        open = false;

        names = new Names();
        xref = new Xref();
        interactorType = new CVType(getINTERACTORTYPEFLAGS());
        organism = new BioSourceType(getORGANISMFLAGS());
        attributeList = new AttributeList();
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
                if (xref.isOpen()) {
                    xref.endElement(qname, depth);
                }
                if (interactorType.isOpen()) {
                    interactorType.endElement(qname, depth, MIF25Tables.getInstance().MIFINTERACTORINTERACTORTYPE);
                }
                if (organism.isOpen()) {
                    organism.endElement(qname, depth);
                }
                if (attributeList.isOpen()) {
                    attributeList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getINTERACTORFLAGS())) {

            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTOR, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTOR, MIFEntryWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTOR, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTOR, names.getShortLabel(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTOR, names.getFullName(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTOR, sequence, "\n");
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
                if (qname.equals(xref.getXREFFLAGS())) {
                    xref.setOpen(true);
                }
                if (qname.equals(getINTERACTORTYPEFLAGS())) {
                    interactorType.setOpen(true);
                }
                if (qname.equals(getORGANISMFLAGS())) {
                    organism.setOpen(true);
                }
                if (qname.equals(attributeList.getATTRIBUTELISTFLAGS())) {
                    attributeList.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.startElement(qname, depth, attributes);
                }
                if (xref.isOpen()) {
                    xref.startElement(qname, depth, attributes, WID);
                }
                if (interactorType.isOpen()) {
                    interactorType.startElement(qname, depth, attributes, WID);
                }
                if (organism.isOpen()) {
                    organism.startElement(qname, depth, attributes, WID);
                }
                if (attributeList.isOpen()) {
                    attributeList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getINTERACTORFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFEntryWID = MIFEntryWID;

            id = attributes.getValue(getIDFLAGS());

            names.setOpen(false);
            xref.setOpen(false);
            interactorType.setOpen(false);
            organism.setOpen(false);
            sequence = null;
            attributeList.setOpen(false);
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
            if (depth == this.depth + 1) {
                if (tagname.equals(getSEQUENCEFLAGS())) {
                    sequence = qname;
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.characters(tagname, qname, depth);
                }
                if (xref.isOpen()) {
                    xref.characters(tagname, qname, depth);
                }
                if (interactorType.isOpen()) {
                    interactorType.characters(tagname, qname, depth);
                }
                if (organism.isOpen()) {
                    organism.characters(tagname, qname, depth);
                }
                if (attributeList.isOpen()) {
                    attributeList.characters(tagname, qname, depth);
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
