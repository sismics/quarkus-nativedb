package com.sismics.nativedb.filter.column;

import com.sismics.nativedb.filter.FilterColumn;

/**
 * Filter a text column containing a number (integer or decimal).
 *
 * @author jtremeaux
 */
public class NumberFilterColumn extends FilterColumn {
    private String operator = "";

    private boolean nullable;

    public NumberFilterColumn(String column, String filter, boolean nullable) {
        super(column, filter);

        this.nullable = nullable;

        parseOperator();
    }

    public NumberFilterColumn(String column, String filter) {
        this(column, filter, false);
    }

    private void parseOperator() {
        if (filter.startsWith("<=") || filter.startsWith(">=") || filter.startsWith("<>")) {
            operator = filter.substring(0, 2);
        } else if (filter.startsWith("<") || filter.startsWith(">") || filter.startsWith("=")) {
            operator = filter.substring(0, 1);
        } else if (filter.startsWith("=")) {
            operator = "=";
        }
    }

    @Override
    public String getPredicate() {
        String predicate;
        if ("".equals(operator) || "=".equals(operator)) {
            predicate = column + " = :" + getParamName();
        } else if ("<>".equals(operator)) {
            predicate = column + " <> :" + getParamName();
        } else if (">".equals(operator)) {
            predicate = column + " > :" + getParamName();
        } else if (">=".equals(operator)) {
            predicate = column + " >= :" + getParamName();
        } else if ("<".equals(operator)) {
            predicate = column + " < :" + getParamName();
        } else if ("<=".equals(operator)) {
            predicate = column + " <= :" + getParamName();
        } else {
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }

        if (nullable) {
            predicate = "(" + predicate + " or " + column + " is null)";
        }
        return predicate;
    }

    @Override
    public Object getParamValue() {
        return getDouble(filter.substring(operator.length()));
    }

    private Object getDouble(String value) {
        if (value == null || value.trim().length() == 0) {
            return null;
        }

        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
