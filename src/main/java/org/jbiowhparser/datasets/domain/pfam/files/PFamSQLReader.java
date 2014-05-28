package org.jbiowhparser.datasets.domain.pfam.files;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.fileformats.sql.JBioWHSQLParser;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.mysql.WHMySQL;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.domain.pfam.PFamTables;

/**
 * This class read the Pfam SQL script and execute it
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Oct 31, 2012
 */
public class PFamSQLReader {

    public void loadSQLScript(JBioWHDBMS wHDBMSFactory) {
        if (wHDBMSFactory instanceof WHMySQL) {
            InputStream inp = ClassLoader.getSystemClassLoader().getResourceAsStream(PFamTables.getInstance().MySQLSCRIPT);
            List<String> lineList = JBioWHSQLParser.getInstance().getAllSQL(inp);

            for (String line : lineList) {
                try {
                    if (line.toUpperCase().startsWith("SELECT")) {
                        List result = wHDBMSFactory.execute(line);
                        if (!result.isEmpty()) {
                            if (result.size() == 2) {
                                if (((String) ((List) result.get(0)).get(0)).contains("@WID:=")
                                        && !line.contains("WIDTable")) {
                                    Long wid = (Long) ((List) result.get(1)).get(0);
                                    if (wid != null) {
                                        WIDFactory.getInstance().setWid(wid);
                                        WIDFactory.getInstance().updateWIDTable();
                                    }
                                }
                            }
                        }
                    } else {
                        wHDBMSFactory.executeUpdate(line);
                    }
                } catch (SQLException ex) {
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                    System.exit(-1);
                }
            }
        }
    }
}
