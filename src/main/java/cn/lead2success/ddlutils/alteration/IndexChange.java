package cn.lead2success.ddlutils.alteration;

import cn.lead2success.ddlutils.model.Database;
import cn.lead2success.ddlutils.model.Index;

/**
 * Represents a change to a index of a table.
 * 
 * @version $Revision: $
 */
public interface IndexChange extends TableChange
{
    /**
     * Finds the index object corresponding to the changed index in the given database model.
     * 
     * @param model         The database model
     * @param caseSensitive Whether identifiers are case sensitive
     * @return The index object or <code>null</code> if it could not be found
     */
    public Index findChangedIndex(Database model, boolean caseSensitive);
}
