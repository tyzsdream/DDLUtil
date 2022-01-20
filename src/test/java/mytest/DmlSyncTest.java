package mytest;

import cn.hutool.core.io.FileUtil;
import cn.lead2success.ddlutils.DDLHelper;
import org.apache.commons.dbcp.BasicDataSource;

import java.util.List;

public class DmlSyncTest {
    //同步结构
    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://172.16.11.47:5432/empi?currentSchema=public");
        dataSource.setUsername("empi");
        dataSource.setPassword("1qaz2wsX");
        String schema = FileUtil.readUtf8String("D:\\A_WORKSPACE\\DefaultSpace\\ddlutils\\src\\test\\java\\mytest\\DmlSyncTest.xml");

        DDLHelper ddlHelper = new DDLHelper(dataSource);
        List<String> alterModelSql = ddlHelper.getAlterModelSql(schema, false, false, true, null, "public");
        System.out.println(String.join("\n", alterModelSql));
        ddlHelper.execute(alterModelSql);
    }


//    public static void main(String[] args) throws SQLException {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://pgm-uf6igxpwh697530yqo.pg.rds.aliyuncs.com:1433/empi");
//        dataSource.setUsername("empi");
//        dataSource.setPassword("1qaz2wsX");
//        PreparedStatement prest = null;
//        Connection conn = dataSource.getConnection();
//        conn.setAutoCommit(false);
//        try {
//            prest = conn.prepareStatement("INSERT INTO public.empi_data_wash_result (source_id,pid,created_by,create_datetime) VALUES (?,?,NULL,now())");
//            for (int i = 0; i < 10; i++) {
//                prest.setObject(1, IdUtil.fastUUID());
//                prest.setObject(2, IdUtil.fastUUID());
//                prest.addBatch();
//            }
//            prest.executeBatch();
//            conn.commit();
//        } finally {
//            IoUtil.close(prest);
//            IoUtil.close(conn);
//        }
//    }
}
