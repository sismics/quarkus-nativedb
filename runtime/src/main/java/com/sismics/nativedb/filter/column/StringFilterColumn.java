package com.sismics.nativedb.filter.column;

import com.sismics.nativedb.filter.FilterColumn;

/**
 * Filter a text column with the usual string comparison operators.
 *
 * @author jtremeaux
 */
public class StringFilterColumn extends FilterColumn {
    private String operator;

    public StringFilterColumn(String column, String filter) {
        super(column, filter);

        parseOperator();
    }

    private void parseOperator() {
        if (filter.startsWith("<=") || filter.startsWith(">=") || filter.startsWith("<>")) {
            operator = filter.substring(0, 2);
        } else if (filter.startsWith("<") || filter.startsWith(">") || filter.startsWith("=")) {
            operator = filter.substring(0, 1);
        } else {
            operator = "";
        }
    }

    @Override
    public String getPredicate() {
        if ("=".equals(operator)) {
            return "lower(" + column + ") = lower(:" + getParamName() + ")";
        } else if ("<>".equals(operator)) {
            return "lower(" + column + ") <> lower(:" + getParamName() + ")";
        } else if (">".equals(operator)) {
            return "lower(" + column + ") > lower(:" + getParamName() + ")";
        } else if (">=".equals(operator)) {
            return "lower(" + column + ") >= lower(:" + getParamName() + ")";
        } else if ("<".equals(operator)) {
            return "lower(" + column + ") < lower(:" + getParamName() + ")";
        } else if ("<=".equals(operator)) {
            return "lower(" + column + ") <= lower(:" + getParamName() + ")";
        } else if ("".equals(operator)) {
            return "lower(" + column + ") like lower(:" + getParamName() + ")";
        } else {
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    @Override
    public Object getParamValue() {
        if ("".equals(operator)) {
            return "%" + filter + "%";
        } else {
            return filter.substring(operator.length());
        }
    }
}
