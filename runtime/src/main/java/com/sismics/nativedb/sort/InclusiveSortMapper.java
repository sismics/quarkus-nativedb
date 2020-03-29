package com.sismics.nativedb.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Inclusive sort mapper: maps one column to the DB column / query alias column with the same name,
 * provided that the name is contained in the inclusive list.
 *
 * @author jtremeaux
 */
public abstract class InclusiveSortMapper implements SortMapper {
    @Override
    public List<SortColumn> map(String path, boolean direction) {
        List<SortColumn> sortColumns = new ArrayList<>();
        if (getColumnName().contains(path)) {
            sortColumns.add(new SortColumn(path, direction, false));
        }
        return sortColumns;
    }

    /**
     * Return the set of columns that can be sorted.
     *
     * @return Set of columns names or aliases
     */
    public abstract Set<String> getColumnName();
}
