package cn.lead2success.ddlutils;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

public class DDLResult {

    private List<SqlItem> sqlItems;

    /**
     * 是否执行完毕
     */
    private boolean runOver;

    public DDLResult(List<SqlItem> ddlSqls) {
        this.sqlItems = new ArrayList<>(ddlSqls);
    }

    public List<SqlItem> getSqlItems() {
        return sqlItems;
    }

    public void setSqlItems(List<SqlItem> sqlItems) {
        this.sqlItems = sqlItems;
    }

    public boolean isRunOver() {
        return runOver;
    }

    public void refresh() {
        for (SqlItem sqlItem : sqlItems) {
            String status = sqlItem.getStatus();//0或空：未执行  1、执行成功  2、执行出错
            if ((StrUtil.isBlank(status) || "2".equals(status)) && !sqlItem.isIgnore()) {
                this.runOver = false;
                return;
            }
        }
        this.runOver = true;
    }

    public static class SqlItem {
        private String tableName;
        private List<String> sqls;
        /**
         * 0或空：未执行  1、执行成功  2、执行出错
         */
        private String status;
        private boolean ignore = false;
        private Throwable exception;

        public SqlItem(String tableName) {
            this.tableName = tableName;
            sqls = new ArrayList<>();
        }

        public List<String> getSqls() {
            return sqls;
        }

        public void setSqls(List<String> sqls) {
            this.sqls = sqls;
        }

        public void addSql(String sql) {
            this.sqls.add(sql);
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isIgnore() {
            return ignore;
        }

        public void setIgnore(boolean ignore) {
            this.ignore = ignore;
        }

        public Throwable getException() {
            return exception;
        }

        public void setException(Throwable exception) {
            this.exception = exception;
        }
    }
}