package org.jbiowhparser.datasets.pathway.kegg.utility;

import java.util.HashMap;
import java.util.Set;

/**
 * This interface is the KEGG Parser Factory
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 29, 2011
 */
public interface KEGGParserFactory {
    /*
     * Format the desired parser
     */

    public void format();

    /*
     * Print TSV format the desired parser
     */
    public void printOnTSVFile();

    /*
     * Get the field data
     */
    public HashMap<String, Object> getFieldValue();

    /*
     * Return the fields read
     */
    public Set<String> getFields();
}
