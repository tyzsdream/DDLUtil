package mytest;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.lead2success.ddlutils.DDLHelper;
import cn.lead2success.ddlutils.DDLResult;
import org.apache.commons.dbcp.BasicDataSource;

import java.net.URL;

public class DdlHelperTest {
    public static void main(String[] args) {
        readFile();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://pgm-uf6igxpwh697530yqo.pg.rds.aliyuncs.com:1433/cda");
        dataSource.setUsername("cda");
        dataSource.setPassword("1qaz2wsX");
        DDLHelper ddlHelper = new DDLHelper(dataSource);
        System.out.println(ddlHelper.getSchecule());
    }

//String schema, Boolean removeTable, int warnSize, Boolean continueOnError
    private static String readFile(){
        URL resource = DdlHelperTest.class.getResource("");
        String path = resource.getPath()+"DdlHelperTest0.xml";
        String content = FileUtil.readUtf8String(path.substring(1));
        return content;
    }
}
