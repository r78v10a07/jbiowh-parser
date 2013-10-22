package org.jbiowhparser.datasets.ppi.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.XrefTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Xref Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class Xref extends XrefTags {

    private int depth = 0;
    private boolean open = false;
    private long MIFOtherWID = 0;
    private DBReferenceType dbref = null;
    private ArrayList<DBReferenceType> dbrefs = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Xref() {
        open = false;
        dbrefs = new ArrayList<>();
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getPRIMARYREFFLAGS()) || qname.equals(getSECONDARYREFFLAGS())) {
                    dbref.open = false;
                    dbrefs.add(dbref);
                }
            }
            if (depth >= this.depth + 1) {
                if (dbref.attributeList.isOpen()) {
                    dbref.attributeList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getXREFFLAGS())) {
            open = false;

            if (!dbrefs.isEmpty()) {
                for (Iterator<DBReferenceType> it = dbrefs.iterator(); it.hasNext();) {
                    String table = MIF25Tables.getInstance().MIFOTHERXREF;
                    dbref = it.next();
                    switch (dbref.db) {
                        case "go":
                            table = MIF25Tables.getInstance().MIFOTHERXREFGO;
                            break;
                        case "pubmed":
                            table = MIF25Tables.getInstance().MIFOTHERXREFPUBMED;
                            break;
                        case "refseq":
                            table = MIF25Tables.getInstance().MIFOTHERXREFREFSEQ;
                            break;
                        case "uniprotkb":
                            table = MIF25Tables.getInstance().MIFOTHERXREFUNIPROT;
                            break;
                    }

                    ParseFiles.getInstance().printOnTSVFile(table, dbref.WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, MIFOtherWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, dbref.db, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, dbref.dbAc, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, dbref.id, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, dbref.secondary, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, dbref.version, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, dbref.refType, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, dbref.refTypeAc, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, dbref.primary, "\t");
                    ParseFiles.getInstance().printOnTSVFile(table, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
                }
                dbrefs.clear();
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
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getPRIMARYREFFLAGS())) {
                    dbref = new DBReferenceType();
                    dbref.WID = WIDFactory.getInstance().getWid();
                    WIDFactory.getInstance().increaseWid();
                    dbref.db = attributes.getValue(getDBFLAGS());
                    dbref.dbAc = attributes.getValue(getDBACFLAGS());
                    dbref.id = attributes.getValue(getIDFLAGS());
                    dbref.primary = true;
                    dbref.refType = attributes.getValue(getREFTYPEFLAGS());
                    dbref.refTypeAc = attributes.getValue(getREFTYPEACFLAGS());
                    dbref.secondary = attributes.getValue(getSECONDARYFLAGS());
                    dbref.version = attributes.getValue(getVERSIONFLAGS());
                } else if (qname.equals(getSECONDARYREFFLAGS())) {
                    dbref = new DBReferenceType();
                    dbref.WID = WIDFactory.getInstance().getWid();
                    WIDFactory.getInstance().increaseWid();
                    dbref.db = attributes.getValue(getDBFLAGS());
                    dbref.dbAc = attributes.getValue(getDBACFLAGS());
                    dbref.id = attributes.getValue(getIDFLAGS());
                    dbref.primary = false;
                    dbref.refType = attributes.getValue(getREFTYPEFLAGS());
                    dbref.refTypeAc = attributes.getValue(getREFTYPEACFLAGS());
                    dbref.secondary = attributes.getValue(getSECONDARYFLAGS());
                    dbref.version = attributes.getValue(getVERSIONFLAGS());
                }
            }
            if (depth == this.depth + 2) {
                if (dbref != null) {
                    if (qname.equals(dbref.attributeList.getATTRIBUTELISTFLAGS())) {
                        dbref.attributeList.setOpen(true);
                    }
                }
            }
            if (depth >= this.depth + 2) {
                if (dbref != null) {
                    if (dbref.attributeList.isOpen()) {
                        dbref.attributeList.startElement(qname, depth, attributes, dbref.WID);
                    }
                }
            }
        }
        if (qname.equals(getXREFFLAGS())) {
            this.depth = depth;
            open = true;


            this.MIFOtherWID = MIFOtherWID;

            dbref = null;
            dbrefs.clear();

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
            if (depth > this.depth + 1) {
                if (dbref.attributeList.isOpen()) {
                    dbref.attributeList.characters(tagname, qname, depth);
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

    private class DBReferenceType {

        private boolean open = false;
        private long WID = 0;
        private boolean primary = false;
        private String db = null;
        private String dbAc = null;
        private String id = null;
        private String secondary = null;
        private String version = null;
        private String refType = null;
        private String refTypeAc = null;
        private AttributeList attributeList = null;

        public DBReferenceType() {
            open = true;
            attributeList = new AttributeList();
        }
    }
}
