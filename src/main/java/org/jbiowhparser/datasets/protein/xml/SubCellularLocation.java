package org.jbiowhparser.datasets.protein.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.SubCellularLocationTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML SubCellularLocation tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 30, 2010
 * @see
 */
public class SubCellularLocation extends SubCellularLocationTags {

    private int depth = 0;
    private boolean open = false;
    private long CommentWID = 0;
    private EvidencedStringType location = null;
    private ArrayList<EvidencedStringType> locations = null;
    private EvidencedStringType topology = null;
    private ArrayList<EvidencedStringType> topologys = null;
    private EvidencedStringType orientation = null;
    private ArrayList<EvidencedStringType> orientations = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public SubCellularLocation() {
        open = false;

        locations = new ArrayList<>();
        topologys = new ArrayList<>();
        orientations = new ArrayList<>();
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
                if (qname.equals(getLOCATIONFLAGS())) {
                    location.setOpen(false);
                    locations.add(location);
                }
                if (qname.equals(getTOPOLOGYFLAGS())) {
                    topology.setOpen(false);
                    topologys.add(topology);
                }
                if (qname.equals(getORIENTATIONFLAGS())) {
                    orientation.setOpen(false);
                    orientations.add(orientation);
                }
            }
        }
        if (qname.equals(getSUBCELLULARLOCATIONFLAGS())) {
            open = false;

            if (!locations.isEmpty()) {
                for (Iterator<EvidencedStringType> it = locations.iterator(); it.hasNext();) {
                    location = it.next();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, WIDFactory.getInstance().getWid(), "\t");
                    WIDFactory.getInstance().increaseWid();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, CommentWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, location.getData(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, getLOCATIONFLAGS(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, location.getEvidence(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, location.getStatus(), "\n");
                }
                locations.clear();
            }
            if (!topologys.isEmpty()) {
                for (Iterator<EvidencedStringType> it = topologys.iterator(); it.hasNext();) {
                    topology = it.next();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, WIDFactory.getInstance().getWid(), "\t");
                    WIDFactory.getInstance().increaseWid();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, CommentWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, topology.getData(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, getTOPOLOGYFLAGS(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, topology.getEvidence(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, topology.getStatus(), "\n");
                }
                topologys.clear();
            }
            if (!orientations.isEmpty()) {
                for (Iterator<EvidencedStringType> it = orientations.iterator(); it.hasNext();) {
                    orientation = it.next();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, WIDFactory.getInstance().getWid(), "\t");
                    WIDFactory.getInstance().increaseWid();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, CommentWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, orientation.getData(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, getORIENTATIONFLAGS(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, orientation.getEvidence(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINCOMMENTSUBCELLULARLOCATION, orientation.getStatus(), "\n");
                }
                orientations.clear();
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
            if (depth == this.depth + 1) {
                if (qname.equals(getLOCATIONFLAGS())) {
                    location = new EvidencedStringType();
                    location.setOpen(true);
                    location.setEvidence(attributes.getValue(location.getEVIDENCE()));
                    location.setStatus(attributes.getValue(location.getSTATUS()));
                }
                if (qname.equals(getTOPOLOGYFLAGS())) {
                    topology = new EvidencedStringType();
                    topology.setOpen(true);
                    topology.setEvidence(attributes.getValue(topology.getEVIDENCE()));
                    topology.setStatus(attributes.getValue(topology.getSTATUS()));
                }
                if (qname.equals(getORIENTATIONFLAGS())) {
                    orientation = new EvidencedStringType();
                    orientation.setOpen(true);
                    topology.setEvidence(attributes.getValue(topology.getEVIDENCE()));
                    topology.setStatus(attributes.getValue(topology.getSTATUS()));
                }
            }
        }
        if (qname.equals(getSUBCELLULARLOCATIONFLAGS())) {
            this.depth = depth;
            this.CommentWID = WID;
            open = true;

            location = null;
            locations.clear();
            topology = null;
            topologys.clear();
            orientation = null;
            orientations.clear();
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
                if (tagname.equals(getLOCATIONFLAGS())) {
                    location.setData(qname);
                }
                if (tagname.equals(getTOPOLOGYFLAGS())) {
                    topology.setData(qname);
                }
                if (tagname.equals(getORIENTATIONFLAGS())) {
                    orientation.setData(qname);
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
