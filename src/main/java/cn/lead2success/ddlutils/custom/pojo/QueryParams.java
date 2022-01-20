package cn.lead2success.ddlutils.custom.pojo;


import java.util.List;

public class QueryParams {
    private String tablePattern;
    private List<String> includeTables;
    private boolean withColumns = true;

    public String getTablePattern() {
        return tablePattern;
    }

    public void setTablePattern(String tablePattern) {
        this.tablePattern = tablePattern;
    }

    public List<String> getIncludeTables() {
        return includeTables;
    }

    public void setIncludeTables(List<String> includeTables) {
        this.includeTables = includeTables;
    }

    public boolean isWithColumns() {
        return withColumns;
    }

    public void setWithColumns(boolean withColumns) {
        this.withColumns = withColumns;
    }

    public QueryParams() {
    }

    public QueryParams(List<String> includeTables) {
        this.includeTables = includeTables;
    }

    public QueryParams(String tablePattern, List<String> includeTables, boolean withColumns) {
        this.tablePattern = tablePattern;
        this.includeTables = includeTables;
        this.withColumns = withColumns;
    }
}
