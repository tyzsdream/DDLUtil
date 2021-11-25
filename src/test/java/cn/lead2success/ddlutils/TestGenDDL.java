package cn.lead2success.ddlutils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.lead2success.ddlutils.io.DatabaseIO;
import cn.lead2success.ddlutils.model.Database;
import cn.lead2success.ddlutils.model.Table;
import cn.lead2success.ddlutils.util.SqlTokenizer;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TestGenDDL {

    //生成Schema
    @Test
    public void main1() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://172.16.11.47:5432/hip_data?currentSchema=public");
        dataSource.setUsername("hip_data");
        dataSource.setPassword("1qaz2wsX");
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);
        Database database = platform.readModelFromDatabase("model", null, "public", null);


        List<Table> oldMaps = Arrays.stream(database.getTables()).filter(table -> (table.getName().toUpperCase().indexOf("PIB_SYSTEM") < 0)).collect(Collectors.toList());
        for (Table table : oldMaps) {
            database.removeTable(table);
        }
        DatabaseIO dbIO = new DatabaseIO();
        StringWriter writer = new StringWriter();
        dbIO.write(database, writer);
        FileUtil.writeString(writer.toString(), "D:\\A_WORKSPACE\\DefaultSpace\\ddlutils\\target\\DDL.xml", CharsetUtil.UTF_8);
    }

    @Test
    public void main2() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://pgm-uf6igxpwh697530yqo.pg.rds.aliyuncs.com:1433/empi?currentSchema=public");
        dataSource.setUsername("empi");
        dataSource.setPassword("1qaz2wsX");
        DDLHelper ddlHelper = new DDLHelper(dataSource);
        String schecule = ddlHelper.getSchecule(null, "public");
//        DDLResult ddlSql = ddlHelper.getAlterModelSql(schecule, false, false, true,null,null);
//        System.out.println(ddlSql);
//
//        System.out.println(ddlHelper.getModelSchemaFromDB());
    }

    @Test
    public void aa(){
        DatabaseIO dbIO = new DatabaseIO();
        String xml="<?xml version='1.0' encoding='UTF-8'?><database name=\"mdm\"></database>";
        InputSource inputSource = new InputSource(new StringReader(xml));
        Database read = dbIO.read(inputSource);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://172.16.11.47:5432/hip_data?currentSchema=public");
        dataSource.setUsername("hip_data");
        dataSource.setPassword("1qaz2wsX");
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);
        String sql = platform.getCreateModelSql(read,false,false);
    }

}
