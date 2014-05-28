package org.jbiowhparser.datasets.protgroup.cog;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.protgroup.cog.files.COGFunParser;
import org.jbiowhparser.datasets.protgroup.cog.files.COGMemberIdParser;
import org.jbiowhparser.datasets.protgroup.cog.files.COGOrgParser;
import org.jbiowhparser.datasets.protgroup.cog.files.KOGLSEParser;
import org.jbiowhparser.datasets.protgroup.cog.files.KOGTWhogParser;
import org.jbiowhparser.datasets.protgroup.cog.files.WhogLikeParser;
import org.jbiowhparser.datasets.protgroup.cog.links.COGLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.cog.COGTables;

/**
 * This class is the COG database parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 19, 2013
 */
public class COGParser extends ParserFactory implements JBioWHParser {
    
    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        File dir = new File(DataSetPersistence.getInstance().getDirectory());
        
        if (DataSetPersistence.getInstance().isDroptables()) {
            for (String table : COGTables.getInstance().getTables()) {
                VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + table);
                whdbmsFactory.executeUpdate("TRUNCATE TABLE " + table);
            }
        }
        
        if (dir.isDirectory()) {
            ParseFiles.getInstance().start(COGTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());
            
            new COGFunParser().run("COG/fun.txt");
            new WhogLikeParser().run("COG/whog");
            new COGMemberIdParser().run("COG/myva=gb");
            new COGOrgParser().run("COG/org.txt");
            
            new COGFunParser().run("KOG/fun.txt");
            new WhogLikeParser().run("KOG/kog");
            new COGMemberIdParser().run("KOG/kyva=gb");
            new KOGLSEParser().run("KOG/lse");
            new KOGTWhogParser().run("KOG/twog");
            
            
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
