package org.jbiowhparser;

import java.sql.SQLException;
import java.util.List;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;

/**
 * This class is
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Oct 25, 2013
 */
public class ParserBasic {

    public void clean(List<String> tables) throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        for (String table : tables) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
        }
    }
}
