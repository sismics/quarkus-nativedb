package com.sismics.nativedb.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Sort criteria of a query.
 *
 * @author jtremeaux
 */
public class SortCriteria {
    /**
     * Columns to sort.
     */
    private List<SortColumn> sortColumnList = new ArrayList<SortColumn>();

    /**
     * Constructor of SortCriteria.
     *
     * @param path Path
     * @param ascending Sort in ascending order (or else descending)
     */
    public SortCriteria(String path, boolean ascending) {
        sortColumnList.add(new SortColumn(path, ascending));
    }

    /**
     * Constructor of SortCriteria with a number of columns all in ascending order.
     *
     * @param columns Path
     */
    public SortCriteria(String... columns) {
        for (String column : columns) {
            sortColumnList.add(new SortColumn(column, true));
        }
    }

    /**
     * Constructor of SortCriteria.
     *
     * @param sortColumnList List of sort columns
     */
    public SortCriteria(List<SortColumn> sortColumnList) {
        this.sortColumnList = sortColumnList;
    }

    public List<SortColumn> getSortColumnList() {
        return sortColumnList;
    }
}