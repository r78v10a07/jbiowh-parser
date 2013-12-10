package org.jbiowhparser.datasets.drug.drugbank.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.xml.sax.Attributes;

/**
 * This class is the TwoFieldXMLParser XML parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-11-08 14:37:19 +0100
 * (Thu, 08 Nov 2012) $ $LastChangedRevision: 322 $
 *
 * @since Sep 10, 2011
 */
public class TwoFieldXMLParser {

    private int depth = 0;
    private boolean open = false;
    private long drug_WID = 0;
    private List values = null;
    private String tablename = null;
    private String mainTag = null;
    private String secondTag = null;
    private String attrib = null;
    private String attribName = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param tablename
     * @param mainTag
     * @param secondTag
     */
    public TwoFieldXMLParser(String tablename, String mainTag, String secondTag) {
        open = false;

        this.tablename = tablename;
        this.mainTag = mainTag;
        this.secondTag = secondTag;
        this.attribName = null;
        values = new ArrayList<>();
    }

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param tablename
     * @param mainTag
     * @param secondTag
     * @param attribName
     */
    public TwoFieldXMLParser(String tablename, String mainTag, String secondTag, String attribName) {
        open = false;

        this.tablename = tablename;
        this.mainTag = mainTag;
        this.secondTag = secondTag;
        this.attribName = attribName;
        values = new ArrayList<>();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param qname
     * @param depth XML depth
     * @param hasWID
     */
    public void endElement(String qname, int depth, boolean hasWID) {
        if (qname.equals(mainTag) && depth == this.depth) {
            open = false;

            Iterator it = values.iterator();
            while (it.hasNext()) {
                String value = (String) it.next();
                if (value != null) {
                    if (hasWID) {
                        ParseFiles.getInstance().printOnTSVFile(tablename, WIDFactory.getInstance().getWid(), "\t");
                        WIDFactory.getInstance().increaseWid();
                    }
                    ParseFiles.getInstance().printOnTSVFile(tablename, drug_WID, "\t");
                    if (attribName == null) {
                        ParseFiles.getInstance().printOnTSVFile(tablename, value, "\n");
                    } else {
                        ParseFiles.getInstance().printOnTSVFile(tablename, value, "\t");
                        ParseFiles.getInstance().printOnTSVFile(tablename, attrib, "\n");
                    }
                }
            }

            values.clear();
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param qname
     * @param depth
     * @param attributes
     * @param drug_WID
     */
    public void startElement(String qname, int depth, Attributes attributes, long drug_WID) {
        if (open && attribName != null) {
            if (depth == this.depth + 1) {
                if (qname.equals(secondTag)) {
                    attrib = attributes.getValue(attribName);
                }
            }
        }
        if (qname.equals(mainTag)) {
            this.depth = depth;
            open = true;

            this.drug_WID = drug_WID;
            values.clear();
        }
    }

    /**
     *
     * @param tagname
     * @param qname
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (tagname.equals(secondTag)) {
                    values.add(qname);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isOpen() {
        return open;
    }

    /**
     *
     * @param open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }
}
