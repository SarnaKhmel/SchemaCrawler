#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import schemacrawler.schemacrawler.DatabaseServerType;
import schemacrawler.tools.databaseconnector.DatabaseConnector;
import schemacrawler.tools.iosource.ClasspathInputResource;

/**
 * SchemaCrawler database support plug-in.
 * 
 * @see <a href="https://www.schemacrawler.com">SchemaCrawler</a>
 * 
 * @author Automatically generated by SchemaCrawler 15.06.01
 */
public final class NewDBDatabaseConnector
  extends DatabaseConnector
{

  private static final Logger LOGGER = Logger
    .getLogger(NewDBDatabaseConnector.class.getName());

  private static final DatabaseServerType DB_SERVER_TYPE = new DatabaseServerType("newdb",
                                                                                  "NewDB");

  public NewDBDatabaseConnector()
    throws IOException
  {
    super(DB_SERVER_TYPE,
          new ClasspathInputResource("/help/Connections.newdb.txt"),
          new ClasspathInputResource("/schemacrawler-newdb.config.properties"),
          (informationSchemaViewsBuilder,
           connection) -> informationSchemaViewsBuilder
             .fromResourceFolder("/newdb.information_schema"),
          url -> Pattern.matches("jdbc:newdb:.*", url));
    // SchemaCrawler will control output of log messages if you use JDK
    // logging
    LOGGER.log(Level.INFO, "Loaded plugin for NewDB");
  }

}
