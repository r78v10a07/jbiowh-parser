package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.EntrySetTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML EntrySet tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class EntrySet extends EntrySetTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private String level = null;
    private String version = null;
    private String minorVersion = null;
    private Entry entry = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param dataset the WH data set manager
     */
    public EntrySet() {
        open = false;

        entry = new Entry();
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
                if (entry.isOpen()) {
                    entry.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getENTRYSETFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSET, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSET, level, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSET, version, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSET, minorVersion, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSET, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes) {
        if (open) {
            if (qname.equals(entry.getENTRYFLAGS())) {
                entry.setOpen(true);
            }
            if (depth >= this.depth + 1) {
                if (entry.isOpen()) {
                    entry.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getENTRYSETFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            level = attributes.getValue(getLEVELFLAGS());
            version = attributes.getValue(getVERSIONFLAGS());
            minorVersion = attributes.getValue(getMINORVERSIONFLAGS());
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
                if (entry.isOpen()) {
                    entry.characters(tagname, qname, depth);
                }
            }
        }
    }
}
