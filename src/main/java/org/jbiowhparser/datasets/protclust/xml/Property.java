package org.jbiowhparser.datasets.protclust.xml;

/**
 * This Class is the Property XMl parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Aug 19, 2011
 */
public class Property {

    private String type = null;
    private String value = null;

    public Property(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
