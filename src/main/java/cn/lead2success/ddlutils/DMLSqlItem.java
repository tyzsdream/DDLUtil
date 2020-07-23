package cn.lead2success.ddlutils;

import java.util.List;

public class DMLSqlItem {
    /**
     * 表名
     */
    public String tableName;

    /**
     * sql列表
     */
    List<DMLSqlItem.SqlItem> sqlItems;
    /**
     * 是否忽略，
     */
    public boolean ignore = false;
    /**
     * 警告状态，超过N条提示
     */
    public String status;

    public DMLSqlItem(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DMLSqlItem.SqlItem> getSqlItems() {
        return sqlItems;
    }

    public void setSqlItems(List<DMLSqlItem.SqlItem> sqlItems) {
        this.sqlItems = sqlItems;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class SqlItem {
        public String sql;
        public String status;
        public boolean ignore = false;
        public Throwable exception;
    }
}
