package com.sismics.nativedb;

import javax.persistence.Query;

/**
 * @author jtremeaux
 */
@FunctionalInterface
public interface OnNativeQuery {
    void run(Query query);
}
