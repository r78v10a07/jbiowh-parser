package org.jbiowhparser.datasets.domain.pfam;

import java.sql.SQLException;
import java.util.Date;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhparser.ParseFactory;
import org.jbiowhparser.ParserBasic;
import org.jbiowhparser.datasets.domain.pfam.files.PFamSQLfiles;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.domain.pfam.PFamTables;

/**
 * This class is the Pfam Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Oct 24, 2012
 */
public class PFamParser extends ParserBasic implements ParseFactory {

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(PFamTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : PFamTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        } else {
            dropTemporalTables();
        }

        PFamSQLfiles pfam = new PFamSQLfiles();
        pfam.loadData();

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
    }

    private void dropTemporalTables() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        for (String table : PFamTables.getInstance().getTables()) {
            if (table.endsWith("Temp")) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(PFamTables.getInstance().getTables());
    }
}
