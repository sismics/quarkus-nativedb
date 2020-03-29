package com.sismics.nativedb.filter.column;

import com.sismics.nativedb.filter.FilterColumn;

import java.util.UUID;

/**
 * Filter a UUID column with the equals operators.
 *
 * @author jtremeaux
 */
public class UUIDFilterColumn extends FilterColumn {
    public UUIDFilterColumn(String column, String filter) {
        super(column, filter);
    }

    @Override
    public String getPredicate() {
        return column + " = :" + getParamName();
    }

    @Override
    public Object getParamValue() {
        return UUID.fromString(filter);
    }
}
