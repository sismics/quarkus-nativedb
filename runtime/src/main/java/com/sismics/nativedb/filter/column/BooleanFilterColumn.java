package com.sismics.nativedb.filter.column;

import com.sismics.nativedb.filter.FilterColumn;

/**
 * @author jtremeaux
 */
public class BooleanFilterColumn extends FilterColumn {
    public BooleanFilterColumn(String column, String filter) {
        super(column, filter);
    }

    @Override
    public String getPredicate() {
        return column + " = :" + getParamName();
    }

    @Override
    public Object getParamValue() {
        return "true".equalsIgnoreCase(filter);
    }

}
