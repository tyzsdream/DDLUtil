package mytest;

import cn.hutool.core.io.FileUtil;
import cn.lead2success.ddlutils.DDLHelper;
import cn.lead2success.ddlutils.DDLResult;
import org.apache.commons.dbcp.BasicDataSource;

import java.net.URL;

public class DmlHelperTest {
    public static void main(String[] args) {
        readFile();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://pgm-uf6igxpwh697530yqo.pg.rds.aliyuncs.com:1433/empi");
        dataSource.setUsername("empi");
        dataSource.setPassword("1qaz2wsX");
        DDLHelper ddlHelper = new DDLHelper(dataSource);
        String schema = readFile();
        DDLResult ddlResult = ddlHelper.getAlterModelSql(schema, false, false);
        ddlResult = ddlHelper.execute(ddlResult);
        while (!ddlResult.isRunOver()) {
            System.out.println("HAHAH");
            ddlResult = ddlHelper.execute(ddlResult);
        }
    }

    //String schema, Boolean removeTable, int warnSize, Boolean continueOnError
    private static String readFile() {
        URL resource = DmlHelperTest.class.getResource("");
        String path = resource.getPath() + "DdlHelperTest0.xml";
        String content = FileUtil.readUtf8String(path.substring(1));
        return content;
    }
}
