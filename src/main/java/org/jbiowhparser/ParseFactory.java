package org.jbiowhparser;

import java.sql.SQLException;

/**
 * This interface is the Parse Factory to parse any data source
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Jun 18, 2011
 */
public interface ParseFactory {

    /**
     * Run the desired parser
     * @throws SQLException 
     */
    public void runLoader() throws SQLException;
}
