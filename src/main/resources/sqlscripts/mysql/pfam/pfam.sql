-- -----------------------------------------------------
-- Insert the Pfam table
-- -----------------------------------------------------
TRUNCATE TABLE PfamAbioWH;
SELECT (@WIDDataSet:=d.WID) FROM DataSet d WHERE Name='Pfam';
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;
SET @s = CONCAT("ALTER TABLE PfamAbioWH AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamAbioWH (pfamA_acc,pfamA_id,previous_id,description,author,deposited_by,seed_source,type,
comment,sequence_GA,domain_GA,sequence_TC,domain_TC,sequence_NC,domain_NC,buildMethod,model_length,searchMethod,
msv_lambda,msv_mu,viterbi_lambda,viterbi_mu,forward_lambda,forward_tau,num_seed,num_full,updated,created,version,
number_archs,number_species,number_structures,number_ncbi,number_meta,average_length,percentage_id,
average_coverage,change_status,seed_consensus,full_consensus,number_shuffled_hits,DataSet_WID) 
 SELECT pfamA_acc,pfamA_id,previous_id,description,author,deposited_by,seed_source,type,comment,sequence_GA,
 domain_GA,sequence_TC,domain_TC,sequence_NC,domain_NC,buildMethod,model_length,searchMethod,msv_lambda,
 msv_mu,viterbi_lambda,viterbi_mu,forward_lambda,forward_tau,num_seed,num_full,updated,created,version,
 number_archs,number_species,number_structures,number_ncbi,number_meta,average_length,percentage_id,
 average_coverage,change_status,seed_consensus,full_consensus,number_shuffled_hits,@WIDDataSet FROM pfamA;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamAbioWH a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Create a temporal table with the auto_pfamseq id and the UniProt Id
-- -----------------------------------------------------
TRUNCATE TABLE PfamSeq_has_UniProtId;
INSERT INTO PfamSeq_has_UniProtId (auto_pfamseq,UniProt_Id)
SELECT auto_pfamseq,pfamseq_id FROM pfamseq s;

TRUNCATE TABLE PfamSeq_has_Protein;
INSERT INTO PfamSeq_has_Protein (auto_pfamseq,Protein_WID)
SELECT u.auto_pfamseq,n.Protein_WID FROM PfamSeq_has_UniProtId
 u inner join ProteinName n on n.Name = u.UniProt_Id;

