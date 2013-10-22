package org.jbiowhparser.datasets.protein.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.IsoformTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Isoform Tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 1, 2010
 * @see
 */
public class Isoform extends IsoformTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long CommentWID = 0;
    private ArrayList<String> id = null;
    private IsoformData name = null;
    private ArrayList<IsoformData> names = null;
    private IsoformData sequence = null;
    private IsoformData note = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Isoform() {
        open = false;

        id = new ArrayList<>();
        names = new ArrayList<>();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getNAMEFLAGS())) {
                    name.open = false;
                    names.add(name);
                }
            }
        }
        if (qname.equals(getISOFORMFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, CommentWID, "\t");
            if (sequence != null) {
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, sequence.attrib, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, sequence.attrib1, "\t");
            } else {
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, "\\N", "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, "\\N", "\t");
            }
            if (note != null) {
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, note.data, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, note.attrib, "\n");
            } else {
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, "\\N", "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTISOFORM, "\\N", "\n");
            }

            if (!id.isEmpty()) {
                for (Iterator<String> it = id.iterator(); it.hasNext();) {
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINISOFORMID, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINISOFORMID, it.next(), "\n");
                }
                id.clear();
            }

            if (!names.isEmpty()) {
                for (Iterator<IsoformData> it = names.iterator(); it.hasNext();) {
                    name = it.next();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINISOFORMNAME, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINISOFORMNAME, name.data, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINISOFORMNAME, name.attrib, "\n");
                }
                names.clear();
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
            if (depth >= this.depth + 1) {
                if (qname.equals(getNAMEFLAGS())) {
                    name = new IsoformData();
                    name.attrib = attributes.getValue(getEVIDENCEFLAGS());
                }
                if (qname.equals(getSEQUENCEFLAGS())) {
                    sequence = new IsoformData();
                    sequence.attrib = attributes.getValue(getTYPEFLAGS());
                    sequence.attrib1 = attributes.getValue(getREFFLAGS());
                }
                if (qname.equals(getNOTEFLAGS())) {
                    note = new IsoformData();
                    note.attrib = attributes.getValue(getEVIDENCEFLAGS());
                }
            }
        }
        if (qname.equals(getISOFORMFLAGS())) {
            this.depth = depth;
            this.CommentWID = WID;
            open = true;

            this.WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            id.clear();
            names.clear();
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
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
                if (tagname.equals(getIDFLAGS())) {
                    id.add(qname);
                }
                if (tagname.equals(getNAMEFLAGS())) {
                    name.data = qname;
                }
                if (tagname.equals(getNOTEFLAGS())) {
                    note.data = qname;
                }
            }
        }
    }

    private class IsoformData {

        private boolean open = false;
        private String data = null;
        private String attrib = null;
        private String attrib1 = null;

        public IsoformData() {
            open = true;
            data = null;
            attrib = null;
            attrib1 = null;
        }
    }
}
