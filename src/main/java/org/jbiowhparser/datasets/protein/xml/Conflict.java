package org.jbiowhparser.datasets.protein.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.ConflictTags;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Conflict tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 1, 2010
 * @see
 */
public class Conflict extends ConflictTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private String type = null;
    private String seqVersion = null;
    private String seqResource = null;
    private String seqID = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Conflict() {
        open = false;
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (qname.equals(getCONFLICTFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTCONFLICT, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTCONFLICT, type, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTCONFLICT, seqVersion, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTCONFLICT, seqResource, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTCONFLICT, seqID, "\n");
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
                if (qname.equals(getSEQUENCEFLAGS())) {
                    seqResource = attributes.getValue(getRESOURCEFLAGS());
                    seqVersion = attributes.getValue(getVERSIONFLAGS());
                    seqID = attributes.getValue(getIDFLAGS());
                }
            }
        }
        if (qname.equals(getCONFLICTFLAGS())) {
            this.depth = depth;
            this.WID = WID;
            open = true;

            type = attributes.getValue(getTYPEFLAGS());
            seqResource = null;
            seqVersion = "-1";
            seqID = null;
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
