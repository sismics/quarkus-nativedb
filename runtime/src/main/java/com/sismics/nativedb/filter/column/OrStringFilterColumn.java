package com.sismics.nativedb.filter.column;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter on a disjunction of string matches.
 * Instead of filtering on "column ~= filter", filters on (columns[0] ~= filter or ... or columns[n - 1] ~= filter).
 *
 * @author jtremeaux
 */
public class OrStringFilterColumn extends StringFilterColumn {
    private String[] columns;

    public OrStringFilterColumn(String column, String filter, String... columns) {
        super(column, filter);

        this.columns = columns;
    }

    @Override
    public String getPredicate() {
        List<String> predicates = new ArrayList<>();
        for (String c : columns) {
            StringFilterColumn f = new StringFilterColumn(c, filter) {
                @Override
                public String getParamName() {
                    return "filtercolumn_" + OrStringFilterColumn.this.hashCode();
                }
            };
            predicates.add(f.getPredicate());
        }
        return "null"; // FIXME
        //        return "(" + StringUtils.join(predicates, " or ") + ")";
    }
}
