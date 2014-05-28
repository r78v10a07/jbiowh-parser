package org.jbiowhparser.datasets.protgroup.cog.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.cog.COGTables;

/**
 * This class is to parser the COG org.txt file
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 19, 2013
 */
public class COGOrgParser {

    public void run(String fileName) {
        File file = new File(DataSetPersistence.getInstance().getDirectory() + "/" + fileName);
        InputStream in;

        VerbLogger.getInstance().log(this.getClass(), "Parsing file: " + file.getName());
        if (file.exists() && file.isFile()) {
            try {
                if (file.getCanonicalPath().endsWith(".gz")) {
                    in = new GZIPInputStream(new FileInputStream(file));
                } else {
                    in = new FileInputStream(file);
                }
                String line = null;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    try {
                        while ((line = reader.readLine()) != null) {
                            String[] fields = line.trim().split(" +");
                            if (fields.length < 2) {
                                throw new IllegalStateException("Bad line in file " + fileName + "\n" + line);
                            }
                            ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, fields[0], "\t");
                            ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, fields[1], "\n");
                        }
                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, "ath", "\t");
                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, 3702, "\n");

                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, "cel", "\t");
                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, 6239, "\n");

                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, "dme", "\t");
                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, 7227, "\n");

                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, "hsa", "\t");
                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, 9606, "\n");

                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, "sce", "\t");
                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, 4932, "\n");

                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, "spo", "\t");
                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, 4896, "\n");

                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, "ecu", "\t");
                        ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_TAXID, 6035, "\n");
                    } catch (IllegalStateException ex) {
                        VerbLogger.getInstance().log(this.getClass(), "Problem on line: " + line);
                        VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                        VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                        DataSetPersistence.getInstance().getDataset().setStatus("Error");
                        DataSetPersistence.getInstance().updateDataSet();
                        WIDFactory.getInstance().updateWIDTable();
                        System.exit(-1);
                    }
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                DataSetPersistence.getInstance().getDataset().setStatus("Error");
                DataSetPersistence.getInstance().updateDataSet();
                WIDFactory.getInstance().updateWIDTable();
                System.exit(-1);
            }
        }
    }
}
