package org.jbiowhparser.datasets.disease.omim;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
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
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Jul 12, 2012
 */
public class OMIMParser extends ParserFactory implements JBioWHParser {

    final static int BUFFER = 2048;

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        ParseFiles.getInstance().start(OMIMTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());

        downloadFTPdata(null, 2);

        if (delete) {
            try {
                try (ZCompressorInputStream zFile = new ZCompressorInputStream(new FileInputStream(DataSetPersistence.getInstance().getTempdir() + "omim.txt.Z"))) {
                    int count;
                    byte data[] = new byte[BUFFER];
                    try (FileOutputStream fos = new FileOutputStream(DataSetPersistence.getInstance().getTempdir() + "omim.txt"); BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)) {
                        while ((count = zFile.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, count);
                        }
                    }
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                DataSetPersistence.getInstance().getDataset().setStatus("Error");
                DataSetPersistence.getInstance().updateDataSet();
                WIDFactory.getInstance().updateWIDTable();
                ex.printStackTrace(System.err);
                System.exit(-1);
            }
        }

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
        cleanTmpDir();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(OMIMTables.getInstance().getTables());
    }
}
