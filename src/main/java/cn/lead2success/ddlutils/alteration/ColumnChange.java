package cn.lead2success.ddlutils.alteration;

import cn.lead2success.ddlutils.model.Column;
import cn.lead2success.ddlutils.model.Database;

/**
 * Represents a change to a column of a table.
 * 
 * @version $Revision: $
 */
public interface ColumnChange extends TableChange
{
    /**
     * Returns the name of the affected column from the original model.
     * 
     * @return The name of the affected column
     */
    public String getChangedColumn();

    /**
     * Finds the column object corresponding to the changed column in the given database model.
     * 
     * @param model         The database model
     * @param caseSensitive Whether identifiers are case sensitive
     * @return The column object or <code>null</code> if it could not be found
     */
    public Column findChangedColumn(Database model, boolean caseSensitive);
}
