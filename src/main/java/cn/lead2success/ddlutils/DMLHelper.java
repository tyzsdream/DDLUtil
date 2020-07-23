package cn.lead2success.ddlutils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.util.regex.Pattern;

public class DMLHelper {

    private final Log _log = LogFactory.getLog(DDLHelper.class);
    private final Pattern sqlPattern = Pattern.compile("(CREATE TABLE|INSERT INTO|DROP TABLE|CREATE INDEX \\S* ON|COMMENT ON TABLE|COMMENT ON COLUMN) ([a-zA-Z_]*)([\\s\\.]*)([\\s\\S]*)");
    public DataSource dataSource;
    private Platform platform;

    public DMLHelper(DataSource dataSource) {
        this.dataSource = dataSource;
        this.platform = PlatformFactory.createNewPlatformInstance(dataSource);
    }

//    exeuuteDms
}
