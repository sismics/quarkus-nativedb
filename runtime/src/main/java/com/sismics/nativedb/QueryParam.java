package com.sismics.nativedb;

import com.sismics.nativedb.filter.FilterCriteria;

import java.util.List;
import java.util.Map;

/**
 * Query parameters.
 *
 * @author jtremeaux
 */
public class QueryParam {

    /**
     * Default table alias.
     */
    private String defaultTableAlias;

    /**
     * Query string.
     */
    private String queryString;

    /**
     * Query criteria.
     */
    private List<String> criteriaList;

    /**
     * Query parameters.
     */
    private Map<String, Object> parameterMap;

    /**
     * Filter criteria.
     */
    private FilterCriteria filterCriteria;

    /**
     * Group by criteria.
     */
    private List<String> groupByList;

    /**
     * Result mapper for native queries.
     */
    private ResultMapper resultMapper;

    /**
     * Constructor of QueryParam.
     *
     * @param queryString Query string
     * @param criteriaList Query criteria
     * @param parameterMap Query parameters
     * @param groupByList Group by criteria
     */
    public QueryParam(String queryString, List<String> criteriaList, Map<String, Object> parameterMap,
            FilterCriteria filterCriteria, List<String> groupByList,
            ResultMapper resultMapper) {
        this.queryString = queryString;
        this.criteriaList = criteriaList;
        this.parameterMap = parameterMap;
        this.filterCriteria = filterCriteria;
        this.groupByList = groupByList;
        this.resultMapper = resultMapper;
    }

    /**
     * Constructor of QueryParam.
     *
     * @param queryString Query string
     * @param criteriaList Query criteria
     * @param parameterMap Query parameters
     */
    public QueryParam(String queryString, List<String> criteriaList, Map<String, Object> parameterMap,
            FilterCriteria filterCriteria, ResultMapper resultMapper) {
        this(queryString, criteriaList, parameterMap, filterCriteria, null, resultMapper);
    }

    public String getDefaultTableAlias() {
        return defaultTableAlias;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public List<String> getCriteriaList() {
        return criteriaList;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public FilterCriteria getFilterCriteria() {
        return filterCriteria;
    }

    public List<String> getGroupByList() {
        return groupByList;
    }

    public ResultMapper getResultMapper() {
        return resultMapper;
    }

    public QueryParam setDefaultTableAlias(String defaultTableAlias) {
        this.defaultTableAlias = defaultTableAlias;
        return this;
    }
}
