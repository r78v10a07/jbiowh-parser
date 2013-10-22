package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the PropertyType XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class PropertyTypeTags {

    public final String PROPERTY = "property";
    public final String KIND = "kind";
    public final String VALUE = "value";
    public final String SOURCE = "source";
    private static PropertyTypeTags singleton;

    private PropertyTypeTags() {
    }

    /**
     * Return a PropertyTypeTags
     *
     * @return a PropertyTypeTags
     */
    public static synchronized PropertyTypeTags getInstance() {
        if (singleton == null) {
            singleton = new PropertyTypeTags();
        }
        return singleton;
    }
}
