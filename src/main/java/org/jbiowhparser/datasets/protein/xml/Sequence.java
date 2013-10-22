package org.jbiowhparser.datasets.protein.xml;

import org.jbiowhparser.datasets.protein.xml.tags.SequenceTags;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Sequence Tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 3, 2010
 * @see
 */
public class Sequence extends SequenceTags {

    private int depth = 0;
    private boolean open = false;
    private String length = null;
    private String mass = null;
    private String checksum = null;
    private String modified = null;
    private String version = null;
    private String precursor = null;
    private String fragment = null;
    private String seq = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Sequence() {
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
            if (qname.equals(getSEQUENCEFLAGS())) {
                open = false;
            }
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes) {
        if (qname.equals(getSEQUENCEFLAGS())) {
            this.depth = depth;
            open = true;

            length = attributes.getValue(getLENGTHFLAGS());
            mass = attributes.getValue(getMASSFLAGS());
            checksum = attributes.getValue(getCHECKSUMFLAGS());
            modified = attributes.getValue(getMODIFIEDFLAGS());
            version = attributes.getValue(getVERSIONFLAGS());
            precursor = attributes.getValue(getPRECURSORFLAGS());
            fragment = attributes.getValue(getFRAGMENTFLAGS());
            seq = null;
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
            if (tagname.equals(getSEQUENCEFLAGS())) {
                seq = qname;
            }
        }
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getPrecursor() {
        return precursor;
    }

    public void setPrecursor(String precursor) {
        this.precursor = precursor;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
