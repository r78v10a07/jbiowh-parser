package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Confidence Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class ConfidenceTags {

    private final String CONFIDENCEFLAGS = "confidence";
    private final String UNITFLAGS = "unit";
    private final String VALUEFLAGS = "value";

    public String getCONFIDENCEFLAGS() {
        return CONFIDENCEFLAGS;
    }

    public String getUNITFLAGS() {
        return UNITFLAGS;
    }

    public String getVALUEFLAGS() {
        return VALUEFLAGS;
    }
}
