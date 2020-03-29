package com.sismics.nativedb.filter.column;

import com.sismics.nativedb.filter.FilterColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter a text column with the in operators.
 *
 * @author bgamard
 */
public class EqualityInFilterColumn extends FilterColumn {
    public EqualityInFilterColumn(String column, String filter) {
        super(column, filter);
    }

    @Override
    public String getPredicate() {
        return column + " in (:" + getParamName() + ")";
    }

    @Override
    public Object getParamValue() {
        List<String> list = new ArrayList<>();
        for (String s : filter.split(";")) {
            list.add(s);
        }
        return list;
    }
}
