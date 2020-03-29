package com.sismics.nativedb;

import com.sismics.nativedb.filter.FilterColumn;
import com.sismics.nativedb.filter.FilterCriteria;
import com.sismics.nativedb.pagination.client.ClientPagination;
import com.sismics.nativedb.sort.SortColumn;
import com.sismics.nativedb.sort.SortCriteria;
import org.hibernate.query.internal.NativeQueryImpl;

import javax.persistence.Query;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utilities for paginated lists.
 *
 * @author jtremeaux
 */
public class PaginatedLists {
    /**
     * Default size of a page.
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Maximum size of a page.
     */
    private static final int MAX_PAGE_SIZE = 10000;

    /**
     * Callback on native query.
     */
    public static OnNativeQuery onNativeQuery;

    /**
     * Constructs a paginated list.
     *
     * @param pageSize Size of the page
     * @param offset Offset of the page
     * @return Paginated list
     */
    public static <E> PaginatedList<E> create(Integer pageSize, Integer offset) {
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (offset == null) {
            offset = 0;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        return new PaginatedList<>(pageSize, offset);
    }

    /**
     * Constructs a paginated list with default parameters.
     *
     * @return Paginated list
     */
    public static <E> PaginatedList<E> create() {
        return create(null, null);
    }

    /**
     * Executes a non paginated query.
     *
     * @param queryParam Query parameters
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> executeQuery(QueryParam queryParam, SortCriteria sortCriteria) {
        StringBuilder sb = new StringBuilder(getQueryString(queryParam));
        if (sortCriteria != null) {
            String alias = getAlias(queryParam);
            sb.append(getOrderByClause(sortCriteria, alias));
        }

        Query query = Db.getSession().createNativeQuery(sb.toString());
        mapQueryParam(query, queryParam);
        mapFilterColumn(query, queryParam);

        if (onNativeQuery != null) {
            onNativeQuery.run(query);
        }

        List<E> resultList = query.getResultList();
        if (queryParam.getResultMapper() != null) {
            return queryParam.getResultMapper().map(resultList);
        } else {
            return resultList;
        }
    }

    /**
     * Executes a non paginated query.
     *
     * @param queryParam Query parameters
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> executeQuery(QueryParam queryParam) {
        return executeQuery(queryParam, null);
    }

    /**
     * Executes a native count request to count the number of results.
     *
     * @param paginatedList Paginated list object containing parameters, and into which results are added by side effects
     * @param queryParam Query parameters
     */
    public static <E> void executeCountQuery(PaginatedList<E> paginatedList, QueryParam queryParam) {
        Query query = Db.getSession().createNativeQuery(getNativeCountQuery(queryParam));
        mapQueryParam(query, queryParam);
        mapFilterColumn(query, queryParam);

        if (onNativeQuery != null) {
            onNativeQuery.run(query);
        }

        Number resultCount = (Number) query.getSingleResult();
        paginatedList.setTotalRowCount(resultCount.intValue());
    }

    /**
     * Returns the native query to count the number of records.
     * The initial query must be of the form "select xx from yy".
     *
     * @param queryParam Query parameters
     * @return Count query
     */
    public static String getNativeCountQuery(QueryParam queryParam) {
        StringBuilder sb = new StringBuilder("select count(*) as result_count from (");
        sb.append(getQueryString(queryParam));
        sb.append(") as t1");

        return sb.toString();
    }

    /**
     * Returns the table alias of the entity queried on.
     *
     * @param queryParam The query param
     * @return SQL alias
     */
    public static String getAlias(QueryParam queryParam) {
        if (queryParam.getDefaultTableAlias() != null) {
            return queryParam.getDefaultTableAlias();
        }
        Pattern p = Pattern.compile("select +(distinct )?(\\w+?)\\..*");
        Matcher m = p.matcher(queryParam.getQueryString());
        if (!m.matches()) {
            throw new RuntimeException("Cannot find entity alias in query: " + queryParam.getQueryString());
        }
        return m.group(2);
    }

    public static String getQueryString(QueryParam queryParam) {
        StringBuilder sb = new StringBuilder(queryParam.getQueryString());

        List<String> whereList = new LinkedList<>(queryParam.getCriteriaList());
        if (queryParam.getFilterCriteria() != null && !queryParam.getFilterCriteria().getFilterColumnList().isEmpty()) {
            whereList.addAll(queryParam.getFilterCriteria().getFilterColumnList().stream()
                    .map(e -> e.getPredicate())
                    .collect(Collectors.toList()));
        }
        if (!whereList.isEmpty()) {
            sb.append(" where ");
            sb.append(String.join(" and ", whereList));
        }
        if (queryParam.getGroupByList() != null && !queryParam.getGroupByList().isEmpty()) {
            sb.append(" group by ");
            sb.append(String.join(", ", queryParam.getGroupByList()));
        }

        return sb.toString();
    }

    /**
     * Executes a query and returns the data of the current page.
     *
     * @param paginatedList Paginated list object containing parameters, and into which results are added by side effects
     * @param queryParam Query parameters
     */
    @SuppressWarnings("unchecked")
    private static <E> void executeResultQuery(PaginatedList<E> paginatedList, QueryParam queryParam,
            SortCriteria sortCriteria) {
        StringBuilder sb = new StringBuilder(getQueryString(queryParam));
        if (sortCriteria != null) {
            String alias = getAlias(queryParam);
            sb.append(getOrderByClause(sortCriteria, alias));
        }

        Query query = Db.getSession().createNativeQuery(sb.toString());
        mapQueryParam(query, queryParam);
        mapFilterColumn(query, queryParam);

        query.setFirstResult(paginatedList.getOffset());
        query.setMaxResults(paginatedList.getLimit());

        if (onNativeQuery != null) {
            onNativeQuery.run(query);
        }

        List<E> resultList = query.getResultList();
        if (queryParam.getResultMapper() != null) {
            paginatedList.setList(queryParam.getResultMapper().map(resultList));
        } else {
            paginatedList.setList(resultList);
        }
    }

    /**
     * Get the order by clause from the sort criteria.
     *
     * @param sortCriteria Sort criteria
     * @param alias Table alias
     * @return Order by clause
     */
    private static String getOrderByClause(SortCriteria sortCriteria, String alias) {
        StringBuilder sb = new StringBuilder();
        if (sortCriteria != null && !sortCriteria.getSortColumnList().isEmpty()) {
            sb.append(" order by ");
            for (int i = 0; i < sortCriteria.getSortColumnList().size(); i++) {
                SortColumn sortColumn = sortCriteria.getSortColumnList().get(i);
                if (sortColumn.isPrefixed()) {
                    sb.append(alias);
                    sb.append(".");
                }
                sb.append(sortColumn.getPath());
                sb.append(sortColumn.getDirection() ? " asc" : " desc");
                if (i < sortCriteria.getSortColumnList().size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Executes a paginated request with 2 native queries (one to count the number of results, and one to return the page).
     *
     * @param paginatedList Paginated list object containing parameters, and into which results are added by side effects
     * @param queryParam Query parameters
     * @param sortCriteria Sort criteria
     */
    public static <E> void executePaginatedQuery(PaginatedList<E> paginatedList, QueryParam queryParam,
            SortCriteria sortCriteria) {
        executeCountQuery(paginatedList, queryParam);
        executeResultQuery(paginatedList, queryParam, sortCriteria);
    }

    /**
     * Executes a paginated request with 2 native queries (one to count the number of results, and one to return the page).
     *
     * @param paginatedList Paginated list object containing parameters, and into which results are added by side effects
     * @param queryParam Query parameters
     */
    public static <E> void executePaginatedQuery(PaginatedList<E> paginatedList, QueryParam queryParam) {
        executePaginatedQuery(paginatedList, queryParam, null);
    }

    private static void mapQueryParam(Query query, QueryParam queryParam) {
        for (Map.Entry<String, Object> parameter : queryParam.getParameterMap().entrySet()) {
            setParameter(query, parameter.getKey(), parameter.getValue());
        }
    }

    private static void mapFilterColumn(Query query, QueryParam queryParam) {
        if (queryParam.getFilterCriteria() != null) {
            for (FilterColumn filterColumn : queryParam.getFilterCriteria().getFilterColumnList()) {
                if (filterColumn.hasParam()) {
                    setParameter(query, filterColumn.getParamName(), filterColumn.getParamValue());
                }
            }
        }
    }

    private static void setParameter(Query query, String key, Object value) {
        if (value instanceof Collection) {
            Collection valueCollection = (Collection) value;
            if (!valueCollection.isEmpty() && ((Collection) value).iterator().next() instanceof UUID) {
                com.sismics.nativedb.query.Query.setParameterUUIDCollection((NativeQueryImpl) query, key, valueCollection);
            } else {
                query.setParameter(key, value);
            }
        } else {
            query.setParameter(key, value);
        }
    }

    /**
     * Paginate, filter and order a list on client (Java) side.
     *
     * @param paginatedList The list to paginate
     * @param fullList The full list of results
     * @param <E> The type of elements
     */
    public static <E> void executeClientQuery(PaginatedList<E> paginatedList, List<E> fullList, SortCriteria sortCriteria,
            FilterCriteria filterCriteria) {
        ClientPagination.paginate(paginatedList, fullList, sortCriteria, filterCriteria);
    }
}
