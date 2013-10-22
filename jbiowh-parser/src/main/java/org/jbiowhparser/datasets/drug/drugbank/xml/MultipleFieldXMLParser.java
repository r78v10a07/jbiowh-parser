package org.jbiowhparser.datasets.drug.drugbank.xml;

import java.util.HashMap;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.xml.sax.Attributes;

/**
 * This class is the Three Field XML Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 11, 2011
 */
public class MultipleFieldXMLParser {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long drug_WID = 0;
    private HashMap values = null;
    private String tablename = null;
    private String[] tags = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param tablename
     * @param tags
     */
    public MultipleFieldXMLParser(String tablename, String[] tags) {
        open = false;

        this.tablename = tablename;
        this.tags = tags;
        values = new HashMap();
        for (int i = 1; i < tags.length; i++) {
            values.put(tags[i], null);
        }
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param qname
     * @param depth XML depth
     * @param hasWID
     */
    public void endElement(String qname, int depth, boolean hasWID) {
        if (qname.equals(tags[0]) && depth == this.depth) {
            open = false;

            if (!values.isEmpty()) {
                if (hasWID) {
                    ParseFiles.getInstance().printOnTSVFile(tablename, WID, "\t");
                }
                ParseFiles.getInstance().printOnTSVFile(tablename, drug_WID, "\t");
                for (int i = 1; i < tags.length; i++) {
                    String value = (String) values.get(tags[i]);
                    String valueAttrib = (String) values.get(tags[i] + "attrib");
                    if (i < tags.length - 1) {
                        ParseFiles.getInstance().printOnTSVFile(tablename, value, "\t");
                        if (valueAttrib != null) {
                            ParseFiles.getInstance().printOnTSVFile(tablename, valueAttrib, "\t");
                        }
                    } else {
                        if (valueAttrib != null) {
                            ParseFiles.getInstance().printOnTSVFile(tablename, value, "\t");
                            ParseFiles.getInstance().printOnTSVFile(tablename, valueAttrib, "\n");
                        } else {
                            ParseFiles.getInstance().printOnTSVFile(tablename, value, "\n");
                        }
                    }
                }
            }
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
        if (open) {
            for (int i = 1; i < tags.length; i++) {
                if (qname.equals(tags[i])) {
                    if (attributes.getLength() > 0) {
                        for (int j = 0; j < attributes.getLength(); j++) {
                            values.put(tags[i] + "attrib", attributes.getValue(j));
                        }
                    }
                }
            }
        }
        if (qname.equals(tags[0])) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

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
                for (int i = 1; i < tags.length; i++) {
                    if (tagname.equals(tags[i])) {
                        values.put(tags[i], copyQName(qname, (String) values.get(tags[i])));
                    }
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

    /**
     *
     * @return
     */
    public String[] getTags() {
        return tags;
    }

    private String copyQName(String qname, String value) {
        String result;
        if (value == null) {
            result = qname;
        } else {
            result = value.concat(qname);
        }
        return result;
    }
}
