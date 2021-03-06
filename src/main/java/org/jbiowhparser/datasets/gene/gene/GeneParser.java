package org.jbiowhparser.datasets.gene.gene;

import java.sql.SQLException;
import java.util.Date;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.gene.gene.files.Gene2AccessionParser;
import org.jbiowhparser.datasets.gene.gene.files.Gene2EnsemblParser;
import org.jbiowhparser.datasets.gene.gene.files.Gene2GOParser;
import org.jbiowhparser.datasets.gene.gene.files.Gene2MIMParser;
import org.jbiowhparser.datasets.gene.gene.files.Gene2PMIDParser;
import org.jbiowhparser.datasets.gene.gene.files.Gene2STSParser;
import org.jbiowhparser.datasets.gene.gene.files.Gene2UniGeneParser;
import org.jbiowhparser.datasets.gene.gene.files.GeneGroupParser;
import org.jbiowhparser.datasets.gene.gene.files.GeneHistoryParser;
import org.jbiowhparser.datasets.gene.gene.files.GeneInfoParser;
import org.jbiowhparser.datasets.gene.gene.files.GeneRefseqUniProtParser;
import org.jbiowhparser.datasets.gene.gene.links.GeneLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since Jul 5, 2011
 */
public class GeneParser extends ParserFactory implements JBioWHParser {

    /**
     * Run the Taxonomy Parser
     *
     * @throws java.sql.SQLException
     */
    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        
        if (DataSetPersistence.getInstance().isDroptables()){
            for (String table : GeneTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }
        
        ParseFiles.getInstance().start(GeneTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());

        GeneInfoParser geneinfo = new GeneInfoParser();
        Gene2AccessionParser gene2accession = new Gene2AccessionParser();
        Gene2EnsemblParser gene2ensembl = new Gene2EnsemblParser();
        Gene2GOParser gene2go = new Gene2GOParser();
        Gene2PMIDParser gene2pmid = new Gene2PMIDParser();
        Gene2STSParser gene2sts = new Gene2STSParser();
        Gene2UniGeneParser gene2unigene = new Gene2UniGeneParser();
        GeneGroupParser genegroup = new GeneGroupParser();
        GeneHistoryParser genehistory = new GeneHistoryParser();
        GeneRefseqUniProtParser uniprot = new GeneRefseqUniProtParser();
        Gene2MIMParser gene2mim = new Gene2MIMParser();

        geneinfo.loader();
        gene2accession.loader();
        gene2ensembl.loader();
        gene2go.loader();
        gene2pmid.loader();
        gene2sts.loader();
        gene2unigene.loader();
        genegroup.loader();
        genehistory.loader();
        uniprot.loader();
        gene2mim.loader();

        if (DataSetPersistence.getInstance().isRunlinks()) {
            GeneLinks.getInstance().runLink();
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(GeneTables.getInstance().getTables());
    }
}
