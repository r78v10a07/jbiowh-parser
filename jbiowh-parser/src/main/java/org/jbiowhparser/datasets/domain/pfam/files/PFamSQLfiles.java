package org.jbiowhparser.datasets.domain.pfam.files;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.fileformats.sql.JBioWHSQLParser;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.datasets.domain.pfam.links.PFamLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.domain.pfam.PFamTables;

/**
 * This class load the Pfam SQL database into the JBioWH relational schema
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 515 $
 *
 * @since Oct 24, 2012
 */
public class PFamSQLfiles {

    private final String PFAMA_REG_FULL_INSIGNIFICANT = "pfamA_reg_full_insignificant";
    private final String PFAMA_REG_FULL_SIGNIFICANT = "pfamA_reg_full_significant";
    private final String PFAMA_REG_SEED = "pfamA_reg_seed";
    private final String PFAMSEQ = "pfamseq";
    private final String CONTEXT_PFAM_REGIONS = "context_pfam_regions";
    private final String PFAMB = "pfamB";
    private final String PFAMB_REG = "pfamB_reg";
    private final String OTHER_REG = "other_reg";
    private final String PFAMSEQ_DISULPHIDE = "pfamseq_disulphide";
    private final String MARKUP_KEY = "markup_key";
    private final String PFAMSEQ_MARKUP = "pfamseq_markup";
    private final String ARCHITECTURE = "architecture";
    private final String PFAMA_ARCHITECTURE = "pfamA_architecture";
    private final String PFAMA_DATABASE_LINKS = "pfamA_database_links";
    private final String INTERPRO = "interpro";
    private final String LITERATURE_REFERENCES = "literature_references";
    private final String PFAMA_LITERATURE_REFERENCES = "pfamA_literature_references";
    private final String PFAMA_INTERACTIONS = "pfamA_interactions";
    private final String CLANS = "clans";
    private final String CLAN_MEMBERSHIP = "clan_membership";
    private final String CLAN_ARCHITECTURE = "clan_architecture";
    private final String CLAN_LIT_REFS = "clan_lit_refs";
    private final String CLAN_DATABASE_LINKS = "clan_database_links";
    private final String NESTED_LOCATIONS = "nested_locations";
    //private final String NCBI_PFAMA_REG = "ncbi_pfamA_reg";
    private final String PDB = "pdb";
    private final String PDB_PFAMA_REG = "pdb_pfamA_reg";
    private final String PDB_RESIDUE_DATA = "pdb_residue_data";
    private final String COMPLETE_PROTEOMES = "complete_proteomes";
    private final String PROTEOME_PFAMSEQ = "proteome_pfamseq";
    private final String PROTEOME_REGIONS = "proteome_regions";

    public String[] pfamFiles() {
        return new String[]{
            "pfamA_reg_full_insignificant.sql.gz", "pfamA_reg_full_insignificant.txt.gz",
            "pfamA_reg_full_significant.sql.gz", "pfamA_reg_full_significant.txt.gz",
            "pfamA_reg_seed.sql.gz", "pfamA_reg_seed.txt.gz",
            "pfamseq.sql.gz", "pfamseq.txt.gz",
            "context_pfam_regions", "context_pfam_regions.txt.gz",
            "pfamB.sql.gz", "pfamB.txt.gz",
            "pfamB_reg.sql.gz", "pfamB_reg.txt.gz",
            "other_reg.sql.gz", "other_reg.txt.gz",
            "pfamseq_disulphide.sql.gz", "pfamseq_disulphide.txt.gz",
            "markup_key.sql.gz", "markup_key.txt.gz",
            "pfamseq_markup.sql.gz", "pfamseq_markup.txt.gz",
            "architecture.sql.gz", "architecture.txt.gz",
            "pfamA_architecture.sql.gz", "pfamA_architecture.txt.gz",
            "pfamA_database_links", "pfamA_database_links.txt.gz",
            "interpro.sql.gz", "interpro.txt.gz",
            "literature_references.sql.gz", "literature_references.txt.gz",
            "pfamA_literature_references", "pfamA_literature_references.txt.gz",
            "pfamA_interactions.sql.gz", "pfamA_interactions.txt.gz",
            "clans.sql.gz", "clans.txt.gz",
            "clan_membership.sql.gz", "clan_membership.txt.gz",
            "clan_architecture.sql.gz", "clan_architecture.txt.gz",
            "clan_lit_refs.sql.gz", "clan_lit_refs.txt.gz",
            "clan_database_links.sql.gz", "clan_database_links.txt.gz",
            "nested_locations.sql.gz", "nested_locations.txt.gz",
            "pdb.sql.gz", "pdb.txt.gz",
            "pdb_pfamA_reg.sql.gz", "pdb_pfamA_reg.txt.gz",
            "pdb_residue_data.sql.gz", "pdb_residue_data.txt.gz",
            "complete_proteomes.sql.gz", "complete_proteomes.txt.gz",
            "proteome_pfamseq.sql.gz", "proteome_pfamseq.txt.gz",
            "proteome_regions.sql.gz", "proteome_regions.txt.gz"
        };
    }

