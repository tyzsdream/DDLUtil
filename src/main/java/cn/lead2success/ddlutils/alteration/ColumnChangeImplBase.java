package cn.lead2success.ddlutils.alteration;

import cn.lead2success.ddlutils.model.Column;
import cn.lead2success.ddlutils.model.Database;
import cn.lead2success.ddlutils.model.Table;

/**
 * Base class for changes to columns.
 * 
 * @version $Revision: $
 */
public abstract class ColumnChangeImplBase extends    TableChangeImplBase
                                           implements ColumnChange
{
    /** The column's name. */
    private String _columnName;

    /**
     * Creates a new change object.
     * 
     * @param tableName  The name of the table to remove the column from
     * @param columnName The column's name
     */
    public ColumnChangeImplBase(String tableName, String columnName)
    {
        super(tableName);
        _columnName = columnName;
    }

    /**
     * {@inheritDoc}
     */
    public String getChangedColumn()
    {
        return _columnName;
    }

    /**
     * {@inheritDoc}
     */
    public Column findChangedColumn(Database model, boolean caseSensitive)
    {
    	Table table = findChangedTable(model, caseSensitive);

    	return table == null ? null : table.findColumn(_columnName, caseSensitive);
    }
}
