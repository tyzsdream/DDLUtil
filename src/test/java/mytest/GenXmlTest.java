package mytest;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.lead2success.ddlutils.DDLHelper;
import cn.lead2success.ddlutils.Platform;
import cn.lead2success.ddlutils.PlatformFactory;
import cn.lead2success.ddlutils.io.DatabaseIO;
import cn.lead2success.ddlutils.model.Database;
import cn.lead2success.ddlutils.model.Table;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenXmlTest {
    //生成XML
    public static void main(String[] args) {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://pgm-uf6igxpwh697530yqo.pg.rds.aliyuncs.com:1433/empi");
//        dataSource.setUsername("empi");
//        dataSource.setPassword("1qaz2wsX");
//        DDLHelper ddlHelper = new DDLHelper(dataSource);
//        String schecule = ddlHelper.getSchecule("", "public");
//        FileUtil.writeString(schecule, "E:\\DefaultSpace\\ddlutils\\target\\DDL.xml", CharsetUtil.UTF_8);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://172.16.11.47:5432/hip_data?currentSchema=public");
        dataSource.setUsername("hip_data");
        dataSource.setPassword("1qaz2wsX");
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);
        Database database = platform.readModelFromDatabase("model", null, "public", null);


        List<Table> oldMaps = Arrays.stream(database.getTables()).filter(table -> (table.getName().toUpperCase().indexOf("PIB_CONF") < 0)).collect(Collectors.toList());
        for (Table table : oldMaps) {
            database.removeTable(table);
        }
        DatabaseIO dbIO = new DatabaseIO();
        StringWriter writer = new StringWriter();
        dbIO.write(database, writer);
        FileUtil.writeString(writer.toString(), "E:\\DefaultSpace\\ddlutils\\target\\DDL.xml", CharsetUtil.UTF_8);
    }
}
