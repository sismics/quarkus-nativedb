package com.sismics.nativedb.filter.mapper;

import com.google.common.collect.Multimap;
import com.sismics.nativedb.filter.FilterColumn;
import com.sismics.nativedb.filter.column.StringFilterColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Inclusive filter mapper: filter all specified columns using a String filter.
 *
 * @author jtremeaux
 */
public abstract class InclusiveFilterMapper extends DefaultFilterMapper {
    /**
     * Return the set of columns that can be filtered.
     *
     * @return Set of columns names or aliases
     */
    public abstract Set<String> getColumnName();

    @Override
    public List<FilterColumn> map(Multimap<String, String> filterMap) {
        List<FilterColumn> columnFilters = new ArrayList<>();

        for (Map.Entry<String, String> entry : filterMap.entries()) {
            if (getColumnName().contains(entry.getKey())) {
                columnFilters.add(new StringFilterColumn(entry.getKey(), entry.getValue()));
            }
        }
        return columnFilters;
    }
}