-- -----------------------------------------------------
-- Insert the PfamARegFullSignificant table
-- -----------------------------------------------------
TRUNCATE TABLE PfamARegFullSignificant;
TRUNCATE TABLE PfamARegFullSignificantTemp;
SET @s = CONCAT("ALTER TABLE PfamARegFullSignificantTemp AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamARegFullSignificantTemp (auto_pfamA_reg_full)
SELECT auto_pfamA_reg_full
 FROM pfamA_reg_full_significant;
INSERT INTO PfamARegFullSignificant (WID,PfamA_WID,Protein_WID,auto_pfamseq,seq_start,seq_end,ali_start,ali_end,model_start,
model_end,domain_bits_score,domain_evalue_score,sequence_bits_score,sequence_evalue_score,cigar,in_full,tree_order,
domain_order,domain_oder)
SELECT t.WID,p.WID,q.Protein_WID,s.auto_pfamseq,s.seq_start,s.seq_end,s.ali_start,s.ali_end,s.model_start,s.model_end,s.domain_bits_score,
s.domain_evalue_score,s.sequence_bits_score,s.sequence_evalue_score,s.cigar,s.in_full,s.tree_order,s.domain_order,s.domain_oder
 FROM pfamA_reg_full_significant s 
 INNER JOIN PfamARegFullSignificantTemp t ON t.auto_pfamA_reg_full = s.auto_pfamA_reg_full
 INNER JOIN pfamA a ON a.auto_pfamA = s.auto_pfamA 
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc
 INNER JOIN PfamSeq_has_Protein q on q.auto_pfamseq = s.auto_pfamseq;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamARegFullSignificant a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamPDB table
-- -----------------------------------------------------
TRUNCATE TABLE PfamPDB;
SET @s = CONCAT("ALTER TABLE PfamPDB AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamPDB (pdb_id,keywords,title,date,resolution,method,author)
 SELECT pdb_id,keywords,title,date,resolution,method,author FROM pdb;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamPDB a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamPDBResidueData table
-- -----------------------------------------------------
TRUNCATE TABLE PfamPDBResidueData;
SET @s = CONCAT("ALTER TABLE PfamPDBResidueData AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamPDBResidueData (auto_pfamseq,PfamPDB_WID,chain,serial,
pdb_res,pdb_seq_number,pdb_insert_code,observed,dssp_code,pfamseq_res,pfamseq_seq_number)
 SELECT d.auto_pfamseq,p.WID,d.chain,d.serial,d.pdb_res,d.pdb_seq_number,
d.pdb_insert_code,d.observed,d.dssp_code,d.pfamseq_res,d.pfamseq_seq_number
 FROM pdb_residue_data d INNER JOIN PfamPDB p ON p.pdb_id = d.pdb_id;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamPDBResidueData a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamAPDBReg table
-- -----------------------------------------------------
TRUNCATE TABLE PfamAPDBReg;
SET @s = CONCAT("ALTER TABLE PfamAPDBReg AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamAPDBReg (PfamARegFullSignificant_WID,chain,pdb_res_start,
pdb_start_icode,pdb_res_end,pdb_end_icode,seq_start,seq_end,hex_colour)
 SELECT f.WID,r.chain,r.pdb_res_start,r.pdb_start_icode,r.pdb_res_end,r.pdb_end_icode,
 r.seq_start,r.seq_end,r.hex_colour FROM pdb_pfamA_reg r
 INNER JOIN pfamA_reg_full_significant s ON s.auto_pfamA_reg_full = r.auto_pfamA_reg_full
 INNER JOIN PfamARegFullSignificantTemp f ON f.auto_pfamA_reg_full = r.auto_pfamA_reg_full;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamAPDBReg a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

TRUNCATE TABLE PfamARegFullSignificantTemp;

-- -----------------------------------------------------
-- Insert the PfamARegFullInsignificant table
-- -----------------------------------------------------
TRUNCATE TABLE PfamARegFullInsignificant;
SET @s = CONCAT("ALTER TABLE PfamARegFullInsignificant AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamARegFullInsignificant (PfamA_WID,Protein_WID,auto_pfamseq,seq_start,seq_end,model_start,model_end,
domain_bits_score,domain_evalue_score,sequence_bits_score,sequence_evalue_score)
SELECT p.WID,q.Protein_WID,s.auto_pfamseq,s.seq_start,s.seq_end,s.model_start,s.model_end,
s.domain_bits_score,s.domain_evalue_score,s.sequence_bits_score,s.sequence_evalue_score
FROM pfamA_reg_full_insignificant s INNER JOIN pfamA a ON a.auto_pfamA = s.auto_pfamA 
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc
 INNER JOIN PfamSeq_has_Protein q on q.auto_pfamseq = s.auto_pfamseq;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamARegFullInsignificant a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamARegSeed table
-- -----------------------------------------------------
TRUNCATE TABLE PfamARegSeed;
INSERT INTO PfamARegSeed (PfamA_WID,auto_pfamseq,seq_start,seq_end,cigar,tree_order)
SELECT p.WID,s.auto_pfamseq,s.seq_start,s.seq_end,s.cigar,s.tree_order FROM
pfamA_reg_seed s INNER JOIN pfamA a ON a.auto_pfamA = s.auto_pfamA 
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc;

-- -----------------------------------------------------
-- Insert the PfamContextRegions table
-- -----------------------------------------------------
TRUNCATE TABLE PfamContextRegions;
SET @s = CONCAT("ALTER TABLE PfamContextRegions AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamContextRegions (auto_pfamseq,PfamA_WID,seq_start,seq_end,domain_score)
SELECT s.auto_pfamseq,p.WID,s.seq_start,s.seq_end,s.domain_score
 FROM context_pfam_regions s INNER JOIN pfamA a ON a.auto_pfamA = s.auto_pfamA 
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamContextRegions a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamNestedLocations table
-- -----------------------------------------------------
TRUNCATE TABLE PfamNestedLocations;
SET @s = CONCAT("ALTER TABLE PfamNestedLocations AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamNestedLocations (PfamA_WID,OtherPfamA_WID,auto_pfamseq,seq_version,
seq_start,seq_end) SELECT p.WID,p1.WID,n.auto_pfamseq,n.seq_version,n.seq_start,n.seq_end 
 FROM nested_locations n INNER JOIN pfamA a ON a.auto_pfamA = n.auto_pfamA 
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc 
 INNER JOIN pfamA a1 ON a1.auto_pfamA = n.nested_auto_pfamA 
 INNER JOIN PfamAbioWH p1 ON p1.pfamA_acc = a1.pfamA_acc;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamNestedLocations a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamSeqDisulphide table
-- -----------------------------------------------------
TRUNCATE TABLE PfamSeqDisulphide;
SET @s = CONCAT("ALTER TABLE PfamSeqDisulphide AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamSeqDisulphide (auto_pfamseq,bond_start,bond_end)
SELECT d.auto_pfamseq,d.bond_start,d.bond_end FROM pfamseq_disulphide d;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamSeqDisulphide a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamOtherReg table
-- -----------------------------------------------------
TRUNCATE TABLE PfamOtherReg;
INSERT INTO PfamOtherReg (region_id,auto_pfamseq,seq_start,seq_end,type_id,source_id,
score,orientation) SELECT d.region_id,d.auto_pfamseq,d.seq_start,d.seq_end,d.type_id,d.source_id,
d.score,d.orientation FROM other_reg d ;

-- -----------------------------------------------------
-- Insert the PfamMarkupKey table
-- -----------------------------------------------------
TRUNCATE TABLE PfamMarkupKey;
SET @s = CONCAT("ALTER TABLE PfamMarkupKey AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamMarkupKey (label) SELECT label FROM markup_key;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamMarkupKey a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamMarkup_has_Protein table
-- -----------------------------------------------------
TRUNCATE TABLE PfamMarkup_has_Protein;
SET @s = CONCAT("ALTER TABLE PfamMarkup_has_Protein AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamMarkup_has_Protein (auto_pfamseq,PfamMarkupKey_WID,residue,annotation)
 SELECT d.auto_pfamseq,y.WID,d.residue,d.annotation FROM pfamseq_markup d
 INNER JOIN markup_key k ON k.auto_markup = d.auto_markup 
 INNER JOIN PfamMarkupKey y ON y.label = k.label;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamMarkup_has_Protein a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamB table
-- -----------------------------------------------------
TRUNCATE TABLE PfamBbioWH;
SET @s = CONCAT("ALTER TABLE PfamBbioWH AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamBbioWH (pfamB_acc,pfamB_id,number_archs,number_regions,number_species,number_structures)
 SELECT pfamB_acc,pfamB_id,number_archs,number_regions,number_species,number_structures FROM pfamB;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamBbioWH a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamBReg table
-- -----------------------------------------------------
TRUNCATE TABLE PfamBReg;
SET @s = CONCAT("ALTER TABLE PfamBReg AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamBReg (PfamB_WID,auto_pfamseq,seq_start,seq_end)
 SELECT p.WID,r.auto_pfamseq,r.seq_start,r.seq_end FROM pfamB_reg r 
 INNER JOIN pfamB a ON a.auto_pfamB = r.auto_pfamB 
 INNER JOIN PfamBbioWH p ON p.pfamB_acc = a.pfamB_acc;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamBReg a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;


-- -----------------------------------------------------
-- Insert the PfamArchitecture table
-- -----------------------------------------------------
TRUNCATE TABLE PfamArchitecture;
SET @s = CONCAT("ALTER TABLE PfamArchitecture AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamArchitecture (architecture,type_example,no_seqs,architecture_acc)
 SELECT architecture,type_example,no_seqs,architecture_acc FROM architecture;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamArchitecture a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamA_has_PfamArchitecture table
-- -----------------------------------------------------
TRUNCATE TABLE PfamA_has_PfamArchitecture;
INSERT INTO PfamA_has_PfamArchitecture (PfamA_WID,PfamArchitecture_WID)
 SELECT p.WID,paa.WID FROM pfamA_architecture pa 
 INNER JOIN pfamA a ON pa.auto_pfamA = a.auto_pfamA
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc
 INNER JOIN architecture c ON c.auto_architecture = pa.auto_architecture
 INNER JOIN PfamArchitecture paa ON paa.architecture = c.architecture;

-- -----------------------------------------------------
-- Insert the PfamClans table
-- -----------------------------------------------------
TRUNCATE TABLE PfamClans;
SET @s = CONCAT("ALTER TABLE PfamClans AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamClans (clan_acc,clan_id,previous_id,clan_description,clan_author,
deposited_by,clan_comment,updated,created,version,number_structures,number_archs,
number_species,number_sequences,competed) SELECT clan_acc,clan_id,previous_id,
clan_description,clan_author,deposited_by,clan_comment,updated,created,version,
number_structures,number_archs,number_species,number_sequences,competed FROM clans;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamClans a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamClans_has_PfamA table
-- -----------------------------------------------------
TRUNCATE TABLE PfamClans_has_PfamA;
INSERT INTO PfamClans_has_PfamA (PfamClans_WID,PfamA_WID)
 SELECT c.WID,p.WID FROM clan_membership m 
 INNER JOIN pfamA a ON m.auto_pfamA = a.auto_pfamA
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc
 INNER JOIN clans s ON s.auto_clan = m.auto_clan
 INNER JOIN PfamClans c on c.clan_acc = s.clan_acc;

-- -----------------------------------------------------
-- Insert the PfamClans_has_PfamArchitecture table
-- -----------------------------------------------------
TRUNCATE TABLE PfamClans_has_PfamArchitecture;
INSERT INTO PfamClans_has_PfamArchitecture (PfamClans_WID,PfamArchitecture_WID)
 SELECT c.WID,paa.WID FROM clan_architecture m
 INNER JOIN clans s ON s.auto_clan = m.auto_clan
 INNER JOIN PfamClans c on c.clan_acc = s.clan_acc
 INNER JOIN architecture a ON a.auto_architecture = m.auto_architecture
 INNER JOIN PfamArchitecture paa ON paa.architecture = a.architecture;

-- -----------------------------------------------------
-- Insert the PfamClanDatabaseLinks table
-- -----------------------------------------------------
TRUNCATE TABLE PfamClanDatabaseLinks;
SET @s = CONCAT("ALTER TABLE PfamClanDatabaseLinks AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamClanDatabaseLinks (PfamClans_WID,db_id,comment,db_link,other_params)
 SELECT c.WID,db_id,comment,db_link,other_params FROM clan_database_links l
 INNER JOIN clans s ON s.auto_clan = l.auto_clan
 INNER JOIN PfamClans c on c.clan_acc = s.clan_acc;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamClanDatabaseLinks a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamLiteratureReferences table
-- -----------------------------------------------------
TRUNCATE TABLE PfamLiteratureReferences;
SET @s = CONCAT("ALTER TABLE PfamLiteratureReferences AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamLiteratureReferences (pmid,title,author,journal)
 SELECT pmid,title,author,journal FROM literature_references;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamLiteratureReferences a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamA_has_PfamLiteratureReferences table
-- -----------------------------------------------------
TRUNCATE TABLE PfamA_has_PfamLiteratureReferences;
INSERT INTO PfamA_has_PfamLiteratureReferences (PfamA_WID,PfamLiteratureReferences_WID,
order_added,comment) SELECT p.WID,l.WID,t.order_added,t.comment FROM  pfamA_literature_references t
 INNER JOIN pfamA a ON a.auto_pfamA = t.auto_pfamA
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc
 INNER JOIN literature_references r ON r.auto_lit = t.auto_lit
 INNER JOIN PfamLiteratureReferences l ON l.pmid = r.pmid group by p.WID,l.WID,t.order_added;

-- -----------------------------------------------------
-- Insert the PfamClans_has_PfamLiteratureReferences table
-- -----------------------------------------------------
TRUNCATE TABLE PfamClans_has_PfamLiteratureReferences;
INSERT INTO PfamClans_has_PfamLiteratureReferences (PfamClans_WID,PfamLiteratureReferences_WID,
order_added,comment) SELECT c.WID,l.WID,t.order_added,t.comment FROM  clan_lit_refs t
 INNER JOIN clans s ON s.auto_clan = t.auto_clan
 INNER JOIN PfamClans c on c.clan_acc = s.clan_acc
 INNER JOIN literature_references r ON r.auto_lit = t.auto_lit
 INNER JOIN PfamLiteratureReferences l ON l.pmid = r.pmid;

-- -----------------------------------------------------
-- Insert the PfamADatabaseLinks table
-- -----------------------------------------------------
TRUNCATE TABLE PfamADatabaseLinks;
SET @s = CONCAT("ALTER TABLE PfamADatabaseLinks AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamADatabaseLinks (PfamA_WID,db_id,comment,db_link,other_params)
 SELECT p.WID,l.db_id,l.comment,l.db_link,l.other_params FROM pfamA_database_links l
 INNER JOIN pfamA a ON a.auto_pfamA = l.auto_pfamA
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamADatabaseLinks a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamInterpro table
-- -----------------------------------------------------
TRUNCATE TABLE PfamInterpro;
INSERT INTO PfamInterpro (PfamA_WID,interpro_id,abstract)
 SELECT p.WID,l.interpro_id,l.abstract FROM interpro l
 INNER JOIN pfamA a ON a.auto_pfamA = l.auto_pfamA
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc;

-- -----------------------------------------------------
-- Insert the PfamANCBIReg table
-- -----------------------------------------------------
-- TRUNCATE TABLE PfamANCBIReg;
-- SET @s = CONCAT("ALTER TABLE PfamANCBIReg AUTO_INCREMENT=",@WID);
-- PREPARE stmt FROM @s;
-- EXECUTE stmt;
-- DEALLOCATE PREPARE stmt;
-- INSERT INTO PfamANCBIReg (PfamA_WID,GeneId,seq_start,seq_end,ali_start,
-- ali_end,model_start,model_end,domain_bits_score,domain_evalue_score,sequence_bits_score,
-- sequence_evalue_score,cigar,in_full,tree_order) SELECT p.WID,r.gi,r.seq_start,r.seq_end,r.ali_start,
-- r.ali_end,r.model_start,r.model_end,r.domain_bits_score,r.domain_evalue_score,r.sequence_bits_score,
-- r.sequence_evalue_score,r.cigar,r.in_full,r.tree_order FROM ncbi_pfamA_reg r
--  INNER JOIN pfamA a ON a.auto_pfamA = r.auto_pfamA
--  INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc;
-- SELECT (@WID:=MAX(a.WID) + 1) FROM PfamANCBIReg a;
-- SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamCompleteProteomes table
-- -----------------------------------------------------
TRUNCATE TABLE PfamCompleteProteomes;
SET @s = CONCAT("ALTER TABLE PfamCompleteProteomes AUTO_INCREMENT=",@WID);
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
INSERT INTO PfamCompleteProteomes (species,grouping,num_distinct_regions,
num_total_regions,num_proteins,sequence_coverage,residue_coverage,total_genome_proteins,
total_aa_length,total_aa_covered,total_seqs_covered,TaxId) 
 SELECT c.species,c.grouping,c.num_distinct_regions,
c.num_total_regions,c.num_proteins,c.sequence_coverage,c.residue_coverage,c.total_genome_proteins,
c.total_aa_length,c.total_aa_covered,c.total_seqs_covered,c.ncbi_taxid FROM complete_proteomes c;
SELECT (@WID:=MAX(a.WID) + 1) FROM PfamCompleteProteomes a;
SELECT (@WID:=PreviousWID + 1) FROM WIDTable;

-- -----------------------------------------------------
-- Insert the PfamProteomeRegions table
-- -----------------------------------------------------
TRUNCATE TABLE PfamProteomeRegions;
INSERT INTO PfamProteomeRegions (PfamCompleteProteomes_WID,auto_pfamseq,
PfamA_WID,count) SELECT o.WID,r.auto_pfamseq,p.WID,r.count FROM proteome_regions r
 INNER JOIN pfamA a ON a.auto_pfamA = r.auto_pfamA
 INNER JOIN PfamAbioWH p ON p.pfamA_acc = a.pfamA_acc
 INNER JOIN complete_proteomes c ON c.auto_proteome = r.auto_proteome
 INNER JOIN PfamCompleteProteomes o ON c.ncbi_taxid = o.TaxId group by o.WID,r.auto_pfamseq,p.WID;

-- -----------------------------------------------------
-- Insert the PfamCompleteProteomes_has_PfamSeq table
-- -----------------------------------------------------
TRUNCATE TABLE PfamCompleteProteomes_has_PfamSeq;
INSERT INTO PfamCompleteProteomes_has_PfamSeq (PfamCompleteProteomes_WID,
auto_pfamseq) SELECT o.WID, s.auto_pfamseq FROM proteome_pfamseq s
 INNER JOIN complete_proteomes c ON c.auto_proteome = s.auto_proteome
 INNER JOIN PfamCompleteProteomes o ON o.TaxId = c.ncbi_taxid group by o.WID,s.auto_pfamseq;































