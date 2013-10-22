package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML SubCellularLocation tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class SubCellularLocationTags {

    private final String SUBCELLULARLOCATIONTYPEFLAGS = "subcellularLocationType";
    private final String LOCATIONFLAGS = "location";
    private final String TOPOLOGYFLAGS = "topology";
    private final String ORIENTATIONFLAGS = "orientation";
    private final String SUBCELLULARLOCATIONFLAGS = "subcellularLocation";

    public String getLOCATIONFLAGS() {
        return LOCATIONFLAGS;
    }

    public String getORIENTATIONFLAGS() {
        return ORIENTATIONFLAGS;
    }

    public String getSUBCELLULARLOCATIONFLAGS() {
        return SUBCELLULARLOCATIONFLAGS;
    }

    public String getSUBCELLULARLOCATIONTYPEFLAGS() {
        return SUBCELLULARLOCATIONTYPEFLAGS;
    }

    public String getTOPOLOGYFLAGS() {
        return TOPOLOGYFLAGS;
    }
}
