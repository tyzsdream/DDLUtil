package cn.lead2success.ddlutils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * DML生成、解析、执行器
 */
public class DMLHelper {

    private final Log _log = LogFactory.getLog(DDLHelper.class);
    public DataSource dataSource;
    private Platform platform;

    public DMLHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void ececuteDml(String sqlContent, String dataContent) {
        sqlContent.split("skip/update||aa,bb;");
        String[] sqlArr = sqlContent.split(";");
        String sqlHeader = sqlArr[0];
        String[] headerArr = sqlHeader.split("||");
        String mode = headerArr[0];
        String[] pks = headerArr[1].split(",");
        String sql = sqlArr[1];


    }


    /**
     * sql解析，生成DmlSql对象
     * @param sqlContent
     * @return
     */
    private DmlSql getSqlInfo(String sqlContent) {
        try {
            String[] sqlArr = sqlContent.split("]");
            String mode = sqlArr[0].trim().replace("[", "").replace("]", "").trim();
            String tableName = sqlArr[1].trim().replace("[", "").replace("]", "").trim();
            String pkStr = sqlArr[2].trim().replace("[", "").replace("]", "").trim();
            String[] pks = pkStr.split(",");

            String sql = sqlArr[3];

            String sqlStr = sql.split("\\(")[1];
            sqlStr = sqlStr.split("\\)")[0];
            String[] columns = sqlStr.split(",");

            return new DmlSql(mode, tableName, Arrays.asList(pks), Arrays.asList(columns));
        } catch (Exception e) {
            throw new RuntimeException("SQL文件结构有误", e);
        }
    }

    private List<Map<String, Object>> getSqlData(String dataContent, String[] columns) throws Exception {
        String[] rows = dataContent.split("\\{;。;}");
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (String row : rows) {
            Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
            resultList.add(rowMap);
            String[] values = row.split("\\{,。,}");
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                Object set = null;
                if (value.startsWith("TIMESTAMP:")) {
                    set = new Timestamp(Long.parseLong(value.substring(10)));
                } else if (value.startsWith("NUMBER:")) {
                    set = new BigDecimal(value.substring(7));
                } else if (value.startsWith("BLOB:")) {
                    set = new SerialBlob(value.substring(5).getBytes("UTF-8"));
                } else if (value.startsWith("CLOB:")) {
                    set = new InputStreamReader(new ByteArrayInputStream(value.substring(5).getBytes()));
                } else if (!"NULL".equals(value)) {
                    set = value;
                }
                rowMap.put(columns[i], set);
            }
        }
        return resultList;
    }

    private boolean checkExists(String tableName, String[] pks, Map<String, Object> dataMap) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
        sql.append(tableName);
        sql.append(" WHERE 1=1 ");
        for (String pk : pks) {
            sql.append(" AND ");
            sql.append(pk);
            sql.append("=? ");
        }
        PreparedStatement ps = dataSource.getConnection().prepareStatement(sql.toString());
        for (int i = 0; i < pks.length; i++) {
            ps.setObject(i + 1, dataMap.get(pks[i]));
        }
        ResultSet resultSet = ps.executeQuery();
        int anInt = resultSet.getInt(1);
        return anInt > 0;
    }

    private boolean update(String tableName, String[] pks, Map<String, Object> dataMap) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
        sql.append(tableName);
        sql.append(" WHERE 1=1 ");
        for (String pk : pks) {
            sql.append(" AND ");
            sql.append(pk);
            sql.append("=? ");
        }
        PreparedStatement ps = dataSource.getConnection().prepareStatement(sql.toString());
        for (int i = 0; i < pks.length; i++) {
            ps.setObject(i + 1, dataMap.get(pks[i]));
        }
        ResultSet resultSet = ps.executeQuery();
        int anInt = resultSet.getInt(1);
        return anInt > 0;
    }

    public static class DmlSql {
        String mode;
        String tableName;
        List<String> pks;
        List<String> columns;

        public DmlSql(String mode, String tableName, List<String> pks, List<String> columns) {
            this.mode = mode;
            this.tableName = tableName;
            this.pks = pks;
            this.columns = columns;
        }

        public String getMode() {
            return mode;
        }

        public String getTableName() {
            return tableName;
        }

        public List<String> getPks() {
            return pks;
        }

        public List<String> getColumns() {
            return columns;
        }
    }
}
