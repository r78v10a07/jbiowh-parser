package org.jbiowhparser.datasets.ontology;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.ontology.links.OntologyLinks;
import org.jbiowhparser.datasets.ontology.xml.GO;
import org.jbiowhparser.datasets.ontology.xml.GOOntologyDefaultHandler;
import org.jbiowhparser.utils.FTPFacade;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ontology.OntologyTables;
import org.xml.sax.SAXException;

/**
 * This Class is the Ontology parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Jun 27, 2011
 */
public class OntologyParser extends ParserFactory implements JBioWHParser {

    /**
     * Run the Taxonomy Parser
     *
     * @throws java.sql.SQLException
     */
    @Override
    public void runLoader() throws SQLException {
        SAXParser saxParser;

        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(OntologyTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        GO go = new GO();

        File dir = null;
        if (!DataSetPersistence.getInstance().isonlineFTP()) {
            dir = new File(DataSetPersistence.getInstance().getDirectory());
        }

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : OntologyTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        } else {
            dropTemporalTables();
        }

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            if (dir != null && dir.isDirectory()) {
                List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir, new String[]{"xml", ".xml.gz"});
                for (File file : files) {

                    saxParser = factory.newSAXParser();
                    if (file.isFile()) {
                        if (file.getAbsolutePath().endsWith(".gz")) {
                            saxParser.parse(new GZIPInputStream(new FileInputStream(file)), new GOOntologyDefaultHandler(go));
                        } else {
                            saxParser.parse(file, new GOOntologyDefaultHandler(go));
                        }
                    }
                }
            } else {
                saxParser = factory.newSAXParser();

                InputStream in;
                FTPFacade ftp = null;

                if (!DataSetPersistence.getInstance().isonlineFTP()) {
                    in = new FileInputStream(DataSetPersistence.getInstance().getDirectory());
                } else {
                    ftp = new FTPFacade(DataSetPersistence.getInstance().getOnlineSite(),
                            DataSetPersistence.getInstance().getOnlineUser(),
                            DataSetPersistence.getInstance().getOnlinePasswd());

                    in = ftp.getFtpClient().retrieveFileStream(DataSetPersistence.getInstance().getOnlinePath());
                }

                if (DataSetPersistence.getInstance().getDirectory().endsWith(".gz")) {
                    in = new GZIPInputStream(in);
                }
                saxParser.parse(in, new GOOntologyDefaultHandler(go));

                in.close();
                if (ftp != null) {
                    ftp.close();
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
            DataSetPersistence.getInstance().getDataset().setStatus("Error");
            DataSetPersistence.getInstance().updateDataSet();
            WIDFactory.getInstance().updateWIDTable();
            System.exit(-1);
        }

        ParseFiles.getInstance().closeAllPrintWriter();

        whdbmsFactory.loadTSVTables(OntologyTables.getInstance().getTables());

        VerbLogger.getInstance().log(this.getClass(), "Running SQL internal processing");

        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGY_HAS_ONTOLOGYSUBSET
                + " (Ontology_WID,OntologySubset_WID) "
                + "select os.OntologyWID,s.WID from "
                + OntologyTables.getInstance().ONTOLOGYWIDONTOLOGYSUBSET
                + " os inner join "
                + OntologyTables.getInstance().ONTOLOGYSUBSET
                + " s on s.Id = os.Subset");

        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGYRELATION
                + " (Ontology_WID,OtherOntology_WID,Type) "
                + "select ort.OntologyWID,o.WID,ort.Type from "
                + OntologyTables.getInstance().ONTOLOGYWIDRELATIONTEMP
                + " ort inner join "
                + OntologyTables.getInstance().ONTOLOGY
                + " o on o.Id = ort.OtherId");

        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGYISA
                + " (Ontology_WID,IsAOntology_WID) "
                + "select oi.OntologyWID,o.WID from "
                + OntologyTables.getInstance().ONTOLOGYWIDISATEMP
                + " oi inner join "
                + OntologyTables.getInstance().ONTOLOGY
                + " o on o.Id = oi.isA");

        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGYTOCONSIDER
                + " (Ontology_WID,ToConsiderOntology_WID) "
                + "select oi.OntologyWID,o.WID from "
                + OntologyTables.getInstance().ONTOLOGYWIDTOCONSIDERTEMP
                + " oi inner join "
                + OntologyTables.getInstance().ONTOLOGY
                + " o on o.Id = oi.OtherId");

        whdbmsFactory.executeUpdate("ALTER TABLE OntologySynonym AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());
        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGYSYNONYM
                + " (Synonym) "
                + "select Synonym"
                + " from "
                + OntologyTables.getInstance().ONTOLOGYWIDSYNONYMTEMP
                + " group by Synonym");

        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGY_HAS_ONTOLOGYSYNONYM
                + " (Ontology_WID,OntologySynonym_WID,Scope) "
                + "select st.OntologyWID,s.WID,st.Scope from "
                + OntologyTables.getInstance().ONTOLOGYWIDSYNONYMTEMP
                + " st "
                + "inner join "
                + OntologyTables.getInstance().ONTOLOGYSYNONYM
                + " s on s.Synonym = st.Synonym group by st.OntologyWID,s.WID,st.Scope");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + OntologyTables.getInstance().ONTOLOGYSYNONYM, "WID"));

        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGYPMID
                + " (Ontology_WID,PMID) "
                + "select OntologyWID,ACC"
                + " from "
                + OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP
                + " where DBName = 'PMID' group by OntologyWID,ACC");

        whdbmsFactory.executeUpdate("ALTER TABLE "
                + OntologyTables.getInstance().ONTOLOGYXREF
                + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGYXREF
                + " (ACC,DBName,Name,Type) "
                + "select ACC,DBName,Name,Type"
                + " from "
                + OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP
                + " where DBName != 'PMID' group by ACC,DBName,Type");

        whdbmsFactory.executeUpdate("insert into "
                + OntologyTables.getInstance().ONTOLOGY_HAS_ONTOLOGYXREF
                + " (Ontology_WID,OntologyXRef_WID) "
                + "select OntologyWID,WID from "
                + OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP
                + " t "
                + "inner join "
                + OntologyTables.getInstance().ONTOLOGYXREF
                + " r on r.ACC = t.ACC and r.DBName = t.DBName and r.Type = t.Type group by OntologyWID,WID");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + OntologyTables.getInstance().ONTOLOGYXREF, "WID"));

        if (DataSetPersistence.getInstance().isRunlinks()) {
            OntologyLinks.getInstance().runLink();
        }

        dropTemporalTables();

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
    }

    private void dropTemporalTables() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        for (String table : OntologyTables.getInstance().getTables()) {
            if (table.endsWith("Temp")
                    || table.equals(OntologyTables.getInstance().ONTOLOGYWIDONTOLOGYSUBSET)) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(OntologyTables.getInstance().getTables());
    }
}
