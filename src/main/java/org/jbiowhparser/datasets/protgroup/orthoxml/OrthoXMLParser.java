package org.jbiowhparser.datasets.protgroup.orthoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.fileformats.orthoxml.Group;
import org.jbiowhcore.utility.fileformats.orthoxml.Groups;
import org.jbiowhcore.utility.fileformats.orthoxml.Scores;
import org.jbiowhcore.utility.fileformats.orthoxml.Species;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhparser.ParseFactory;
import org.jbiowhparser.ParserBasic;
import org.jbiowhparser.datasets.protgroup.orthoxml.files.OrthoXMLPrintOnTSVFile;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.orthoxml.OrthoXMLTables;

/**
 * This class is the OrthoXML database parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Jan 8, 2014
 */
public class OrthoXMLParser extends ParserBasic implements ParseFactory {

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : OrthoXMLTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }

        if (dir.isDirectory()) {
            try {
                List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{"xml", ".xml.gz"});
                ParseFiles.getInstance().start(OrthoXMLTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
                for (File file : files) {
                    InputStream in;
                    if (file.exists() && file.isFile()) {
                        if (file.getCanonicalPath().endsWith(".gz")) {
                            in = new GZIPInputStream(new FileInputStream(file));
                        } else {
                            in = new FileInputStream(file);
                        }
                        try {
                            VerbLogger.getInstance().log(this.getClass(), "Parsing OrthoXML file: " + file);
                            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(in);
                            Unmarshaller unmarshaller = JAXBContext.newInstance(Species.class, Groups.class, Scores.class).createUnmarshaller();

                            long WID = 0L;
                            while (reader.hasNext()) {
                                reader.next();
                                if (reader.isStartElement()) {
                                    if (reader.getName().getLocalPart().equals("orthoXML")) {
                                        WID = OrthoXMLPrintOnTSVFile.getInstance().printOrthoXMLOnTSVFile(
                                                reader.getAttributeValue(null, "origin"),
                                                reader.getAttributeValue(null, "originVersion"),
                                                reader.getAttributeValue(null, "version"));
                                    } else if (reader.getName().getLocalPart().equals(Species.class.getSimpleName().toLowerCase())) {
                                        Species s = unmarshaller.unmarshal(reader, Species.class).getValue();
                                        OrthoXMLPrintOnTSVFile.getInstance().printSpeciesOnTSVFile(WID, s);
                                    } else if (reader.getName().getLocalPart().equals(Scores.class.getSimpleName().toLowerCase())) {
                                        Scores s = unmarshaller.unmarshal(reader, Scores.class).getValue();
                                        OrthoXMLPrintOnTSVFile.getInstance().printScoresOnTSVFile(WID, s);
                                    } else if (reader.getName().getLocalPart().equals("orthologGroup")) {
                                        Group s = unmarshaller.unmarshal(reader, Group.class).getValue();
                                        OrthoXMLPrintOnTSVFile.getInstance().printGroupOnTSVFile(WID, s, 1);
                                    } else if (reader.getName().getLocalPart().equals("paralogGroup")) {
                                        Group s = unmarshaller.unmarshal(reader, Group.class).getValue();
                                        OrthoXMLPrintOnTSVFile.getInstance().printGroupOnTSVFile(WID, s, 0);
                                    }
                                }
                            }

                            reader.close();
                            in.close();
                        } catch (JAXBException | XMLStreamException ex) {
                            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                        }
                    }
                }
                ParseFiles.getInstance().closeAllPrintWriter();
                whdbmsFactory.loadTSVTables(OrthoXMLTables.getInstance().getTables());
                ParseFiles.getInstance().end();

            } catch (IOException ex) {
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            }
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(OrthoXMLTables.getInstance().getTables());
    }

}
