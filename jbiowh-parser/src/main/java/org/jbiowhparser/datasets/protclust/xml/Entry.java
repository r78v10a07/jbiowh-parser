package org.jbiowhparser.datasets.protclust.xml;

import java.util.ArrayList;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protclust.xml.tags.EntryTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protclust.UniRefTables;
import org.xml.sax.Attributes;

/**
 * This Class is handled the XML Entry tag on Uniref
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Aug 19, 2011
 */
public class Entry extends EntryTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private String id = null;
    private String updated = null;
    private String name = null;
    private String taxId = null;
    private ArrayList<Property> property = null;
    private MemberType representativeMember = null;
    private MemberType member = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Entry() {
        open = false;
        property = new ArrayList<>();
        representativeMember = new MemberType(REPRESENTATIVEMEMBER);
        member = new MemberType(MEMBER);
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (representativeMember.isOpen()) {
                    representativeMember.endElement(qname, depth, true);
                }
                if (member.isOpen()) {
                    member.endElement(qname, depth, false);
                }
            }
        }

        if (qname.equals(ENTRY)) {
            open = false;
            for (Property prop : property) {
                if (prop.getType().equals("common taxon ID")) {
                    taxId = prop.getValue();
                } else {
                    ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRYPROPERTY, WIDFactory.getInstance().getWid(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRYPROPERTY, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRYPROPERTY, prop.getType(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRYPROPERTY, prop.getValue(), "\n");
                    WIDFactory.getInstance().increaseWid();
                }
            }
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRY, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRY, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRY, updated, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRY, name, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRY, taxId, "\t");
            ParseFiles.getInstance().printOnTSVFile(UniRefTables.getInstance().UNIREFENTRY, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(PROPERTY)) {
                    property.add(new Property(attributes.getValue(TYPE), attributes.getValue(VALUE)));
                }
                if (qname.equals(REPRESENTATIVEMEMBER)) {
                    representativeMember.clear();
                    representativeMember.setOpen(true);
                }
                if (qname.equals(MEMBER)) {
                    member.clear();
                    member.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (representativeMember.isOpen()) {
                    representativeMember.startElement(qname, depth, attributes, WID);
                }
                if (member.isOpen()) {
                    member.startElement(qname, depth, attributes, WID);
                }
            }
        }

        if (qname.equals(ENTRY)) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            name = null;
            id = attributes.getValue(ID);
            updated = attributes.getValue(UPDATED);
            taxId = null;
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
        if (open) {
            if (depth >= this.depth + 1) {
                if (representativeMember.isOpen()) {
                    representativeMember.characters(tagname, qname, depth);
                }
                if (member.isOpen()) {
                    member.characters(tagname, qname, depth);
                }
            }
            if (depth == this.depth + 1) {
                if (tagname.equals(NAME)) {
                    name = qname;
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
