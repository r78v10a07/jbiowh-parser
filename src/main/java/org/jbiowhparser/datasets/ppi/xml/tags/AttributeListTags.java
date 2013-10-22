package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML AttributeList Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class AttributeListTags {

    private final String ATTRIBUTELISTFLAGS = "attributeList";
    private final String ATTRIBUTEFLAGS = "attribute";
    private final String NAMEFLAGS = "name";
    private final String NAMEACFLAGS = "nameAc";

    public String getATTRIBUTEFLAGS() {
        return ATTRIBUTEFLAGS;
    }

    public String getATTRIBUTELISTFLAGS() {
        return ATTRIBUTELISTFLAGS;
    }

    public String getNAMEACFLAGS() {
        return NAMEACFLAGS;
    }

    public String getNAMEFLAGS() {
        return NAMEFLAGS;
    }
}
