package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the Gene2Accession parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Jul 5, 2011
 */
public class Gene2AccessionParser {

    /**
     * This method load the data in gene2accession file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE2ACCESSIONTEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2ACCESSIONTEMP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.GENE2ACCESSION);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.GENE2ACCESSION);
        }

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE2ACCESSIONFILE + " file");

        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE2ACCESSIONTEMP, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE2ACCESSIONFILE,
                "  IGNORE 1 LINES "
                + "(@TaxID,GeneID,@Status,@RNANucleotideAccession,@RNANucleotideGi,@ProteinAccession,@ProteinGi,@GenomicNucleotideAccession,"
                + "@GenomicNucleotideGi,@StartPositionOnTheGenomicAccession,@EndPositionOnTheGenomicAccession,@Orientation,@Assembly)"
                + " set "
                + "Status=REPLACE(@Status,'-',NULL),"
                + "RNANucleotideAccession=REPLACE(@RNANucleotideAccession,'-',NULL),"
                + "RNANucleotideGi=REPLACE(@RNANucleotideGi,'-',NULL),"
                + "ProteinAccession=REPLACE(@ProteinAccession,'-',NULL),"
                + "ProteinGi=REPLACE(@ProteinGi,'-',NULL),"
                + "GenomicNucleotideAccession=REPLACE(@GenomicNucleotideAccession,'-',NULL),"
                + "GenomicNucleotideGi=REPLACE(@GenomicNucleotideGi,'-',NULL),"
                + "StartPositionOnTheGenomicAccession=REPLACE(@StartPositionOnTheGenomicAccession,'-',NULL),"
                + "EndPositionOnTheGenomicAccession=REPLACE(@EndPositionOnTheGenomicAccession,'-',NULL),"
                + "Orientation=REPLACE(@Orientation,'\\?',NULL),"
                + "Assembly=REPLACE(@Assembly,'-',NULL)");

        whdbmsFactory.executeUpdate("ALTER TABLE " + GeneTables.GENE2ACCESSION + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.GENE2ACCESSION
                + "(GeneInfo_WID,"
                + "Status,"
                + "RNANucleotideAccession,"
                + "RNANucleotideAccessionVersion,"
                + "RNANucleotideGi,"
                + "ProteinAccession,"
                + "ProteinAccessionVersion,"
                + "ProteinGi,"
                + "GenomicNucleotideAccession,"
                + "GenomicNucleotideAccessionVersion,"
                + "GenomicNucleotideGi,"
                + "StartPositionOnTheGenomicAccession,"
                + "EndPositionOnTheGenomicAccession,"
                + "Orientation,"
                + "Assembly) "
                + "select g.WID,"
                + "a.Status,"
                + "IF(LOCATE('.',a.RNANucleotideAccession),SUBSTRING(a.RNANucleotideAccession,1,LOCATE('.',a.RNANucleotideAccession)-1),a.RNANucleotideAccession),"
                + "IF(LOCATE('.',a.RNANucleotideAccession),SUBSTRING(a.RNANucleotideAccession,LOCATE('.',a.RNANucleotideAccession)+1),NULL),"
                + "a.RNANucleotideGi,"
                + "IF(LOCATE('.',a.ProteinAccession),SUBSTRING(a.ProteinAccession,1,LOCATE('.',a.ProteinAccession)-1),a.ProteinAccession),"
                + "IF(LOCATE('.',a.ProteinAccession),SUBSTRING(a.ProteinAccession,LOCATE('.',a.ProteinAccession)+1),NULL),"
                + "a.ProteinGi,"
                + "IF(LOCATE('.',a.GenomicNucleotideAccession),SUBSTRING(a.GenomicNucleotideAccession,1,LOCATE('.',a.GenomicNucleotideAccession)-1),a.GenomicNucleotideAccession),"
                + "IF(LOCATE('.',a.GenomicNucleotideAccession),SUBSTRING(a.GenomicNucleotideAccession,LOCATE('.',a.GenomicNucleotideAccession)+1),NULL),"
                + "a.GenomicNucleotideGi,"
                + "a.StartPositionOnTheGenomicAccession,"
                + "a.EndPositionOnTheGenomicAccession,"
                + "a.Orientation,"
                + "a.Assembly "
                + "from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENE2ACCESSIONTEMP
                + " a on a.GeneID = g.GeneID");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + GeneTables.GENE2ACCESSION, "WID"));

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE2ACCESSIONTEMP);
    }
}
