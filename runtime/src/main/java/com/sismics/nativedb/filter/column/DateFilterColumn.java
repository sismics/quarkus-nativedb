package com.sismics.nativedb.filter.column;

import com.sismics.nativedb.Db;
import com.sismics.nativedb.filter.FilterColumn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter a text column containing a date.
 *
 * @author jtremeaux
 */
public class DateFilterColumn extends FilterColumn {
    public static final Pattern DATE_PATTERN = Pattern.compile("(\\d+)\\-(\\d+)\\-(\\d+)");

    private String operator = "";

    private boolean nullable;

    public DateFilterColumn(String column, String filter, boolean nullable) {
        super(column, filter);

        this.nullable = nullable;

        parseOperator();
    }

    public DateFilterColumn(String column, String filter) {
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
            predicate = Db.getDateTrunc(column) + " = :" + getParamName();
        } else if ("<>".equals(operator)) {
            predicate = Db.getDateTrunc(column) + " <> :" + getParamName();
        } else if (">".equals(operator)) {
            predicate = Db.getDateTrunc(column) + " > :" + getParamName();
        } else if (">=".equals(operator)) {
            predicate = Db.getDateTrunc(column) + " >= :" + getParamName();
        } else if ("<".equals(operator)) {
            predicate = Db.getDateTrunc(column) + " < :" + getParamName();
        } else if ("<=".equals(operator)) {
            predicate = Db.getDateTrunc(column) + " <= :" + getParamName();
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
        return getDate(filter.substring(operator.length()));
    }

    private Object getDate(String value) {
        if (value == null || value.trim().length() == 0) {
            return null;
        }

        try {
            Matcher matcher = DATE_PATTERN.matcher(value.trim());
            if (matcher.matches()) {
                return null; // FIXME
                //                return new LocalDate(Integer.valueOf(matcher.group(1)),
                //                        Integer.valueOf(matcher.group(2)),
                //                        Integer.valueOf(matcher.group(3))).toDate();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
