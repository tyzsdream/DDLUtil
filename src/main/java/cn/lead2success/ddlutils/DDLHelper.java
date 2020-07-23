package cn.lead2success.ddlutils;

import cn.hutool.core.io.IoUtil;
import cn.lead2success.ddlutils.io.DatabaseIO;
import cn.lead2success.ddlutils.model.Database;
import cn.lead2success.ddlutils.model.Table;
import cn.lead2success.ddlutils.util.SqlTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.StringReader;
import java.io.StringWriter;
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

    public DDLHelper(DataSource dataSource) {
        this.dataSource = dataSource;
        this.platform = PlatformFactory.createNewPlatformInstance(dataSource);
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
    public DDLResult getAlterModelSql(String schema, Boolean removeTable) {
        Database oldDatabase = platform.readModelFromDatabase("model");

        DatabaseIO dbIO = new DatabaseIO();
        dbIO.setValidateXml(false);
        Database targetModel = dbIO.read(new StringReader(schema));
        Map<String, Table> targetMaps = Arrays.stream(targetModel.getTables()).collect(Collectors.toMap(item -> item.getName(), item -> item));
        List<Table> oldMaps = Arrays.stream(oldDatabase.getTables()).filter(table -> (!targetMaps.containsKey(table.getName()))).collect(Collectors.toList());

        if (!removeTable) {
            List<String> moreTable = new ArrayList<>();
            for (Table table : oldMaps) {
                moreTable.add(table.getName());
                oldDatabase.removeTable(table);
            }
        }

        String sql = platform.getAlterModelSql(oldDatabase, targetModel);

        SqlTokenizer tokenizer = new SqlTokenizer(sql.trim());
        LinkedHashMap<String, DDLResult.SqlItem> sqlMap = new LinkedHashMap<>();
        while (tokenizer.hasMoreStatements()) {
            String command = tokenizer.getNextStatement().trim();
            String tabName = getMatchVal(sqlPattern, command, 2);
            tabName = tabName.endsWith("_") ? tabName.substring(0, tabName.length() - 1) : tabName;
            if (sqlMap.containsKey(tabName)) {
                sqlMap.get(tabName).addSql(command);
            } else {
                DDLResult.SqlItem sqlItem = new DDLResult.SqlItem(tabName);
                sqlItem.addSql(command);
                sqlMap.put(tabName, sqlItem);
            }
        }
        return new DDLResult(new ArrayList<>(sqlMap.values()));
    }

    public DDLResult execute(DDLResult ddlResult) {
        Statement statement = null;
        try {
            statement = dataSource.getConnection().createStatement();
            List<DDLResult.SqlItem> sqlItems = ddlResult.getSqlItems();
            for (DDLResult.SqlItem sqlItem : sqlItems) {
                if (sqlItem.isIgnore()) {
                    continue;
                }
                String errSql = "";
                try {
                    for (String sql : sqlItem.getSqls()) {
                        errSql = sql;
                        System.out.println("exec:"+sql);
                        statement.executeUpdate(sql);
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
        }

        return ddlResult;
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
    public String getModelSchemaFromDB() {
        Database database = platform.readModelFromDatabase("model");
        DatabaseIO dbIO = new DatabaseIO();
        StringWriter writer = new StringWriter();
        dbIO.write(database, writer);
        return writer.toString();
    }

    public String getSchecule() {
        platform.setSqlCommentsOn(true);
        Database database = platform.readModelFromDatabase("model");
        DatabaseIO dbIO = new DatabaseIO();
        StringWriter stringWriter = new StringWriter();
        dbIO.write(database, stringWriter);
        return stringWriter.toString();
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
