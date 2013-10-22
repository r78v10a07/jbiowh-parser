package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Groups XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 10, 2011
 */
public class GroupsTags {

    private static GroupsTags groupsTags;
    public final String GROUPS = "groups";
    public final String GROUP = "group";

    private GroupsTags() {
    }

    /**
     * Return a GroupsTags
     *
     * @return a GroupsTags
     */
    public static synchronized GroupsTags getInstance() {
        if (groupsTags == null) {
            groupsTags = new GroupsTags();
        }
        return groupsTags;
    }
}
