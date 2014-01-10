package org.jbiowhparser.datasets.pathway.kegg;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhparser.ParseFactory;
import org.jbiowhparser.ParserBasic;
import org.jbiowhparser.datasets.pathway.kegg.kgml.PathwayPrintOnTSVFile;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Pathway;
import org.jbiowhparser.datasets.pathway.kegg.links.KEGGLinks;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserFactory;
import org.jbiowhparser.datasets.pathway.kegg.utility.KEGGParserFile;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This class is the KEGG Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 591 $
 *
 * @since Oct 30, 2011
 */
public class KEGGParser extends ParserBasic implements ParseFactory {

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(KEGGTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : KEGGTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }

        if (dir.isDirectory()) {
            KEGGParserFile parser;
            ArrayList<String> keggDirs = new ArrayList<>();
            keggDirs.add("gene");
            keggDirs.add("kgml");
            keggDirs.add("ligand");
            ArrayList<String> ligandDir = new ArrayList<>();
            ligandDir.add("enzyme");
            ligandDir.add("reaction");
            ligandDir.add("compound");
            ligandDir.add("glycan");
            ligandDir.add("rpair");

            for (String value : keggDirs) {
                switch (value) {
                    case "ligand": {
                        for (String inValue : ligandDir) {
                            try {
                                parser = new KEGGParserFile(inValue, DataSetPersistence.getInstance().getDirectory() + "ligand/" + inValue);
                                runKEGGParserFactory(parser);
                            } catch (FileNotFoundException ex) {
                                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                                System.exit(1);
                            }
                        }
                        break;
                    }
                    case "gene": {
                        File dirGene = new File(DataSetPersistence.getInstance().getDirectory() + "gene/");
                        if (dirGene.isDirectory()) {
                            List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dirGene);
                            for (File file : files) {
                                try {
                                    parser = new KEGGParserFile("gene", file);
                                    runKEGGParserFactory(parser);
                                } catch (FileNotFoundException ex) {
                                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                                }
                            }
                        }
                        break;
                    }
                    case "kgml": {
                        File dirKGML = new File(DataSetPersistence.getInstance().getDirectory() + "kgml/");
                        if (dirKGML.isDirectory()) {
                            List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dirKGML, new String[]{"xml"});
                            for (File file : files) {
                                try {
                                    VerbLogger.getInstance().log(this.getClass(), "Parsing KGML file: " + file);
                                    JAXBContext jc = JAXBContext.newInstance("org.jbiowhparser.datasets.pathway.kegg.kgml.xml");
                                    Unmarshaller unmarshaller = jc.createUnmarshaller();

                                    Pathway pathway = (Pathway) unmarshaller.unmarshal(file);
                                    if (pathway != null) {
                                        PathwayPrintOnTSVFile.getInstance().printPathwayOnTSVFile(pathway);
                                    }
                                } catch (JAXBException ex) {
                                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }

        ParseFiles.getInstance().closeAllPrintWriter();
        whdbmsFactory.loadTSVTables(KEGGTables.getInstance().getTables());

        sqlRelationshipReaction();
        sqlRelationshipEnzyme();
        sqlRelationshipGlycan();
        sqlRelationshipRPair();
        sqlRelationshipGene();
        sqlRelationshipCompound();

        if (DataSetPersistence.getInstance().isRunlinks()) {
            KEGGLinks.getInstance().runLink();
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
    }

    private void runKEGGParserFactory(KEGGParserFile parser) {
        KEGGParserFactory entry;
        VerbLogger.getInstance().log(this.getClass(), "Parsing KEGG file: " + parser.getKeggFileName());
        int count = 0;
        while ((entry = parser.readKEGGEntryFile()) != null) {
            entry.format();
            entry.printOnTSVFile();
            count++;
        }
    }

    private void sqlRelationshipReaction() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Creating Reaction relationships");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGREACTION_HAS_KEGGENZYME
                + " (KEGGReaction_WID,KEGGEnzyme_WID) "
                + "select r.KEGGReaction_WID,e.WID from "
                + KEGGTables.getInstance().KEGGREACTIONENZYME
                + " r inner join "
                + KEGGTables.getInstance().KEGGENZYME
                + " e on r.Enzyme = e.Entry");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGREACTION_HAS_KEGGCOMPOUND_AS_SUBSTRATE
                + " (KEGGReaction_WID,KEGGCompound_WID) "
                + "select s.KEGGReaction_WID,c.WID from "
                + KEGGTables.getInstance().KEGGREACTIONSUBSTRATE
                + " s inner join "
                + KEGGTables.getInstance().KEGGCOMPOUND
                + " c on s.Entry = c.Entry group by s.KEGGReaction_WID,c.WID");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGREACTION_HAS_KEGGCOMPOUND_AS_PRODUCT
                + " (KEGGReaction_WID,KEGGCompound_WID) "
                + "select s.KEGGReaction_WID,c.WID from "
                + KEGGTables.getInstance().KEGGREACTIONPRODUCT
                + " s inner join "
                + KEGGTables.getInstance().KEGGCOMPOUND
                + " c on s.Entry = c.Entry group by s.KEGGReaction_WID,c.WID");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGREACTION_HAS_KEGGGLYCAN_AS_SUBSTRATE
                + " (KEGGReaction_WID,KEGGGlycan_WID) "
                + "select s.KEGGReaction_WID,c.WID from "
                + KEGGTables.getInstance().KEGGREACTIONSUBSTRATE
                + " s inner join "
                + KEGGTables.getInstance().KEGGGLYCAN
                + " c on s.Entry = c.Entry group by s.KEGGReaction_WID,c.WID");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGREACTION_HAS_KEGGGLYCAN_AS_PRODUCT
                + " (KEGGReaction_WID,KEGGGlycan_WID) "
                + "select s.KEGGReaction_WID,c.WID from "
                + KEGGTables.getInstance().KEGGREACTIONPRODUCT
                + " s inner join "
                + KEGGTables.getInstance().KEGGGLYCAN
                + " c on s.Entry = c.Entry group by s.KEGGReaction_WID,c.WID");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGREACTION_HAS_KEGGPATHWAY
                + " (KEGGReaction_WID,KEGGPathway_WID) "
                + "select g.WID,p.KEGGPathway_WID from "
                + KEGGTables.getInstance().KEGGREACTION
                + " g inner join "
                + KEGGTables.getInstance().KEGGPATHWAYENTRYREACTION
                + " p on p.Entry = g.Entry group by g.WID,p.KEGGPathway_WID");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.getInstance().KEGGPATHWAYENTRYREACTION);
    }

    private void sqlRelationshipEnzyme() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Creating Enzyme relationships");

        whdbmsFactory.executeUpdate("ALTER TABLE " + KEGGTables.getInstance().KEGGENZYMECLASS + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());
        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.getInstance().KEGGENZYMECLASS
                + " (Class) "
                + "select Class from "
                + KEGGTables.getInstance().KEGGENZYMECLASSTEMP
                + " group by Class");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + KEGGTables.getInstance().KEGGENZYMECLASS, "WID"));

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGENZYME_HAS_KEGGENZYMECLASS
                + " (KEGGEnzyme_WID,KEGGEnzymeClass_WID) "
                + "select c.KEGGEnzyme_WID,e.WID from "
                + KEGGTables.getInstance().KEGGENZYMECLASS
                + " e inner join "
                + KEGGTables.getInstance().KEGGENZYMECLASSTEMP
                + " c on e.Class = c.Class group by c.KEGGEnzyme_WID,e.WID");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.getInstance().KEGGENZYMECLASSTEMP);

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGENZYME_HAS_KEGGCOMPOUND_AS_COFACTOR
                + " (KEGGEnzyme_WID,KEGGCompound_WID) "
                + "select e.KEGGEnzyme_WID,c.WID from "
                + KEGGTables.getInstance().KEGGENZYMECOFACTOR
                + " e inner join "
                + KEGGTables.getInstance().KEGGCOMPOUND
                + " c on c.Entry = e.Entry");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGENZYME_HAS_KEGGCOMPOUND_AS_INHIBITOR
                + " (KEGGEnzyme_WID,KEGGCompound_WID) "
                + "select e.KEGGEnzyme_WID,c.WID from "
                + KEGGTables.getInstance().KEGGENZYMEINHIBITOR
                + " e inner join "
                + KEGGTables.getInstance().KEGGCOMPOUND
                + " c on c.Entry = e.Entry");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGENZYME_HAS_KEGGCOMPOUND_AS_EFFECTOR
                + " (KEGGEnzyme_WID,KEGGCompound_WID) "
                + "select e.KEGGEnzyme_WID,c.WID from "
                + KEGGTables.getInstance().KEGGENZYMEEFFECTOR
                + " e inner join "
                + KEGGTables.getInstance().KEGGCOMPOUND
                + " c on c.Entry = e.Entry");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGENZYME_HAS_KEGGPATHWAY
                + " (KEGGEnzyme_WID,KEGGPathway_WID) "
                + "select g.WID,p.KEGGPathway_WID from "
                + KEGGTables.getInstance().KEGGENZYME
                + " g inner join "
                + KEGGTables.getInstance().KEGGPATHWAYENTRYENZYME
                + " p on p.Entry = g.Entry group by g.WID,p.KEGGPathway_WID");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.getInstance().KEGGPATHWAYENTRYENZYME);
    }

    private void sqlRelationshipGlycan() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Creating Glycan relationships");

        whdbmsFactory.executeUpdate("ALTER TABLE " + KEGGTables.getInstance().KEGGGLYCANCLASS + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());
        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.getInstance().KEGGGLYCANCLASS
                + " (Class) "
                + "select Class from "
                + KEGGTables.getInstance().KEGGGLYCANCLASSTEMP
                + " group by Class");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + KEGGTables.getInstance().KEGGGLYCANCLASS, "WID"));

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGGLYCAN_HAS_KEGGGLYCANCLASS
                + " (KEGGGlycan_WID,KEGGGlycanClass_WID) "
                + "select c.KEGGGlycan_WID,e.WID from "
                + KEGGTables.getInstance().KEGGGLYCANCLASS
                + " e inner join "
                + KEGGTables.getInstance().KEGGGLYCANCLASSTEMP
                + " c on e.Class = c.Class group by c.KEGGGlycan_WID,e.WID");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.getInstance().KEGGGLYCANCLASSTEMP);
    }

    private void sqlRelationshipRPair() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Creating RPair relationships");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.getInstance().KEGGRPAIRRELATEDPAIR
                + " (KEGGRPair_WID,KEGGRPair_Other_WID) "
                + "select p.WID,t.KEGGRPair_WID from "
                + KEGGTables.getInstance().KEGGRPAIRRELATEDPAIRTEMP
                + " t inner join "
                + KEGGTables.getInstance().KEGGRPAIR
                + " p on t.Entry = p.Entry");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.getInstance().KEGGRPAIRRELATEDPAIRTEMP);

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGRPAIR_HAS_KEGGCOMPOUND
                + " (KEGGRPair_WID,KEGGCompound_WID) "
                + "select r.KEGGRPair_WID,c.WID from "
                + KEGGTables.getInstance().KEGGRPAIRCOMPOUND
                + " r inner join "
                + KEGGTables.getInstance().KEGGCOMPOUND
                + " c on r.Entry = c.Entry group by r.KEGGRPair_WID,c.WID");
    }

    private void sqlRelationshipGene() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Creating Gene relationships");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGGENE_HAS_KEGGPATHWAY
                + " (KEGGGene_WID,KEGGPathway_WID) "
                + "select g.WID,p.KEGGPathway_WID from "
                + KEGGTables.getInstance().KEGGGENE
                + " g inner join "
                + KEGGTables.getInstance().KEGGPATHWAYENTRYGENE
                + " p on p.Entry = g.Entry group by g.WID,p.KEGGPathway_WID");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.getInstance().KEGGPATHWAYENTRYGENE);
    }

    private void sqlRelationshipCompound() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Creating Compound relationships");

        whdbmsFactory.executeUpdate("insert into "
                + KEGGTables.KEGGCOMPOUND_HAS_KEGGPATHWAY
                + " (KEGGCompound_WID,KEGGPathway_WID) "
                + "select g.WID,p.KEGGPathway_WID from "
                + KEGGTables.getInstance().KEGGCOMPOUND
                + " g inner join "
                + KEGGTables.getInstance().KEGGPATHWAYENTRYCOMPOUND
                + " p on p.Entry = g.Entry group by g.WID,p.KEGGPathway_WID");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + KEGGTables.getInstance().KEGGPATHWAYENTRYCOMPOUND);
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(KEGGTables.getInstance().getTables());
    }
}
