package org.jbiowhparser.datasets.disease.omim;

import java.sql.SQLException;
import java.util.Date;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhparser.ParseFactory;
import org.jbiowhparser.datasets.disease.omim.files.GeneMapParser;
import org.jbiowhparser.datasets.disease.omim.files.MorbidMapParser;
import org.jbiowhparser.datasets.disease.omim.files.OMIMTXTParser;
import org.jbiowhparser.datasets.disease.omim.links.OMIMLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.disease.omim.OMIMTables;

/**
 * This Class is the OMIM parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
 * @since Jul 12, 2012
 */
public class OMIMParser implements ParseFactory {

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        ParseFiles.getInstance().start(OMIMTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : OMIMTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }

        (new GeneMapParser()).loader();
        (new MorbidMapParser()).loader();
        (new OMIMTXTParser()).loader();

        whdbmsFactory.executeUpdate("INSERT INTO "
                + OMIMTables.OMIMGENEMAP_HAS_OMIMMORBIDMAP
                + "(OMIMGeneMap_WID,OMIMMorbidMap_WID) "
                + " select ot.WID,o.WID from "
                + OMIMTables.getInstance().OMIMGENEMAP + " ot inner join "
                + OMIMTables.getInstance().OMIMMORBIDMAP + " o on ot.MIMNumber = o.MIMNumber ");

        if (DataSetPersistence.getInstance().isRunlinks()) {
            OMIMLinks.getInstance().runLink();
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
    }
}
