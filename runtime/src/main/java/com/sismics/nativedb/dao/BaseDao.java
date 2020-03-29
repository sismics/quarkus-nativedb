package com.sismics.nativedb.dao;

import com.sismics.nativedb.PaginatedList;
import com.sismics.nativedb.PaginatedLists;
import com.sismics.nativedb.QueryParam;
import com.sismics.nativedb.filter.FilterCriteria;
import com.sismics.nativedb.sort.SortCriteria;

import java.util.List;

/**
 * Base DAO.
 *
 * @author jtremeaux
 */
public abstract class BaseDao<T, C> {
    /**
     * Search items by criteria.
     *
     * @param list Paginated list (updated by side effects)
     * @param criteria Search criteria
     * @param sortCriteria Sort criteria
     * @param filterCriteria Filter criteria
     */
    public void findByCriteria(PaginatedList<T> list, C criteria, SortCriteria sortCriteria, FilterCriteria filterCriteria) {
        PaginatedLists.executePaginatedQuery(list, getQueryParam(criteria, filterCriteria), sortCriteria);
    }

    /**
     * Search items by criteria.
     *
     * @param list Paginated list (updated by side effects)
     * @param criteria Search criteria
     * @param sortCriteria Sort criteria
     */
    public void findByCriteria(PaginatedList<T> list, C criteria, SortCriteria sortCriteria) {
        PaginatedLists.executePaginatedQuery(list, getQueryParam(criteria, null), sortCriteria);
    }

    /**
     * Search items by criteria.
     *
     * @param criteria Search criteria
     * @param sortCriteria Sort criteria
     * @param filterCriteria Filter criteria
     */
    public List<T> findByCriteria(C criteria, SortCriteria sortCriteria, FilterCriteria filterCriteria) {
        return PaginatedLists.executeQuery(getQueryParam(criteria, filterCriteria), sortCriteria);
    }

    /**
     * Search items by criteria.
     *
     * @param criteria Search criteria
     * @param sortCriteria Sort criteria
     */
    public List<T> findByCriteria(C criteria, SortCriteria sortCriteria) {
        return PaginatedLists.executeQuery(getQueryParam(criteria, null), sortCriteria);
    }

    /**
     * Search items by criteria.
     *
     * @param criteria Search criteria
     */
    public List<T> findByCriteria(C criteria) {
        return findByCriteria(criteria, null, null);
    }

    /**
     * Find one item by criteria.
     * Throws an exception if more than one item is found.
     *
     * @param criteria Search criteria
     */
    public T findOneByCriteria(C criteria) {
        return findOneByCriteria(criteria, null);
    }

    /**
     * Find one item by criteria.
     * Throws an exception if more than one item is found.
     *
     * @param criteria Search criteria
     * @param filterCriteria Filter criteria
     */
    public T findOneByCriteria(C criteria, FilterCriteria filterCriteria) {
        List<T> list = PaginatedLists.executeQuery(getQueryParam(criteria, filterCriteria), null);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("nativedb.nonunique.error");
        }
        return list.iterator().next();
    }

    /**
     * Find the first item by criteria.
     *
     * @param criteria Search criteria
     */
    public T findFirstByCriteria(C criteria) {
        return findFirstByCriteria(criteria, null, null);
    }

    /**
     * Find the first item by criteria.
     *
     * @param criteria Search criteria
     * @param sortCriteria Sort criteria
     */
    public T findFirstByCriteria(C criteria, SortCriteria sortCriteria) {
        return findFirstByCriteria(criteria, sortCriteria, null);
    }

    /**
     * Find the first item by criteria.
     *
     * @param criteria Search criteria
     * @param sortCriteria Sort criteria
     * @param filterCriteria Filter criteria
     */
    public T findFirstByCriteria(C criteria, SortCriteria sortCriteria, FilterCriteria filterCriteria) {
        List<T> list = PaginatedLists.executeQuery(getQueryParam(criteria, filterCriteria), sortCriteria);
        return !list.isEmpty() ? list.iterator().next() : null;
    }

    /**
     * Get the number of items by criteria.
     *
     * @param criteria Search criteria
     * @return The number of items
     */
    public long countByCriteria(C criteria) {
        return countByCriteria(criteria, null);
    }

    /**
     * Get the number of items by criteria.
     *
     * @param criteria Search criteria
     * @param filterCriteria Filter criteria
     * @return The number of items
     */
    public long countByCriteria(C criteria, FilterCriteria filterCriteria) {
        PaginatedList<T> list = PaginatedLists.create();
        PaginatedLists.executeCountQuery(list, getQueryParam(criteria, filterCriteria));
        return list.getTotalRowCount();
    }

    protected abstract QueryParam getQueryParam(C criteria, FilterCriteria filterCriteria);

}
