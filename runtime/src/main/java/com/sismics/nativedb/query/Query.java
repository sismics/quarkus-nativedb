package com.sismics.nativedb.query;

import com.sismics.nativedb.Db;
import org.hibernate.QueryException;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.type.StandardBasicTypes;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Query builder that encapsulates JPA native query builder, and handles null values properly.
 *
 * @author jtremeaux
 */
public class Query {
    private NativeQueryImpl jpaQuery;

    public Query(String queryString) {
        jpaQuery = (NativeQueryImpl) createQuery(queryString);
    }

    private javax.persistence.Query createQuery(String queryString) {
        return Db.getSession().createNativeQuery(queryString);
    }

    public Query setParameter(String key, String value) {
        jpaQuery.setParameter(key, value);
        return this;
    }

    public Query setParameter(String key, Short value) {
        jpaQuery.setParameter(key, value, StandardBasicTypes.SHORT);
        return this;
    }

    public Query setParameter(String key, Integer value) {
        jpaQuery.setParameter(key, value, StandardBasicTypes.INTEGER);
        return this;
    }

    public Query setParameter(String key, Long value) {
        jpaQuery.setParameter(key, value, StandardBasicTypes.LONG);
        return this;
    }

    public Query setParameter(String key, Float value) {
        jpaQuery.setParameter(key, value, StandardBasicTypes.FLOAT);
        return this;
    }

    public Query setParameter(String key, Double value) {
        jpaQuery.setParameter(key, value, StandardBasicTypes.DOUBLE);
        return this;
    }

    public Query setParameter(String key, BigDecimal value) {
        jpaQuery.setParameter(key, value, StandardBasicTypes.BIG_DECIMAL);
        return this;
    }

    public Query setParameter(String key, Boolean value) {
        jpaQuery.setParameter(key, value, StandardBasicTypes.BOOLEAN);
        return this;
    }

    public Query setParameter(String key, Date value) {
        jpaQuery.setParameter(key, value, StandardBasicTypes.TIMESTAMP);
        return this;
    }

    public Query setParameter(String key, UUID value) {
        setParameterUUID(jpaQuery, key, value);
        return this;
    }

    public static void setParameterUUID(NativeQueryImpl query, String key, Object value) {
        if (Db.isDriverH2()) {
            query.setParameter(key, value, StandardBasicTypes.UUID_CHAR);
        } else if (Db.isDriverPostgresql()) {
            query.setParameter(key, value, org.hibernate.type.PostgresUUIDType.INSTANCE);
        }
    }

    public static void setParameterUUIDCollection(NativeQueryImpl query, String key, Collection value) {
        if (Db.isDriverH2()) {
            query.setParameterList(key, value);
        } else if (Db.isDriverPostgresql()) {
            if (value == null) {
                throw new QueryException("Collection must be not null!");
            } else {
                query.setParameterList(key, value);
            }
        }
    }

    public Query setParameter(String key, Object value) {
        jpaQuery.setParameter(key, value);
        return this;
    }

    public int executeUpdate() {
        return jpaQuery.executeUpdate();
    }
}
