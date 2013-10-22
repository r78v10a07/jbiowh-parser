package org.jbiowhparser.datasets.protein.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.LocationTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Location tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 30, 2010
 * @see
 */
public class Location extends LocationTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long otherWID = 0;
    private String BeginPos = "-1";
    private String BeginStatus = null;
    private String EndPos = "-1";
    private String EndStatus = null;
    private String Position = "-1";
    private String PositionStatus = null;
    private String Sequence = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Location() {
        open = false;
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (qname.equals(getLOCATIONFLAGS())) {
            open = false;
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, otherWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, BeginPos, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, BeginStatus, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, EndPos, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, EndStatus, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, Position, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, PositionStatus, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, Sequence, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINOTHERLOCATION, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long otherWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getBEGINFLAGS())) {
                    BeginPos = attributes.getValue(getPOSITIONFLAGS());
                    BeginStatus = attributes.getValue(getSTATUSFLAGS());
                }
                if (qname.equals(getENDFLAGS())) {
                    EndPos = attributes.getValue(getPOSITIONFLAGS());
                    EndStatus = attributes.getValue(getSTATUSFLAGS());
                }
                if (qname.equals(getPOSITIONFLAGS())) {
                    Position = attributes.getValue(getPOSITIONFLAGS());
                    PositionStatus = attributes.getValue(getSTATUSFLAGS());
                }
            }
        }
        if (qname.equals(getLOCATIONFLAGS())) {
            this.depth = depth;
            this.otherWID = otherWID;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            BeginPos = "-1";
            BeginStatus = null;
            EndPos = "-1";
            EndStatus = null;
            Position = "-1";
            PositionStatus = null;
            Sequence = attributes.getValue(getSEQUENCEFLAGS());
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
