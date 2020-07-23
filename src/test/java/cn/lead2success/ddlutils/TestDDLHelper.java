package cn.lead2success.ddlutils;

import org.apache.commons.dbcp.BasicDataSource;

public class TestDDLHelper {
    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://pgm-uf6igxpwh697530yqo.pg.rds.aliyuncs.com:1433/cdr");
        dataSource.setUsername("cdr");
        dataSource.setPassword("1qaz2wsX");
        DDLHelper ddlHelper = new DDLHelper(dataSource);


        final String schema =
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database name='cda'>\n" +
                        "  <table name='DEMO' description='test01'>\n" +
//                        "    <column name='COL_ARRAY'           type='ARRAY' primaryKey='true' required='true' description='ceshishuju'/>\n" +
//                        "    <column name='COL_BIGINT'          type='BIGINT' primaryKey='true' required='true'/>\n" +
                        "    <column name='COL_BINARY'          type='BINARY' primaryKey='true'/>\n" +
                        "    <column name='COL_BIT'             type='BIT'/>\n" +
                        "    <column name='COL_BLOB1'            type='BLOB' description='blob类型数值'/>\n" +
                        "    <column name='COL_TIMESTAMP'       type='TIMESTAMP' default='now'/>\n" +
//                        "    <column name='COL_TINYINT'         type='VARBINARY' />\n" +
                        "    <column name='COL_VARBINARY'       type='VARCHAR' size='16' default='AAAA'/>" +
                        "    <column name='COL_VARCHAR2'         type='VARCHAR' size='30' default='222' description='AAA'/>\n" +
                        "    <column name='COL_VARCHAR3'         type='VARCHAR' size='30' default='222' description='AAA'/>\n" +
                        "  </table>\n" +
                        "  <table name='DEMO1'>\n" +
                        "    <column name='COL_BINARY1'          type='BINARY'/>\n" +
                        "    <column name='COL_BIT'             type='BIT'/>\n" +
                        "    <column name='COL_BLOB'            type='BLOB' description='boolean类型数值'/>\n" +
//                        "    <column name='COL_BOOLEAN'         type='BOOLEAN' description='boolean类型数值'/>\n" +
//                        "    <column name='COL_CHAR'            type='CHAR' size='15'/>\n" +
//                        "    <column name='COL_CLOB'            type='CLOB'/>\n" +
//                        "    <column name='COL_DATALINK'        type='DATALINK'/>\n" +
//                        "    <column name='COL_DATE'            type='DATE'/>\n" +
//                        "    <column name='COL_DECIMAL'         type='DECIMAL' size='15,3'/>\n" +
//                        "    <column name='COL_DECIMAL_NOSCALE' type='DECIMAL' size='15'/>\n" +
//                        "    <column name='COL_DISTINCT'        type='DISTINCT'/>\n" +
//                        "    <column name='COL_DOUBLE'          type='DOUBLE'/>\n" +
//                        "    <column name='COL_FLOAT'           type='FLOAT'/>\n" +
//                        "    <column name='COL_INTEGER'         type='INTEGER'/>\n" +
//                        "    <column name='COL_JAVA_OBJECT'     type='JAVA_OBJECT'/>\n" +
//                        "    <column name='COL_LONGVARBINARY'   type='LONGVARBINARY'/>\n" +
//                        "    <column name='COL_LONGVARCHAR'     type='LONGVARCHAR'/>\n" +
//                        "    <column name='COL_NULL'            type='NULL'/>\n" +
//                        "    <column name='COL_NUMERIC'         type='NUMERIC' size='15' />\n" +
//                        "    <column name='COL_OTHER'           type='OTHER'/>\n" +
//                        "    <column name='COL_REAL'            type='REAL'/>\n" +
//                        "    <column name='COL_REF'             type='REF'/>\n" +
//                        "    <column name='COL_SMALLINT'        type='SMALLINT' size='5'/>\n" +
//                        "    <column name='COL_STRUCT'          type='STRUCT'/>\n" +
//                        "    <column name='COL_TIME'            type='TIME'/>\n" +
                        "    <column name='COL_TIMESTAMP'       type='TIMESTAMP'/>\n" +
//                        "    <column name='COL_TINYINT'         type='VARBINARY' />\n" +
                        "    <column name='COL_VARBINARY'       type='VARCHAR' size='10' default='AAAA'/>" +
                        "    <column name='COL_VARCHAR'         type='VARCHAR' size='30' default='222'/>" +
                        " <index name='COL_VARCHAR'>\n" +
                        "      <index-column name='COL_VARCHAR'/>\n" +
                        "    </index>" +
                        "  </table>\n" +
                        "</database>";
//        DDLResult ddlSql = ddlHelper.getDDLSql(schema, true, 100, true);
//        System.out.println(ddlSql);

        System.out.println(ddlHelper.getModelSchemaFromDB());
    }

}
