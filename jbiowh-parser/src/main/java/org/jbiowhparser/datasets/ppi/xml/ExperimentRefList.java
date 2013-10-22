package org.jbiowhparser.datasets.ppi.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.ExperimentRefListTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML ExperimentRefList Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class ExperimentRefList extends ExperimentRefListTags {

    private int depth = 0;
    private boolean open = false;
    private long MIFOtherWID = 0;
    private ArrayList<String> experimentRef = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public ExperimentRefList() {
        open = false;

        experimentRef = new ArrayList<>();
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (qname.equals(getEXPERIMENTREFLISTFLAGS())) {
            open = false;

            if (!experimentRef.isEmpty()) {
                for (Iterator<String> it = experimentRef.iterator(); it.hasNext();) {
                    ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHEREXPERIMENTREF, MIFOtherWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHEREXPERIMENTREF, it.next(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHEREXPERIMENTREF, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
                }
                experimentRef.clear();
            }
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFOtherWID) {
        if (qname.equals(getEXPERIMENTREFLISTFLAGS())) {
            this.depth = depth;
            open = true;

            this.MIFOtherWID = MIFOtherWID;

            experimentRef.clear();
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
                if (tagname.equals(getEXPERIMENTREFFLAGS())) {
                    experimentRef.add(qname);
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
