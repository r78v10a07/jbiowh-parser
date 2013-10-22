package org.jbiowhparser.datasets.disease.omim.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.disease.omim.OMIMTables;

/**
 * This Class is the parser for the OMIM genemap file
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
 * @since Jul 13, 2012
 */
public class GeneMapParser {

    /**
     * This method load the data in genemap file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + OMIMTables.getInstance().OMIMGENEMAP + " file");

        whdbmsFactory.loadTSVFile(OMIMTables.getInstance().OMIMGENEMAPTEMP, DataSetPersistence.getInstance().getDirectory() + OMIMTables.getInstance().GENEMAPFILE,
                " FIELDS TERMINATED BY '|' ENCLOSED BY '\\t' ESCAPED BY '\\\\'");

        whdbmsFactory.executeUpdate("ALTER TABLE " + OMIMTables.getInstance().OMIMGENEMAP + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());


        whdbmsFactory.executeUpdate("INSERT INTO "
                + OMIMTables.getInstance().OMIMGENEMAP
                + "(Number,Month,Day,Year,CytogLoc,GeneStatus,Title,MIMNumber,Comments,MouseCorr,Reference)"
                + "select "
                + "Number,"
                + "if(trim(Month) = '', null, Month),"
                + "if(trim(Day) = '', null, Day),"
                + "if(trim(Year) = '', null, Year),"
                + "if(trim(CytogLoc) = '', null, CytogLoc),"
                + "if(trim(GeneStatus) = '', null, GeneStatus),"
                + "if(trim(concat(Title,TitleCont)) = '', null, trim(concat(Title, ' ',TitleCont))) ,"
                + "if(trim(MIMNumber) = '', null, MIMNumber),"
                + "if(trim(concat(Comments,CommentsCont)) = '', null, trim(concat(Comments, ' ',CommentsCont))),"
                + "if(trim(MouseCorr) = '', null, MouseCorr),"
                + "if(trim(Reference) = '', null, Reference)"
                + " from "
                + OMIMTables.getInstance().OMIMGENEMAPTEMP);

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + OMIMTables.getInstance().OMIMGENEMAP, "WID"));

        splitGeneSymbol();
        splitMethod();

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + OMIMTables.getInstance().OMIMGENEMAPTEMP);
    }

    /**
     * This metho split the GeneSymbol field included into the genemap file to a
     * string array repeating the citation id
     */
    private void splitGeneSymbol() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP);

        whdbmsFactory.splitField(ParseFiles.getInstance().getPrintWriterFromName(OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP),
                "select o.WID,ot.GeneSymbol from "
                + OMIMTables.getInstance().OMIMGENEMAP
                + " o inner join "
                + OMIMTables.getInstance().OMIMGENEMAPTEMP
                + "  ot on ot.Number = o.Number",
                "WID", "GeneSymbol", ",");

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP + " file");
        ParseFiles.getInstance().getPrintWriterFromName(OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP).close();
        whdbmsFactory.loadTSVFile(OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP, ParseFiles.getInstance().getFileAbsolutName(OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP));

        whdbmsFactory.executeUpdate("INSERT INTO "
                + OMIMTables.OMIMGENEMAP_HAS_GENESYMBOL
                + " (OMIMGeneMap_WID,GeneSymbol) "
                + " select OMIMGeneMap_WID,trim(GeneSymbol) from "
                + OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP
                + " where trim(GeneSymbol) != '' "
                + " group by OMIMGeneMap_WID,GeneSymbol");

        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + OMIMTables.getInstance().OMIMGENEMAP_HAS_GENESYMBOLTEMP);
    }

    /**
     * This metho split the GeneSymbol field included into the genemap file to a
     * string array repeating the citation id
     */
    private void splitMethod() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + OMIMTables.getInstance().OMIMMETHODTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + OMIMTables.getInstance().OMIMMETHODTEMP);

        whdbmsFactory.splitField(ParseFiles.getInstance().getPrintWriterFromName(OMIMTables.getInstance().OMIMMETHODTEMP),
                "select o.WID,ot.Method from "
                + OMIMTables.getInstance().OMIMGENEMAP
                + " o inner join "
                + OMIMTables.getInstance().OMIMGENEMAPTEMP
                + "  ot on ot.Number = o.Number",
                "WID", "Method", ",");

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + OMIMTables.getInstance().OMIMMETHODTEMP + " file");
        ParseFiles.getInstance().getPrintWriterFromName(OMIMTables.getInstance().OMIMMETHODTEMP).close();
        whdbmsFactory.loadTSVFile(OMIMTables.getInstance().OMIMMETHODTEMP, ParseFiles.getInstance().getFileAbsolutName(OMIMTables.getInstance().OMIMMETHODTEMP));

        whdbmsFactory.executeUpdate("ALTER TABLE " + OMIMTables.getInstance().OMIMMETHOD + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("INSERT INTO "
                + OMIMTables.getInstance().OMIMMETHOD
                + " (Method) "
                + " select trim(Method) from "
                + OMIMTables.getInstance().OMIMMETHODTEMP
                + " where trim(Method) != '' "
                + " group by Method");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + OMIMTables.getInstance().OMIMMETHOD, "WID"));

        whdbmsFactory.executeUpdate("INSERT INTO "
                + OMIMTables.OMIMGENEMAP_HAS_OMIMMETHOD
                + " (OMIMGeneMap_WID,OMIMMethod_WID) "
                + " select t.WID,m.WID from "
                + OMIMTables.getInstance().OMIMMETHODTEMP
                + " t inner join "
                + OMIMTables.getInstance().OMIMMETHOD
                + " m on m.Method = t.Method "
                + " group by t.WID,m.WID");

        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + OMIMTables.getInstance().OMIMMETHODTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + OMIMTables.getInstance().OMIMMETHODTEMP);
    }
}
