package com.sismics.nativedb.filter.mapper;

import com.google.common.collect.Multimap;
import com.sismics.nativedb.filter.FilterColumn;

import java.util.List;

/**
 * Maps datatables columns to table columns for filtering.
 *
 * @author jtremeaux
 */
@FunctionalInterface
public interface FilterMapper {
    List<FilterColumn> map(Multimap<String, String> filterMap);
}
