package cn.lead2success.ddlutils;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import cn.lead2success.ddlutils.platform.db2.Db2Platform;
import cn.lead2success.ddlutils.platform.db2.Db2v11Platform;
import cn.lead2success.ddlutils.platform.db2.Db2v8Platform;
import cn.lead2success.ddlutils.platform.derby.DerbyPlatform;
import cn.lead2success.ddlutils.platform.h2.H2Platform;
import cn.lead2success.ddlutils.platform.hsqldb.HsqlDbPlatform;
import cn.lead2success.ddlutils.platform.mssql.MSSqlPlatform;
import cn.lead2success.ddlutils.platform.mysql.MySql50Platform;
import cn.lead2success.ddlutils.platform.mysql.MySqlPlatform;
import cn.lead2success.ddlutils.platform.oracle.Oracle10Platform;
import cn.lead2success.ddlutils.platform.oracle.Oracle12Platform;
import cn.lead2success.ddlutils.platform.oracle.Oracle8Platform;
import cn.lead2success.ddlutils.platform.oracle.Oracle9Platform;
import cn.lead2success.ddlutils.platform.postgresql.PostgreSqlPlatform;

/**
 * A factory of {@link Platform} instances based on a case
 * insensitive database name. Note that this is a convenience class as the platforms
 * can also simply be created via their constructors.
 * 
 * @version $Revision: 209952 $
 */
public class PlatformFactory
{
    /** The database name -> platform map. */
    private static Map<String, Class<?>> _platforms = null;

    /**
     * Returns the platform map.
     * 
     * @return The platform list
     */
    private static synchronized Map<String, Class<?>> getPlatforms()
    {
        if (_platforms == null)
        {
            // lazy initialization
            _platforms = new HashMap<>();
            registerPlatforms();
        }
        return _platforms;
    }
    
    /**
     * Creates a new platform for the given (case insensitive) database name
     * or returns null if the database is not recognized.
     * 
     * @param databaseName The name of the database (case is not important)
     * @return The platform or <code>null</code> if the database is not supported
     * @throws DdlUtilsException 
     */
    public static synchronized Platform createNewPlatformInstance(String databaseName) throws DdlUtilsException
    {
        Class<?> platformClass = getPlatforms().get(databaseName.toLowerCase());

        try
        {
            return platformClass != null ? (Platform)platformClass.newInstance() : null;
        }
        catch (Exception ex)
        {
            throw new DdlUtilsException("Could not create platform for database "+databaseName, ex);
        }
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(String, String)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}. Note that no database connection is
     * established when using this method.
     * 
     * @param jdbcDriver        The jdbc driver
     * @param jdbcConnectionUrl The connection url
     * @return The platform or <code>null</code> if the database is not supported
     * @throws DdlUtilsException 
     */
    public static synchronized Platform createNewPlatformInstance(String jdbcDriver, String jdbcConnectionUrl) throws DdlUtilsException
    {
        return createNewPlatformInstance(new PlatformUtils().determineDatabaseType(jdbcDriver, jdbcConnectionUrl));
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(DataSource)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}. Note that this method sets the data source
     * at the returned platform instance (method {@link Platform#setDataSource(DataSource)}).
     * 
     * @param dataSource The data source for the database
     * @return The platform or <code>null</code> if the database is not supported
     * @throws DdlUtilsException 
     */
    public static synchronized Platform createNewPlatformInstance(DataSource dataSource) throws DdlUtilsException
    {
        Platform platform = createNewPlatformInstance(new PlatformUtils().determineDatabaseType(dataSource));

        platform.setDataSource(dataSource);
        return platform;
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(DataSource)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}. Note that this method sets the data source
     * at the returned platform instance (method {@link Platform#setDataSource(DataSource)}).
     * 
     * @param dataSource The data source for the database
     * @param username   The user name to use for connecting to the database
     * @param password   The password to use for connecting to the database
     * @return The platform or <code>null</code> if the database is not supported
     * @throws DdlUtilsException 
     */
    public static synchronized Platform createNewPlatformInstance(DataSource dataSource, String username, String password) throws DdlUtilsException
    {
        Platform platform = createNewPlatformInstance(new PlatformUtils().determineDatabaseType(dataSource, username, password));

        platform.setDataSource(dataSource);
        platform.setUsername(username);
        platform.setPassword(password);
        return platform;
    }

    /**
     * Returns a list of all supported platforms.
     * 
     * @return The names of the currently registered platforms
     */
    public static synchronized String[] getSupportedPlatforms()
    {
        return (String[])getPlatforms().keySet().toArray(new String[0]);
    }

    /**
     * Determines whether the indicated platform is supported.
     * 
     * @param platformName The name of the platform
     * @return <code>true</code> if the platform is supported
     */
    public static boolean isPlatformSupported(String platformName)
    {
        return getPlatforms().containsKey(platformName.toLowerCase());
    }

    /**
     * Registers a new platform.
     * 
     * @param platformName  The platform name
     * @param platformClass The platform class which must implement the {@link Platform} interface
     */
    public static synchronized void registerPlatform(String platformName, Class<?> platformClass)
    {
        addPlatform(getPlatforms(), platformName, platformClass);
    }

    /**
     * Registers the known platforms.
     */
    private static void registerPlatforms()
    {
        addPlatform(_platforms, Db2Platform.DATABASENAME,         Db2Platform.class);
        addPlatform(_platforms, Db2v8Platform.DATABASENAME,       Db2v8Platform.class);
        addPlatform(_platforms, Db2v11Platform.DATABASENAME,      Db2v11Platform.class);
        addPlatform(_platforms, DerbyPlatform.DATABASENAME,       DerbyPlatform.class);
        addPlatform(_platforms, HsqlDbPlatform.DATABASENAME,      HsqlDbPlatform.class);
        addPlatform(_platforms, H2Platform.DATABASENAME,      	  H2Platform.class);
        addPlatform(_platforms, MSSqlPlatform.DATABASENAME,       MSSqlPlatform.class);
        addPlatform(_platforms, MySqlPlatform.DATABASENAME,       MySqlPlatform.class);
        addPlatform(_platforms, MySql50Platform.DATABASENAME,     MySql50Platform.class);
        addPlatform(_platforms, Oracle8Platform.DATABASENAME,     Oracle8Platform.class);
        addPlatform(_platforms, Oracle9Platform.DATABASENAME,     Oracle9Platform.class);
        addPlatform(_platforms, Oracle10Platform.DATABASENAME,    Oracle10Platform.class);
        addPlatform(_platforms, Oracle12Platform.DATABASENAME,    Oracle12Platform.class);
        addPlatform(_platforms, PostgreSqlPlatform.DATABASENAME,  PostgreSqlPlatform.class);
    }

    /**
     * Registers a new platform.
     * 
     * @param platformMap   The map to add the platform info to 
     * @param platformName  The platform name
     * @param platformClass The platform class which must implement the {@link Platform} interface
     */
    private static synchronized void addPlatform(Map<String, Class<?>> platformMap, String platformName, Class<?> platformClass)
    {
        if (!Platform.class.isAssignableFrom(platformClass))
        {
            throw new IllegalArgumentException("Cannot register class "+platformClass.getName()+" because it does not implement the "+Platform.class.getName()+" interface");
        }
        platformMap.put(platformName.toLowerCase(), platformClass);        
    }
}
