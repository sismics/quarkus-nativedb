package com.sismics.nativedb.pagination.client;

import com.sismics.nativedb.PaginatedList;
import com.sismics.nativedb.filter.FilterColumn;
import com.sismics.nativedb.filter.FilterCriteria;
import com.sismics.nativedb.sort.SortColumn;
import com.sismics.nativedb.sort.SortCriteria;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Client-side (Java) pagination / sorting / filtering.
 *
 * @author jtremeaux
 */
public class ClientPagination {
    /**
     * Paginate, filter and order a list on client (Java) side.
     *
     * @param paginatedList The list to paginate
     * @param fullList The full list of results
     * @param <E> The type of elements
     */
    public static <E> void paginate(PaginatedList<E> paginatedList, List<E> fullList, SortCriteria sortCriteria,
            FilterCriteria filterCriteria) {
        // Filter list
        if (filterCriteria != null) {
            fullList = doFilterClient(fullList, filterCriteria);
        }

        // Order list
        if (sortCriteria != null) {
            doSortClient(fullList, sortCriteria);
        }

        // Paginate list
        paginatedList.setList(doPaginateClient(paginatedList, fullList));
        paginatedList.setTotalRowCount(fullList.size());
    }

    @SuppressWarnings("unchecked")
    private static <E> void doSortClient(List<E> fullList, SortCriteria sortCriteria) {
        for (SortColumn sortColumn : sortCriteria.getSortColumnList()) {
            int sortDirection = sortColumn.getDirection() ? 1 : -1;
            fullList.sort((e1, e2) -> {
                //                if (e1 instanceof JsonObject) {
                //                    try {
                //                        JsonElement value1 = JsonUtil.getPropertyValue(((JsonObject) e1), sortColumn.getPath());
                //                        JsonElement value2 = JsonUtil.getPropertyValue(((JsonObject) e2), sortColumn.getPath());
                //                        return value1.getAsString().compareTo(value2.getAsString()) * sortDirection;
                //                    } catch (Exception ex0) {
                //                        return 0;
                //                    }
                //                } else {
                //                    try {
                //                        // Try comparing with BeanUtils
                //                        Object value1 = PropertyUtils.getProperty(e1, sortColumn.getPath());
                //                        if (value1 instanceof Comparable) {
                //                            return ((Comparable) value1)
                //                                    .compareTo(PropertyUtils.getProperty(e2, sortColumn.getPath()))
                //                                    * sortDirection;
                //                        } else {
                //                            return (BeanUtils.getProperty(e1, sortColumn.getPath()))
                //                                    .compareTo(BeanUtils.getProperty(e2, sortColumn.getPath()))
                //                                    * sortDirection;
                //                        }
                //                    } catch (Exception ex1) {
                //                        try {
                //                            // Try comparing with introspection
                //                            Object value1 = ReflectionUtil.getFieldValue(e1, sortColumn.getPath());
                //                            Object value2 = ReflectionUtil.getFieldValue(e2, sortColumn.getPath());
                //                            if (value1 instanceof Comparable) {
                //                                return ((Comparable) value1)
                //                                        .compareTo(value2)
                //                                        * sortDirection;
                //                            } else {
                //                                return value1.toString()
                //                                        .compareTo(value2.toString())
                //                                        * sortDirection;
                //                            }
                //                        } catch (Exception ex2) {
                //                            // I don't know how to sort
                //                            return 0;
                //                        }
                //                    }
                //                }
                return 0; // FIXME
            });
        }
    }

    private static <E> List<E> doFilterClient(List<E> fullList, FilterCriteria filterCriteria) {
        for (FilterColumn filterColumn : filterCriteria.getFilterColumnList()) {
            fullList = fullList.stream().filter(e -> {
                // Try getting value from JsonObject
                //                if (e instanceof JsonObject) {
                //                    try {
                //                        return JsonUtil.getPropertyValue(((JsonObject) e), filterColumn.getColumn()).toString().contains(filterColumn.getFilter());
                //                    } catch (Exception ex0) {
                //                        return true;
                //                    }
                //                } else {
                //                    try {
                //                        // Try getting value with BeanUtils
                //                        return BeanUtils.getProperty(e, filterColumn.getColumn()).contains(filterColumn.getFilter());
                //                    } catch (Exception ex) {
                //                        // Try getting value with introspection
                //                        try {
                //                            // Try comparing with introspection
                //                            return ReflectionUtil.getFieldValue(e, filterColumn.getColumn()).toString().contains(filterColumn.getFilter());
                //                        } catch (Exception ex2) {
                //                            // I don't know how to filter
                //                            return true;
                //                        }
                //                    }
                //                }
                return true; // FIXME
            }).collect(Collectors.toList());
        }
        return fullList;
    }

    private static <T> List<T> doPaginateClient(PaginatedList<T> paginatedList, List<T> fullList) {
        return fullList.subList(
                paginatedList.getOffset(),
                Integer.min(paginatedList.getOffset() + paginatedList.getLimit(), fullList.size()));
    }
}
