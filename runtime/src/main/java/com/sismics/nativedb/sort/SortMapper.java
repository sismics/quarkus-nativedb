package com.sismics.nativedb.sort;

import java.util.List;

/**
 * Maps datatables columns to table columns for sorting.
 *
 * @author jtremeaux
 */
@FunctionalInterface
public interface SortMapper {
    List<SortColumn> map(String path, boolean direction);
}
