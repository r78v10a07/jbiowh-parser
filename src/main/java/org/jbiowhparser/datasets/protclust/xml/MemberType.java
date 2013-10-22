package org.jbiowhparser.datasets.protclust.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protclust.xml.tags.MemberTypeTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protclust.UniRefTables;
import org.xml.sax.Attributes;

/**
 * This Class is the MemberType XML parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Aug 19, 2011
 */
public class MemberType extends MemberTypeTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long entryWID = 0;
    private String mainTags = null;
    private String type = null;
    private String id = null;
    private String taxId = null;
    private ArrayList<Property> property = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public MemberType(String mainTags) {
        open = false;
        this.mainTags = mainTags;
        property = new ArrayList<>();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth, boolean isRepresentative) {

        if (qname.equals(mainTags)) {
            open = false;

            Iterator it = property.iterator();
            while (it.hasNext()) {
                Property prop = (Property) it.next();
                if (prop.getType().equals("NCBI taxonomy")) {
                    taxId = prop.getValue();
                } else {
                    ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERPROPERTY, WIDFactory.getInstance().getWid(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERPROPERTY, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERPROPERTY, prop.getType(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERPROPERTY, prop.getValue(), "\n");
                    WIDFactory.getInstance().increaseWid();
                }
            }

            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERTEMP2, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERTEMP2, entryWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERTEMP2, (String) null, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERTEMP2, taxId, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERTEMP2, type, "\t");
            if (id.contains("-")) {
                ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERTEMP2, id.substring(0, id.indexOf("-")), "\t");
            } else {
                ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERTEMP2, id, "\t");
            }
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFMEMBERTEMP2, isRepresentative, "\n");
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long entryWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(DBREFERENCE)) {
                    type = attributes.getValue(TYPE);
                    id = attributes.getValue(ID);
                }
            }
            if (depth == this.depth + 2) {
                if (qname.equals(PROPERTY)) {
                    property.add(new Property(attributes.getValue(TYPE), attributes.getValue(VALUE)));
                }
            }
        }

        if (qname.equals(mainTags)) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            this.entryWID = entryWID;
            property.clear();
        }
    }

    /**
     *
     * @param tagname
     * @param name
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void clear() {
        depth = 0;
        open = false;
        WID = 0;
        entryWID = 0;
        type = null;
        id = null;
        taxId = null;
        property.clear();
    }
}
