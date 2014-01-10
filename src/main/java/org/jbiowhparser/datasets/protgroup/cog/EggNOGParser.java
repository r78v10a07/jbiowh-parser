package org.jbiowhparser.datasets.protgroup.cog;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhparser.ParseFactory;
import org.jbiowhparser.ParserBasic;
import org.jbiowhparser.datasets.protgroup.cog.files.COGFunParser;
import org.jbiowhparser.datasets.protgroup.cog.files.EggNOGParserFiles;
import org.jbiowhparser.datasets.protgroup.cog.links.COGLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.cog.COGTables;

/**
 * This class is the eggNOG parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Jan 8, 2014
 */
public class EggNOGParser extends ParserBasic implements ParseFactory {

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        File dir = new File(DataSetPersistence.getInstance().getDirectory());
        
        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : COGTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }
        
        if (dir.isDirectory()) {
            ParseFiles.getInstance().start(COGTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
            
            new COGFunParser().run("eggnogv4.funccats.txt");  
            new EggNOGParserFiles().parserFiles();
            
            ParseFiles.getInstance().closeAllPrintWriter();
            whdbmsFactory.loadTSVTables(COGTables.getInstance().getTables());
            ParseFiles.getInstance().end();
            
            if (DataSetPersistence.getInstance().isRunlinks()) {
                COGLinks.getInstance().runLink();
            }
        }
        
        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(COGTables.getInstance().getTables());
    }

}
