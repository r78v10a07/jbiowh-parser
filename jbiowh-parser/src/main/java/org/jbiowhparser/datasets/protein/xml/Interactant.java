package org.jbiowhparser.datasets.protein.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.InteractantTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Interactant tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 2, 2010
 * @see
 */
public class Interactant extends InteractantTags {

    private int depth = 0;
    private boolean open = false;
    private long CommentWID = 0;
    private String intactID = null;
    private String id = null;
    private String label = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Interactant() {
        open = false;
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (qname.equals(getINTERACTANTFLAGS())) {
            open = false;
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTINTERACT, WIDFactory.getInstance().getWid(), "\t");
            WIDFactory.getInstance().increaseWid();
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTINTERACT, CommentWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTINTERACT, intactID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTINTERACT, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTINTERACT, label, "\n");
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long CommentWID) {
        if (qname.equals(getINTERACTANTFLAGS())) {
            this.depth = depth;
            this.CommentWID = CommentWID;
            open = true;

            intactID = attributes.getValue(getINTACTIDFLAGS());
            id = null;
            label = null;
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
            if (depth == this.depth + 1) {
                if (tagname.equals(getIDFLAGS())) {
                    id = qname;
                }
                if (tagname.equals(getLABELFLAGS())) {
                    label = qname;
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
