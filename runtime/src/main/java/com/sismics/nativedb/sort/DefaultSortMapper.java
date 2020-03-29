package com.sismics.nativedb.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Default sort mapper: maps one column to the DB column / query alias with the same name.
 *
 * @author jtremeaux
 */
public class DefaultSortMapper implements SortMapper {
    @Override
    public List<SortColumn> map(String path, boolean direction) {
        List<SortColumn> list = new ArrayList<>();
        list.add(new SortColumn(path, direction));
        return list;
    }
}
