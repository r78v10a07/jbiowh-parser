package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhparser.datasets.disease.omim.exception.OMIMFormatException;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.disease.omim.OMIMTables;

/**
 * This Class is the parser for the OMIM morbidmap file
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
 * @since Jul 13, 2012
 */
public class MorbidMapParser {

    /**
     * This method load the data in morbidmap file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        try {
            WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

            long WID = WIDFactory.getInstance().getWid();
            try (BufferedReader reader = new BufferedReader(new FileReader(new File(DataSetPersistence.getInstance().getDirectory() + OMIMTables.getInstance().MORBIDMAPFILE)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split("\\|");
                    if (fields.length != 4) {
                        throw new OMIMFormatException("Bad format on line: " + line);
                    }

                    String mim = null;
                    Matcher m = Pattern.compile("\\d{6} \\(\\d+\\)").matcher(fields[0].trim());
                    if (m.find()) {
                        String mimTemp = m.group();
                        Matcher m2 = Pattern.compile("\\d{6}\\s").matcher(mimTemp);
                        if (m2.find()) {
                            mim = m2.group().trim();
                        }
                    }

                    ParseFiles.getInstance().printOnTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP, mim, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP, fields[0].trim(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP, fields[2].trim(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP, fields[3].trim(), "\n");

                    String[] geneSymbol = fields[1].split(",");
                    for (int i = 0; i < geneSymbol.length; i++) {
                        ParseFiles.getInstance().printOnTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP, WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP, geneSymbol[i].trim(), "\n");
                    }
                    WID++;
                }
            }

            VerbLogger.getInstance().log(this.getClass(), "Inserting the " + OMIMTables.getInstance().OMIMMORBIDMAP + " file");
            ParseFiles.getInstance().getPrintWriterFromName(OMIMTables.getInstance().OMIMMORBIDMAP).close();
            whdbmsFactory.loadTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP, ParseFiles.getInstance().getFileAbsolutName(OMIMTables.getInstance().OMIMMORBIDMAP));

            VerbLogger.getInstance().log(this.getClass(), "Inserting the " + OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP + " file");
            ParseFiles.getInstance().getPrintWriterFromName(OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP).close();
            whdbmsFactory.loadTSVFile(OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP, ParseFiles.getInstance().getFileAbsolutName(OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP));

            whdbmsFactory.executeUpdate("INSERT INTO "
                    + OMIMTables.OMIMMORBIDMAP_HAS_GENESYMBOL
                    + " (OMIMMorbidMap_WID,GeneSymbol) "
                    + " select OMIMMorbidMap_WID,GeneSymbol from "
                    + OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP
                    + " group by OMIMMorbidMap_WID,GeneSymbol");

            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + OMIMTables.getInstance().OMIMMORBIDMAP_HAS_GENESYMBOLTEMP);

            WIDFactory.getInstance().setWid(WID);

        } catch (OMIMFormatException | IOException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            VerbLogger.getInstance().log(this.getClass(), "Error: " + ex.toString());
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
            System.exit(1);
        }

    }
}
