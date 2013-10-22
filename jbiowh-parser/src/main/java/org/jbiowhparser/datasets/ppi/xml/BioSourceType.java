package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.BioSourceTypeTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML HostOrganism Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class BioSourceType extends BioSourceTypeTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIExperimentWID = 0;
    private String taxId = null;
    private Names names = null;
    private OpenCvType cellType = null;
    private OpenCvType compartment = null;
    private OpenCvType tissue = null;
    private String mainTag = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public BioSourceType(String mainTag) {
        this.mainTag = mainTag;
        open = false;

        names = new Names();
        cellType = new OpenCvType(getCELLTYPEFLAGS());
        compartment = new OpenCvType(getCOMPARTMENTFLAGS());
        tissue = new OpenCvType(getTISSUEFLAGS());
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
                if (cellType.isOpen()) {
                    cellType.endElement(qname, depth, MIF25Tables.getInstance().MIFBIOSOURCETYPECELLTYPE);
                }
                if (compartment.isOpen()) {
                    compartment.endElement(qname, depth, MIF25Tables.getInstance().MIFBIOSOURCETYPECOMPARTMENT);
                }
                if (tissue.isOpen()) {
                    tissue.endElement(qname, depth, MIF25Tables.getInstance().MIFBIOSOURCETYPETISSUE);
                }
            }
        }
        if (qname.equals(mainTag)) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIOSOURCETYPE, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIOSOURCETYPE, MIExperimentWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIOSOURCETYPE, taxId, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIOSOURCETYPE, names.getShortLabel(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIOSOURCETYPE, names.getFullName(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIOSOURCETYPE, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
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
                if (qname.equals(getCELLTYPEFLAGS())) {
                    cellType.setOpen(true);
                }
                if (qname.equals(getCOMPARTMENTFLAGS())) {
                    compartment.setOpen(true);
                }
                if (qname.equals(getTISSUEFLAGS())) {
                    tissue.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.startElement(qname, depth, attributes);
                }
                if (cellType.isOpen()) {
                    cellType.startElement(qname, depth, attributes, WID);
                }
                if (compartment.isOpen()) {
                    compartment.startElement(qname, depth, attributes, WID);
                }
                if (tissue.isOpen()) {
                    tissue.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(mainTag)) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIExperimentWID = MIFEntryWID;

            taxId = attributes.getValue(getNCBITAXIDFLAGS());
            cellType.setOpen(false);
            compartment.setOpen(false);
            tissue.setOpen(false);
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
                if (cellType.isOpen()) {
                    cellType.characters(tagname, qname, depth);
                }
                if (compartment.isOpen()) {
                    compartment.characters(tagname, qname, depth);
                }
                if (tissue.isOpen()) {
                    tissue.characters(tagname, qname, depth);
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
