package org.jbiowhparser.datasets.protgroup.pirsf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhparser.ParseFactory;
import org.jbiowhparser.ParserBasic;
import org.jbiowhparser.datasets.protgroup.pirsf.links.PirsfLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.pirsf.PirsfTables;

/**
 * This class is the PIRSF database parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 11, 2013
 */
public class PirsfParser extends ParserBasic implements ParseFactory {

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : PirsfTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }

        if (dir.isDirectory()) {
            try {
                InputStream in;
                File file = new File(dir.getCanonicalPath() + "/" + PirsfTables.getInstance().PIRSF_FILE);
                VerbLogger.getInstance().log(this.getClass(), "Parsing file: " + file.getName());
                if (file.exists() && file.isFile()) {
                    if (file.getCanonicalPath().endsWith(".gz")) {
                        in = new GZIPInputStream(new FileInputStream(file));
                    } else {
                        in = new FileInputStream(file);
                    }
                    ParseFiles.getInstance().start(PirsfTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());

                    String line = null;
                    Long WID = null;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                        try {
                            while ((line = reader.readLine()) != null) {
                                if (line.startsWith(">")) {
                                    Pattern p;
                                    if (line.contains("[Parent=")) {
                                        p = Pattern.compile("(\\w+) \\((\\w+\\p{Punct}*\\w+\\p{Punct}*)\\)(.+)?\\[Parent=(\\w+)\\]");
                                    } else {
                                        p = Pattern.compile("(\\w+) \\((\\w+\\p{Punct}*\\w+\\p{Punct}*)\\)(.+)?");
                                    }
                                    Matcher m = p.matcher(line);

                                    m.find();

                                    WID = WIDFactory.getInstance().getWid();
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.getInstance().PIRSF, WID, "\t");
                                    WIDFactory.getInstance().increaseWid();
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.getInstance().PIRSF, m.group(1), "\t");
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.getInstance().PIRSF, m.group(2), "\t");
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.getInstance().PIRSF, m.group(3), "\t");
                                    if (m.groupCount() == 4) {
                                        ParseFiles.getInstance().printOnTSVFile(PirsfTables.getInstance().PIRSF, m.group(4), "\t");
                                    } else {
                                        ParseFiles.getInstance().printOnTSVFile(PirsfTables.getInstance().PIRSF, (String) null, "\t");
                                    }
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.getInstance().PIRSF, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
                                } else {
                                    Pattern p = Pattern.compile("(\\w+) (\\d) (\\d)");
                                    Matcher m = p.matcher(line);
                                    m.find();
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.PIRSFPROTEIN, WID, "\t");
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.PIRSFPROTEIN, m.group(1), "\t");
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.PIRSFPROTEIN, m.group(2), "\t");
                                    ParseFiles.getInstance().printOnTSVFile(PirsfTables.PIRSFPROTEIN, m.group(3), "\n");
                                }
                            }
                        } catch (IllegalStateException ex) {
                            VerbLogger.getInstance().log(this.getClass(), "Problem on line: " + line);
                            ex.printStackTrace(System.err);
                        }
                    }

                    ParseFiles.getInstance().closeAllPrintWriter();
                    whdbmsFactory.loadTSVTables(PirsfTables.getInstance().getTables());
                    ParseFiles.getInstance().end();

                    if (DataSetPersistence.getInstance().isRunlinks()) {
                        PirsfLinks.getInstance().runLink();
                    }
                } else {
                    VerbLogger.getInstance().log(this.getClass(), "Can find the file " + PirsfTables.getInstance().PIRSF_FILE + " on directory " + dir.getCanonicalPath());
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            }
        } else {
            VerbLogger.getInstance().log(this.getClass(), "The config XML tag <directory> has to point to the DB directory");
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(PirsfTables.getInstance().getTables());
    }

}
