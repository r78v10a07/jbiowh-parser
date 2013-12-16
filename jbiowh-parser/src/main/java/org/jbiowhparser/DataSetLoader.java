package org.jbiowhparser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.jbiowhcore.basic.JBioWHUserData;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.AbstractDefaultTool;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.mysql.WHMySQL;
import org.jbiowhparser.datasets.disease.omim.OMIMParser;
import org.jbiowhparser.datasets.domain.pfam.PFamParser;
import org.jbiowhparser.datasets.drug.drugbank.DrugBankParser;
import org.jbiowhparser.datasets.gene.genbank.GeneBankParser;
import org.jbiowhparser.datasets.gene.genbank.GeneBankUpdateParser;
import org.jbiowhparser.datasets.gene.gene.GeneParser;
import org.jbiowhparser.datasets.gene.genome.GenePTTParser;
import org.jbiowhparser.datasets.ontology.OntologyParser;
import org.jbiowhparser.datasets.pathway.kegg.KEGGParser;
import org.jbiowhparser.datasets.ppi.MIF25Parser;
import org.jbiowhparser.datasets.protclust.UniRefParser;
import org.jbiowhparser.datasets.protein.ProteinParser;
import org.jbiowhparser.datasets.protgroup.pirsf.PirsfParser;
import org.jbiowhparser.datasets.taxonomy.TaxonomyParser;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.utils.entitymanager.JBioWHPersistence;
import org.xml.sax.SAXException;

/**
 * This Class to start parsing from the command line the data sources
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-10-03 22:11:05 +0200
 * (Wed, 03 Oct 2012) $ $LastChangedRevision: 591 $
 *
 * @since Jun 15, 2011
 */
public class DataSetLoader extends AbstractDefaultTool {

    private String configXMLFile;
    private boolean clean;

    public void dataSetLoader() throws SQLException, ParserConfigurationException, SAXException, IOException {
        DataSetPersistence.getInstance().readDatasetFromFile(configXMLFile);
        JBioWHUserData factory = JBioWHPersistence.getInstance().getWhdbmsFactory();
        JBioWHDBMS.getInstance().setWhdbmsFactory(
                new WHMySQL(factory.getDriver(), factory.getUrl(), factory.getUser(), factory.getPasswd(), true));
        VerbLogger.getInstance().log(this.getClass(), "Parsing a " + DataSetPersistence.getInstance().getType() + " data source");
        JBioWHDBMS.getInstance().setMainURL(factory.getUrl());
        ParseFactory parser = null;
        switch (DataSetPersistence.getInstance().getType()) {
            case "Taxonomy":
                DataSetPersistence.getInstance().getDataset().setApplication("TaxonomyLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new TaxonomyParser();
                break;
            case "Ontology":
                DataSetPersistence.getInstance().getDataset().setApplication("OntologyLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new OntologyParser();
                break;
            case "Gene":
                DataSetPersistence.getInstance().getDataset().setApplication("GeneLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new GeneParser();
                break;
            case "GenePTT":
                DataSetPersistence.getInstance().getDataset().setApplication("GenePTTLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new GenePTTParser();
                break;
            case "Protein":
                DataSetPersistence.getInstance().getDataset().setApplication("ProteinLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new ProteinParser();
                break;
            case "MIF25":
                DataSetPersistence.getInstance().getDataset().setApplication("MIF25Loader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new MIF25Parser();
                break;
            case "UniRef":
                DataSetPersistence.getInstance().getDataset().setApplication("ProtsFamLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new UniRefParser();
                break;
            case "DrugBank":
                DataSetPersistence.getInstance().getDataset().setApplication("DrugLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new DrugBankParser();
                break;
            case "KEGG":
                DataSetPersistence.getInstance().getDataset().setApplication("PathwayLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new KEGGParser();
                break;
            case "OMIM":
                DataSetPersistence.getInstance().getDataset().setApplication("OMIMLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new OMIMParser();
                break;
            case "PFAM":
                DataSetPersistence.getInstance().getDataset().setApplication("PFAMLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new PFamParser();
                break;
            case "GeneBank":
                DataSetPersistence.getInstance().getDataset().setApplication("GeneBankLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new GeneBankParser();
                break;
            case "GeneBankUpdate":
                DataSetPersistence.getInstance().getDataset().setApplication("GeneBankLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new GeneBankUpdateParser();
                break;
            case "PIRSF":
                DataSetPersistence.getInstance().getDataset().setApplication("PIRSFLoader");
                DataSetPersistence.getInstance().getDataset().setApplicationVersion("1.0");
                parser = new PirsfParser();
                break;
        }

        if (parser != null) {
            if (clean) {
                parser.runCleaner();
            } else {
                parser.runLoader();
            }
        }
    }

    /**
     * This is the Main to parse and insert the DataSet
     *
     * @param args The String arguments
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws
            ParserConfigurationException, SAXException, IOException, SQLException {
        DataSetLoader task = new DataSetLoader();

        long startTime = System.currentTimeMillis();
        task.parseOptions(args);
        task.dataSetLoader();
        task.closeConnection();

        VerbLogger.getInstance().log(DataSetLoader.class, "Total elapsed time: " + ((long) (System.currentTimeMillis() - startTime) / 1000) + " s");
        System.exit(0);
    }

    @Override
    protected void parseOptions(String[] args) throws FileNotFoundException {
        OptionParser parser = new OptionParser("i:c");
        parser.accepts("i").withRequiredArg().ofType(String.class);
        parser.accepts("c");
        OptionSet options = parser.parse(args);
        configXMLFile = (String) parseOption(options, "i", null, true, true);

        clean = (Boolean) parseOption(options, "c", false, false, false);
    }

    @Override
    protected void printHelp() {
        System.out.println("Arguments:");
        System.out.println("\t-i XML config file");
        System.out.println("\t-c Clean the relational schema");
        System.exit(0);
    }

    @Override
    protected void openConnection(boolean isMain, boolean createSchema, boolean startThread) throws SQLException {
    }

    @Override
    protected void closeConnection() throws SQLException {
        if (file != null) {
            file.close();
        }
        JBioWHPersistence.getInstance().closeAll();
        JBioWHDBMS.getInstance().closeAll();
    }
}
