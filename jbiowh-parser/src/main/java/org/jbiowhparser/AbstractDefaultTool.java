package org.jbiowhparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.jbiowhcore.basic.JBioWHUserData;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhdbms.dbms.mysql.WHMySQL;
import org.jbiowhpersistence.utils.entitymanager.JBioWHPersistence;

/**
 * This class is to parse the default options on the command line tool using
 * JBioWH
 *
 * $Author: r78v10a07@gmail.com $
 *
 * $LastChangedDate: 2013-08-27 09:24:09 +0200 (Tue, 27 Aug 2013) $
 *
 * $LastChangedRevision: 656 $
 *
 * @since Aug 31, 2012
 */
public abstract class AbstractDefaultTool {

    protected String driver;                  // JDBC Mysql Driver
    protected String url;                     // JDBC DB Url 
    protected String user;                    // DBMS User  
    protected String passwd;                  // DBMS Password
    protected WHDBMSFactory whdbmsFactory;    // JBioWHPersistence Factory    
    protected PrintWriter file;               // File to print the output

    /**
     * This method parse the command line arguments with the JBioWH options
     *
     * @param args the command line arguments
     * @throws FileNotFoundException
     */
    protected abstract void parseOptions(String[] args) throws FileNotFoundException;

    /**
     * Method to print the help
     */
    protected abstract void printHelp();

    /**
     * This method print in both file, the result and the System.out and
     * includes the end of line
     *
     * @param toPrint string to be printed
     */
    protected void println(String toPrint) {
        //System.out.println(toPrint);
        file.println(toPrint);
    }

    /**
     * This method print in both file, the result and the System.out and does
     * not includes the end of line
     *
     * @param toPrint string to be printed
     */
    protected void print(String toPrint) {
        System.out.print(toPrint);
        file.print(toPrint);
    }

    /**
     * Return the JBioWHPersistence factory
     *
     * @return the JBioWHPersistence factory
     */
    public WHDBMSFactory getWhdbmsFactory() {
        return whdbmsFactory;
    }

    /**
     * Method to parse the command line arguments
     *
     * @param parser Parses command line arguments, using a syntax that attempts
     * to take from the best of POSIX getopt() and GNU getopt_long()
     * @param args arguments to parse
     * @param hasArguments true if the tool needs arguments
     * @return a representation of a group of detected command line options,
     * their arguments, and non-option arguments
     * @throws FileNotFoundException
     */
    protected OptionSet parseDefaultOptions(OptionParser parser, String[] args, boolean hasArguments) throws FileNotFoundException {

        parser.accepts("v");
        parser.accepts("h");
        parser.accepts("help");
        parser.accepts("f").withRequiredArg().ofType(String.class);
        parser.accepts("driver").withRequiredArg().ofType(String.class);
        parser.accepts("url").withRequiredArg().ofType(String.class);
        parser.accepts("user").withRequiredArg().ofType(String.class);
        parser.accepts("passwd").withRequiredArg().ofType(String.class);

        OptionSet options = parser.parse(args);

        if ((hasArguments && args.length == 0) || options.has("h") || options.has("help")) {
            printHelp();
        }

        if (options.has("v")) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().INFO);
        } else {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().NONE);
        }

        if (options.has("f")) {
            if (options.hasArgument("f")) {
                file = new PrintWriter(new File((String) options.valueOf("f")));
            } else {
                System.err.println("Bad file parameter");
                printDefaultHelp();
            }
        } else {
            file = new PrintWriter(new File("result.txt"));
        }

        if (options.has("driver")) {
            if (options.hasArgument("driver")) {
                driver = (String) options.valueOf("driver");
            } else {
                System.err.println("Bad driver parameter");
                printDefaultHelp();
            }
        } else {
            driver = "com.mysql.jdbc.Driver";
        }

        if (options.has("url")) {
            if (options.hasArgument("url")) {
                url = (String) options.valueOf("url");
            } else {
                System.err.println("Bad url parameter");
                printDefaultHelp();
            }
        } else {
            url = "jdbc:mysql://hydrax.icgeb.trieste.it:3307/biowh";
        }

        if (options.has("user")) {
            if (options.hasArgument("user")) {
                user = (String) options.valueOf("user");
            } else {
                System.err.println("Bad user parameter");
                printDefaultHelp();
            }
        } else {
            user = "biowh";
        }

        if (options.has("passwd")) {
            if (options.hasArgument("passwd")) {
                passwd = (String) options.valueOf("passwd");
            } else {
                System.err.println("Bad passwd parameter");
                printDefaultHelp();
            }
        } else {
            passwd = "mypass";
        }

        return options;
    }

    /**
     * Parse the command line option<br>
     *
     * @param options a representation of a group of detected command line
     * options, their arguments, and non-option arguments
     * @param option the option to be parsed
     * @param defaultValue the default value for this option, null is not
     * default value
     * @param hasArgument true if the option has an argument
     * @param isObligatory true if the option is obligatory
     * @return the argument for the option or true if the option does not have
     * argument but is present on the command line
     */
    protected Object parseOption(OptionSet options, String option, Object defaultValue, boolean hasArgument, boolean isObligatory) {
        if (options.has(option)) {
            if (hasArgument) {
                if (options.hasArgument(option)) {
                    return options.valueOf(option);
                } else {
                    System.out.println("Bad " + option + " parameter");
                    printHelp();
                }
            } else {
                return true;
            }
        } else {
            if (isObligatory) {
                System.out.println("Set the " + option + " parameter");
                printHelp();
            }
            return defaultValue;
        }
        return null;
    }

    /**
     * Method to print the help
     */
    protected void printDefaultHelp() {
        System.out.println("\nDefault arguments:");
        System.out.println("\t-h Print this help");
        System.out.println("\t-v verbose");
        System.out.println("\t-f File name to print the result (default: result.txt)");
        System.out.println("\t--driver Java MySQl driver (default: com.mysql.jdbc.Driver)");
        System.out.println("\t--url Database JDBC Url (default: jdbc:mysql://hydrax.icgeb.trieste.it:3307/biowh)");
        System.out.println("\t--user DB user (default: biowh)");
        System.out.println("\t--passwd DB password (default: mypass)");
        System.exit(0);
    }

    /**
     * Create the JBioWHPersistence factory and open the connection with the
     * DBMS
     *
     * @param isMain true if the opened JBioWHPersistenceFactory should be set
     * as Main schema
     * @param createSchema true if the schema have to be created
     * @param startThread true to setup a thread to check the connection every
     * hour
     * @throws SQLException
     */
    protected void openConnection(boolean isMain, boolean createSchema, boolean startThread) throws SQLException {
        JBioWHDBMS.getInstance().setWhdbmsFactory(new WHMySQL(driver, url, user, passwd, isMain));
        JBioWHPersistence.getInstance().openSchema(new JBioWHUserData(driver, url, user, passwd, isMain), startThread, isMain);
    }

    /**
     * Close the connection with the DBMS
     *
     * @throws SQLException
     */
    protected void closeConnection() throws SQLException {
        if (file != null) {
            file.close();
        }
        JBioWHPersistence.getInstance().closeAll();
    }
}
