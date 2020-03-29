package com.sismics.nativedb;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to represent a page of T object.
 *
 * @author jtremeaux
 */
public class PaginatedList<T> {

    private int limit;

    private int offset;

    private long totalRowCount;

    private List<T> list;

    public PaginatedList(int limit, int offset) {
        this.list = new ArrayList<>();
        this.totalRowCount = 0;
        this.limit = limit;
        this.offset = offset;
    }

    public PaginatedList(List<T> data, long total, int limit, int offset) {
        this.list = data;
        this.totalRowCount = total;
        this.limit = limit;
        this.offset = offset;
    }

    public long getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(long totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}