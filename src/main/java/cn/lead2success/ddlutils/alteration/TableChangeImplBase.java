package cn.lead2success.ddlutils.alteration;

import cn.lead2success.ddlutils.model.Database;
import cn.lead2success.ddlutils.model.Table;

/**
 * Base class for change implementations.
 * 
 * @version $Revision: $
 */
public abstract class TableChangeImplBase implements TableChange
{
    /** The name of the affected table. */
    private String _tableName;

    /**
     * Creates a new change object.
     * 
     * @param tableName The table's name
     */
    public TableChangeImplBase(String tableName)
    {
        _tableName = tableName;
    }

    /**
     * {@inheritDoc}
     */
    public String getChangedTable()
    {
        return _tableName;
    }

    /**
     * {@inheritDoc}
     */
    public Table findChangedTable(Database model, boolean caseSensitive)
    {
    	return model.findTable(_tableName, caseSensitive);
    }
}
