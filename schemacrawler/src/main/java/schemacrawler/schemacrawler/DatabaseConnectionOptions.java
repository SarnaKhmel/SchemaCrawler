/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2009, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package schemacrawler.schemacrawler;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import schemacrawler.utility.Utility;

public class DatabaseConnectionOptions
  implements Options
{

  private static final long serialVersionUID = -8141436553988174836L;

  private static final Logger LOGGER = Logger
    .getLogger(DatabaseConnectionOptions.class.getName());

  private static final String DRIVER = "driver";
  private static final String URL = "url";
  private static final String USER = "user";
  private static final String PASSWORD = "password";

  private static void loadJdbcDriver(final String jdbcDriverClassName)
    throws SchemaCrawlerException
  {
    if (Utility.isBlank(jdbcDriverClassName))
    {
      throw new SchemaCrawlerException("No JDBC driver class provided");
    }
    try
    {
      Class.forName(jdbcDriverClassName);
    }
    catch (final Exception e)
    {
      throw new SchemaCrawlerException("Could not load JDBC driver, "
                                       + jdbcDriverClassName);
    }
  }

  private final String connectionUrl;
  private String user;
  private String password;
  private final Properties properties = new Properties();

  public DatabaseConnectionOptions(final Map<String, String> properties)
    throws SchemaCrawlerException
  {
    if (properties == null)
    {
      throw new SchemaCrawlerException("No connection properties provided");
    }

    connectionUrl = properties.get(URL);
    if (Utility.isBlank(connectionUrl))
    {
      throw new SchemaCrawlerException("No database connection URL provided");
    }
    loadJdbcDriver(properties.get(DRIVER));

    user = properties.get(USER);
    password = properties.get(PASSWORD);
    copyOtherConnectionProperties(properties);
  }

  public DatabaseConnectionOptions(final String jdbcDriverClassName,
                                   final String connectionUrl)
    throws SchemaCrawlerException
  {
    if (Utility.isBlank(connectionUrl))
    {
      throw new SchemaCrawlerException("No database connection URL provided");
    }
    this.connectionUrl = connectionUrl;
    loadJdbcDriver(jdbcDriverClassName);

    if (properties != null)
    {
      user = properties.getProperty(USER);
      password = properties.getProperty(PASSWORD);
    }
  }

  public String getConnectionUrl()
  {
    return connectionUrl;
  }

  public Driver getJdbcDriver()
  {
    try
    {
      return DriverManager.getDriver(connectionUrl);
    }
    catch (final SQLException e)
    {
      LOGGER.log(Level.WARNING,
                 "Could not get a database driver for database connection URL "
                     + connectionUrl);
      return null;
    }
  }

  public String getPassword()
  {
    return password;
  }

  public Properties getProperties()
  {
    return new Properties(properties);
  }

  public String getUser()
  {
    return user;
  }

  public Connection createConnection()
    throws SQLException
  {
    return DriverManager.getConnection(connectionUrl, user, password);
  }

  public void setPassword(final String password)
  {
    this.password = password;
  }

  public void setUser(final String user)
  {
    this.user = user;
  }

  private void copyOtherConnectionProperties(final Map<String, String> properties)
  {
    if (properties != null)
    {
      for (final Entry<String, String> property: properties.entrySet())
      {
        final String propertyName = property.getKey();
        if (!(DRIVER.equals(propertyName) || URL.equals(propertyName)
              || USER.equals(propertyName) || PASSWORD.equals(propertyName)))
        {
          this.properties.setProperty(propertyName, property.getValue());
        }
      }
    }
  }

}
