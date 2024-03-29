package cn.lead2success.ddlutils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.lead2success.ddlutils.custom.pojo.QueryParams;
import cn.lead2success.ddlutils.io.DatabaseIO;
import cn.lead2success.ddlutils.model.*;
import cn.lead2success.ddlutils.util.SqlTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DDLHelper {
    private final Log _log = LogFactory.getLog(DDLHelper.class);
    private final Pattern sqlPattern = Pattern.compile("(CREATE TABLE|INSERT INTO|DROP TABLE|CREATE INDEX \\S* ON|COMMENT ON TABLE|COMMENT ON COLUMN) ([a-zA-Z_]*)([\\s\\.]*)([\\s\\S]*)");
    public DataSource dataSource;
    private Platform platform;
    private String connSchema;
    private String connCatalog;


    /**
     * 构造函数，创建新实例
     * @param dataSource
     */
    public DDLHelper(DataSource dataSource) {
        this.dataSource = dataSource;
        this.platform = PlatformFactory.createNewPlatformInstance(dataSource);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connCatalog = connection.getCatalog();
            connSchema = connection.getSchema();
        } catch (AbstractMethodError e) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(connection);
        }
    }

    /**
     * 获取数据结构
     * name – The name of the resulting database; null when the default name (the catalog) is desired which might be null itself though
     * catalog – The catalog to access in the database; use null for the default value
     * schema – The schema to access in the database; use null for the default value
     * tableTypes – The table types to process; use null or an empty list for the default ones
     * queryParams -    tablePattern:String 表名筛选正则，includeTables：List<String>：包含表名，withColumns:返回字典等信息 true;
     */
    public synchronized Database getDatabase(String name, String catalog, String schema, String[] tableTypes, QueryParams queryParams) {
        try {
            platform.setQueryParams(queryParams);
            catalog = StrUtil.isNotEmpty(catalog) ? catalog : connCatalog;
            schema = StrUtil.isNotEmpty(schema) ? schema : connSchema;
            return platform.readModelFromDatabase(name, catalog, schema, tableTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            platform.setQueryParams(null);
        }
    }

    /*
     * @描述
     * @参数 schema:
     *       removeTable:
     *       warnSize:
     *       continueOnError:
     * @返回值  cn.lead2success.ddlutils.DDLResult
     * @创建人  Allan
     * @创建时间  2020-6-20 17:48
     */
    public List<String> getAlterModelSql(String schema, Boolean removeTable, Boolean removeColumn, Boolean removeIndex, String dbCatalog, String dbSchema) {
        dbCatalog = StrUtil.isNotEmpty(dbCatalog) ? dbCatalog : connCatalog;
        dbSchema = StrUtil.isNotEmpty(dbSchema) ? dbSchema : connSchema;
        DatabaseIO dbIO = new DatabaseIO();
        dbIO.setValidateXml(false);
        Database targetModel = dbIO.read(new StringReader(schema));
        Map<String, Table> targetMaps = Arrays.stream(targetModel.getTables()).collect(Collectors.toMap(item -> item.getName().toUpperCase(), item -> item));
        if (!removeTable) {
            List<String> schemaTables = targetMaps.keySet().stream().map(item -> item.toUpperCase()).collect(Collectors.toList());
            platform.setQueryParams(new QueryParams(schemaTables));
        }

        Database oldDatabase = null;
        try {
            oldDatabase = platform.readModelFromDatabase("model", dbCatalog, dbSchema, null);
        } finally {
            platform.setQueryParams(null);
        }
        if (!removeTable) {
            List<Table> oldMaps = Arrays.stream(oldDatabase.getTables()).filter(table -> (!targetMaps.containsKey(table.getName().toUpperCase()))).collect(Collectors.toList());
            List<String> moreTable = new ArrayList<>();
            for (Table table : oldMaps) {
                moreTable.add(table.getName());
                oldDatabase.removeTable(table);
            }
        }
        if (!removeColumn) {
            Map<String, Table> oldTableMap = Arrays.stream(oldDatabase.getTables()).collect(Collectors.toMap(item -> item.getName().toUpperCase(), item -> item));
            for (Table targetTable : targetModel.getTables()) {
                Table oldTable = oldTableMap.get(targetTable.getName().toUpperCase());
                if (null != oldTable) {
                    for (Column oldColumn : oldTable.getColumns()) {
                        long count = Arrays.stream(targetTable.getColumns()).filter(item -> item.getName().equalsIgnoreCase(oldColumn.getName())).count();
                        if (count <= 0) {
                            targetTable.addColumn(oldColumn);
                        }
                    }
                }
            }
        }
        if (!removeIndex) {
            Map<String, Table> oldTableMap = Arrays.stream(oldDatabase.getTables()).collect(Collectors.toMap(item -> item.getName().toUpperCase(), item -> item));
            for (Table targetTable : targetModel.getTables()) {
                Table oldTable = oldTableMap.get(targetTable.getName().toUpperCase());
                if (null != oldTable) {
                    for (Index index : oldTable.getIndices()) {
                        long count = Arrays.stream(targetTable.getIndices()).filter(item -> item.getName().equalsIgnoreCase(index.getName())).count();
                        if (count <= 0) {
                            oldTable.removeIndex(index);
                        }
                    }
                }
            }
        } else {
            Map<String, Table> oldTableMap = Arrays.stream(oldDatabase.getTables()).collect(Collectors.toMap(item -> item.getName().toUpperCase(), item -> item));
            for (Table targetTable : targetModel.getTables()) {
                Table oldTable = oldTableMap.get(targetTable.getName().toUpperCase());
                if (null != oldTable) {
                    for (Index index : oldTable.getIndices()) {
                        String indexName = index.getName().toLowerCase();
                        if (index instanceof UniqueIndex && (indexName.indexOf("pk") >= 0 || indexName.indexOf("pkey") >= 0)) {
                            oldTable.removeIndex(index);
                        }
                    }
                }
            }
        }

        String sql = platform.getAlterModelSql(oldDatabase, targetModel);
        SqlTokenizer tokenizer = new SqlTokenizer(sql.trim());
        LinkedHashMap<String, DDLResult.SqlItem> sqlMap = new LinkedHashMap<>();
        List<String> sqlList = new ArrayList<>();
        while (tokenizer.hasMoreStatements()) {
            String command = tokenizer.getNextStatement().trim();
            sqlList.add(command);
        }
        return sqlList;
    }

    /*
     * @描述
     * @参数 schema:
     *       removeTable:
     *       warnSize:
     *       continueOnError:
     * @返回值  cn.lead2success.ddlutils.DDLResult
     * @创建人  Allan
     * @创建时间  2020-6-20 17:48
     */
    public DDLResult getAlterSqlResult(String schema, Boolean removeTable, Boolean removeColumn, Boolean removeIndex, String dbCatalog, String dbSchema) {
        dbCatalog = StrUtil.isNotEmpty(dbCatalog) ? dbCatalog : connCatalog;
        dbSchema = StrUtil.isNotEmpty(dbSchema) ? dbSchema : connSchema;

        List<String> sqls = getAlterModelSql(schema, removeTable, removeColumn, removeIndex, dbCatalog, dbSchema);

        LinkedHashMap<String, DDLResult.SqlItem> sqlMap = new LinkedHashMap<>();
        for (String sql : sqls) {
            String tabName = getMatchVal(sqlPattern, sql, 2);
            tabName = tabName.endsWith("_") ? tabName.substring(0, tabName.length() - 1) : tabName;
            if (sqlMap.containsKey(tabName)) {
                sqlMap.get(tabName).addSql(sql);
            } else {
                DDLResult.SqlItem sqlItem = new DDLResult.SqlItem(tabName);
                sqlItem.addSql(sql);
                sqlMap.put(tabName, sqlItem);
            }
        }
        return new DDLResult(new ArrayList<>(sqlMap.values()));
    }

    public DDLResult execute(DDLResult ddlResult) {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            List<DDLResult.SqlItem> sqlItems = ddlResult.getSqlItems();
            for (DDLResult.SqlItem sqlItem : sqlItems) {
                if (sqlItem.isIgnore()) {
                    continue;
                }
                String errSql = "";
                try {
                    for (String sql : sqlItem.getSqls()) {
                        errSql = sql;
                        _log.debug("exec:" + sql);
                        statement.execute(sql);
                    }
                    sqlItem.setStatus("1");
                } catch (Exception e) {
                    sqlItem.setException(new DatabaseOperationException(errSql, e));
                    sqlItem.setStatus("2");
                    break;
                }
            }
            ddlResult.refresh();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(statement);
            IoUtil.close(connection);
        }
        return ddlResult;
    }

    public boolean execute(List<String> sqls) {
        List<String> insertTables = new ArrayList<>();
        Statement statement = null;
        int i = 0;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            for (; i < sqls.size(); i++) {
                String sql = sqls.get(i).trim().toUpperCase(Locale.ROOT);
                String[] sqlArr = sql.split("\\s");
                String tableName = sqlArr.length >= 3 ? sqlArr[2].trim() : "";
                if (sql.startsWith("INSERT INTO")) {
                    insertTables.add(tableName);
                }
                if (sql.startsWith("DROP TABLE")
                        && !tableName.endsWith("_")
                        && !insertTables.contains(tableName + "_")) {  //有备份表才允许删除原表
                    throw new RuntimeException(String.format("脚本更新冲突,删除'%s'表失败", tableName));
                }
                statement.execute(sqls.get(i));
                _log.debug("exec:" + sqls.get(i));
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("执行脚本第%d行发生错误：\n%s", i + 1, String.join("\n", sqls)), e);
        } finally {
            IoUtil.close(statement);
            IoUtil.close(connection);
        }
        return true;
    }

    /*
     * @描述
     * @参数  仅用于辅助生成数据结构模板当前时间默认值
     *         反向生成模板有问题
     * @返回值  java.lang.String
     * @创建人  Allan
     * @创建时间  2020-6-22 9:12
     */
    @Deprecated
    public String getModelSchemaFromDB(String dbCatalog, String dbSchema, QueryParams queryParams) {
        try {
            platform.setQueryParams(queryParams);
            dbCatalog = StrUtil.isNotEmpty(dbCatalog) ? dbCatalog : connCatalog;
            dbSchema = StrUtil.isNotEmpty(dbSchema) ? dbSchema : connSchema;
            Database database = platform.readModelFromDatabase("model", dbCatalog, dbSchema, null);
            DatabaseIO dbIO = new DatabaseIO();
            StringWriter writer = new StringWriter();
            dbIO.write(database, writer);
            return writer.toString();
        } finally {
            platform.setQueryParams(null);
        }
    }

    public String getSchecule(String dbCatalog, String dbSchema, QueryParams queryParams) {
        try {
            platform.setQueryParams(queryParams);
            platform.setSqlCommentsOn(true);
            dbCatalog = StrUtil.isNotEmpty(dbCatalog) ? dbCatalog : connCatalog;
            dbSchema = StrUtil.isNotEmpty(dbSchema) ? dbSchema : connSchema;
            Database database = platform.readModelFromDatabase("model", dbCatalog, dbSchema, null);
            DatabaseIO dbIO = new DatabaseIO();
            StringWriter stringWriter = new StringWriter();
            dbIO.write(database, stringWriter);
            return stringWriter.toString();
        } finally {
            platform.setQueryParams(null);
        }
    }


    private String getMatchVal(Pattern pattern, String text, int matchIdx) {
        Matcher m = pattern.matcher(text);
        String str = "";
        if (m.find()) {
            str = m.group(matchIdx);
        }
        return str;
    }
}
