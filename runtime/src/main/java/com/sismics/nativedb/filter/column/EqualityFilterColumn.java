package com.sismics.nativedb.filter.column;

import com.sismics.nativedb.filter.FilterColumn;

/**
 * Filter a text column with the equals operators.
 *
 * @author jtremeaux
 */
public class EqualityFilterColumn extends FilterColumn {
    public EqualityFilterColumn(String column, String filter) {
        super(column, filter);
    }

    @Override
    public String getPredicate() {
        return column + " = :" + getParamName();
    }

    @Override
    public Object getParamValue() {
        return filter;
    }
}