    public void loadData() {
        try {
            JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
            loadPFAMData(PFamTables.getInstance().PFAMA_ORIG);
            loadPFAMData(PFAMA_REG_FULL_INSIGNIFICANT);
            loadPFAMData(PFAMA_REG_FULL_SIGNIFICANT);
            loadPFAMData(PFAMA_REG_SEED);
            loadPFAMData(PFAMSEQ);
            loadPFAMData(CONTEXT_PFAM_REGIONS);
            loadPFAMData(PFAMB);
            loadPFAMData(PFAMB_REG);
            loadPFAMData(OTHER_REG);
            loadPFAMData(PFAMSEQ_DISULPHIDE);
            loadPFAMData(MARKUP_KEY);
            loadPFAMData(PFAMSEQ_MARKUP);
            loadPFAMData(ARCHITECTURE);
            loadPFAMData(PFAMA_ARCHITECTURE);
            loadPFAMData(PFamTables.getInstance().GENE_ONTOLOGY);
            loadPFAMData(PFAMA_DATABASE_LINKS);
            loadPFAMData(INTERPRO);
            loadPFAMData(LITERATURE_REFERENCES);
            loadPFAMData(PFAMA_LITERATURE_REFERENCES);
            loadPFAMData(PFAMA_INTERACTIONS);
            loadPFAMData(CLANS);
            loadPFAMData(CLAN_MEMBERSHIP);
            loadPFAMData(CLAN_ARCHITECTURE);
            loadPFAMData(CLAN_LIT_REFS);
            loadPFAMData(CLAN_DATABASE_LINKS);
            loadPFAMData(NESTED_LOCATIONS);
            loadPFAMData(PFamTables.getInstance().PFAMA_NCBI);
            //loadPFAMData(NCBI_PFAMA_REG);
            loadPFAMData(PDB);
            loadPFAMData(PDB_PFAMA_REG);
            loadPFAMData(PDB_RESIDUE_DATA);
            loadPFAMData(COMPLETE_PROTEOMES);
            loadPFAMData(PROTEOME_PFAMSEQ);
            loadPFAMData(PROTEOME_REGIONS);

            new PFamSQLReader().loadSQLScript(whdbmsFactory);

            if (DataSetPersistence.getInstance().isRunlinks()) {
                PFamLinks.getInstance().runLink();
            }

            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_REG_FULL_INSIGNIFICANT);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_REG_FULL_SIGNIFICANT);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_REG_SEED);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMSEQ);
            whdbmsFactory.executeUpdate("DROP TABLE " + CONTEXT_PFAM_REGIONS);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMB);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMB_REG);
            whdbmsFactory.executeUpdate("DROP TABLE " + OTHER_REG);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMSEQ_DISULPHIDE);
            whdbmsFactory.executeUpdate("DROP TABLE " + MARKUP_KEY);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMSEQ_MARKUP);
            whdbmsFactory.executeUpdate("DROP TABLE " + ARCHITECTURE);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_ARCHITECTURE);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_DATABASE_LINKS);
            whdbmsFactory.executeUpdate("DROP TABLE " + INTERPRO);
            whdbmsFactory.executeUpdate("DROP TABLE " + LITERATURE_REFERENCES);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_LITERATURE_REFERENCES);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_INTERACTIONS);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLANS);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLAN_MEMBERSHIP);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLAN_ARCHITECTURE);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLAN_LIT_REFS);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLAN_DATABASE_LINKS);
            whdbmsFactory.executeUpdate("DROP TABLE " + NESTED_LOCATIONS);
            whdbmsFactory.executeUpdate("DROP TABLE " + PDB);
            whdbmsFactory.executeUpdate("DROP TABLE " + PDB_PFAMA_REG);
            whdbmsFactory.executeUpdate("DROP TABLE " + PDB_RESIDUE_DATA);
            whdbmsFactory.executeUpdate("DROP TABLE " + COMPLETE_PROTEOMES);
            whdbmsFactory.executeUpdate("DROP TABLE " + PROTEOME_PFAMSEQ);
            whdbmsFactory.executeUpdate("DROP TABLE " + PROTEOME_REGIONS);
        } catch (SQLException ex) {
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

    private void loadPFAMData(String fileName) {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        List<String> lineList = JBioWHSQLParser.getInstance().getAllSQL(new File(DataSetPersistence.getInstance().getDirectory() + fileName + ".sql"));
        for (String line : lineList) {
            try {
                if (line.toUpperCase().contains("DROP") || line.toUpperCase().contains("CREATE")) {
                    whdbmsFactory.executeUpdate(line);
                }
            } catch (SQLException ex) {
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
        whdbmsFactory.loadTSVFile(fileName, DataSetPersistence.getInstance().getDirectory() + fileName + ".txt", "LINES TERMINATED BY '\\n';");
    }
}
