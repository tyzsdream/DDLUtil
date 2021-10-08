package cn.lead2success.ddlutils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @auther: TYZ
 * @description:
 * @Date: 2019/12/16
 */
public class DDLSchemaHelper {

    private Document document;


    public DDLSchemaHelper(String schemaName) {
        document = XmlUtil.createXml("database");
        document.getDocumentElement().setAttribute("name", schemaName);
    }


    public Element addTable(String name, String comment) {
        Element table = XmlUtil.appendChild(document.getDocumentElement(), "table");
        table.setAttribute("name", name);
        if (StrUtil.isNotBlank(comment)) {
            table.setAttribute("description", comment);
        }
        return table;
    }

    public Element addColumn(Element tableElement, String name, String comment, COLType colType, Boolean isPk, Boolean notNull, Integer precision, Integer scale, String defValue) {
        Element column = XmlUtil.appendChild(tableElement, "column");
        column.setAttribute("name", name);

        column.setAttribute("type", getType(colType));

        if (StrUtil.isNotBlank(comment)) {
            column.setAttribute("description", comment);
        }

        if (null != precision || null != scale) {
            String size = precision + (null == scale ? "" : "," + scale);
            column.setAttribute("size", size);
        }

        if (null != notNull && notNull) {
            column.setAttribute("required", "true");
        }
        if (null != isPk && isPk) {
            column.setAttribute("primaryKey", "true");
        }
        if (!StrUtil.isAllBlank(defValue)) {
            column.setAttribute("default", defValue);
        }
        return column;
    }


    public String getType(COLType type) {
        switch (type) {
            case NUMBER:
                return "DECIMAL";
            case DATE:
                return "TIMESTAMP";
            case BOOLEAN:
            case INTEGER:
            case CLOB:
                return type.name();
            default:
                return "VARCHAR";
        }
    }

    public enum COLType {STRING, INTEGER, DATE, NUMBER, BOOLEAN, CLOB}

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getSchema() {
        return XmlUtil.toStr(document);
    }
}