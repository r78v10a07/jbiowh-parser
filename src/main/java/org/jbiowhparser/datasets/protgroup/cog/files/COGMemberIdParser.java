package org.jbiowhparser.datasets.protgroup.cog.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.protgroup.cog.COGTables;

/**
 * This class is to parse the COG myva=gb file
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 19, 2013
 */
public class COGMemberIdParser {

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
                            if (fields.length == 2) {
                                ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_HAS_GI, fields[0], "\t");
                                ParseFiles.getInstance().printOnTSVFile(COGTables.getInstance().COGMEMBER_HAS_GI, fields[1], "\n");
                            }
                        }
                    } catch (IllegalStateException ex) {
                        VerbLogger.getInstance().log(this.getClass(), "Problem on line: " + line);
                        ex.printStackTrace(System.err);
                    }
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            }
        }
    }
}
