package com.sismics.nativedb.filter.column;

import com.sismics.nativedb.filter.FilterColumn;

/**
 * Filter that keeps only rows where the specified column is null.
 *
 * @author jtremeaux
 */
public class NullFilterColumn extends FilterColumn {
    public NullFilterColumn(String column, String filter) {
        super(null, column);
    }

    @Override
    public String getPredicate() {
        return "(" + filter + " is null)";
    }

    @Override
    public Object getParamValue() {
        return null;
    }
}
