package org.jbiowhparser.datasets.protein.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.KeywordTags;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Keyword tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 2, 2010
 * @see
 */
public class Keyword extends KeywordTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private String id = null;
    private String evidence = null;
    private String keyword = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Keyword() {
        open = false;
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (qname.equals(getKEYWORDFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINWIDKEYWORDTEMP, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINWIDKEYWORDTEMP, evidence, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINWIDKEYWORDTEMP, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINWIDKEYWORDTEMP, keyword, "\n");
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long WID) {
        if (qname.equals(getKEYWORDFLAGS())) {
            this.depth = depth;
            this.WID = WID;
            open = true;

            id = attributes.getValue(getIDFLAGS());
            evidence = attributes.getValue(getEVIDENCEFLAGS());
            keyword = null;
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
            if (depth == this.depth) {
                if (tagname.equals(getKEYWORDFLAGS())) {
                    keyword = qname;
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
