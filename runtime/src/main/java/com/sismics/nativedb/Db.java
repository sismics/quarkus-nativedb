package com.sismics.nativedb;

import com.sismics.nativedb.query.Query;
import io.quarkus.arc.Arc;
import org.hibernate.internal.SessionImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

/**
 * @author jtremeaux
 */
@ApplicationScoped
public class Db {

    /**
     * Return the Hibernate session.
     *
     * @return The Hibernate session
     */
    public static SessionImpl getSession() {
        EntityManager entityManager = Arc.container().instance(EntityManager.class).get();
        if (entityManager == null) {
            throw new PersistenceException("No EntityManager found. Do you have any JPA entities defined?");
        }
        return (SessionImpl) entityManager.getDelegate();
    }

    /**
     * Build a new native (SQL) query.
     *
     * @param queryString The query string
     * @return The query
     */
    public static Query query(String queryString) {
        return new Query(queryString);
    }

    public static String getDateTrunc(String field) {
        if (isDriverH2()) {
            return "parsedatetime (year(" + field + ") || '-' || month(" + field + ") || '-' || day(" + field
                    + "), 'yyyy-MM-dd')";
        } else if (isDriverPostgresql()) {
            return "date_trunc('day', cast(" + field + " as timestamp))";
        } else {
            throw new RuntimeException("Unknown DB: " + getDriver());
        }
    }

    public static String getDateTimeTrunc(String field) {
        if (isDriverH2()) {
            return "parsedatetime (year(" + field + ") || '-' || month(" + field + ") || '-' || day(" + field
                    + ") || ' ' || hour(" + field + ")  || ':' || minute(" + field + ") || ':' || second(" + field
                    + "), 'yyyy-MM-dd hh:mm:ss')";
        } else if (isDriverPostgresql()) {
            return "date_trunc('second', cast(" + field + " as timestamp))";
        } else {
            throw new RuntimeException("Unknown DB: " + getDriver());
        }
    }

    public static String getYearTrunc(String field) {
        if (Db.isDriverH2()) {
            return "year(" + field + ")";
        } else if (Db.isDriverPostgresql()) {
            return "date_trunc('year', cast(" + field + " as timestamp))";
        } else {
            throw new RuntimeException("Unknown DB: " + Db.getDriver());
        }
    }

    public static String getMonthTrunc(String field) {
        if (Db.isDriverH2()) {
            return "parsedatetime (year(" + field + ") || '-' || month(" + field + ") || '-01', 'yyyy-MM-dd')";
        } else if (Db.isDriverPostgresql()) {
            return "date_trunc('month', cast(" + field + " as timestamp))";
        } else {
            throw new RuntimeException("Unknown DB: " + Db.getDriver());
        }
    }

    public static String getWeekTrunc(String field) {
        if (Db.isDriverH2()) {
            return "week(" + field + ")";
        } else if (Db.isDriverPostgresql()) {
            return "date_trunc('week', cast(" + field + " as timestamp))";
        } else {
            throw new RuntimeException("Unknown DB: " + Db.getDriver());
        }
    }

    public static String getStringAgg(String field, String orderBy, String separator, boolean distinct) {
        if (isDriverH2()) {
            return "group_concat(" + (distinct ? "distinct " : "") + field + " order by " + orderBy + " separator '" + separator
                    + "')";
        } else if (isDriverPostgresql()) {
            return "string_agg(" + (distinct ? "distinct " : "") + field + ", '" + separator + "' order by " + orderBy + ")";
        } else {
            throw new RuntimeException("Unknown DB: " + getDriver());
        }
    }

    public static String getDateAdd(String field, String add, String unit) {
        if (isDriverH2()) {
            return "dateadd('" + unit + "', " + add + ", " + field + ")";
        } else if (isDriverPostgresql()) {
            return field + " + (" + add + " * interval '1 " + unit + "')";
        } else {
            throw new RuntimeException("Unknown DB: " + getDriver());
        }
    }

    public static String getDateDiff(String field, String diff, String unit) {
        if (isDriverH2()) {
            return "datediff('" + unit + "', " + diff + ", " + field + ")";
        } else if (isDriverPostgresql()) {
            return field + " - (" + diff + " * interval '1 " + unit + "')";
        } else {
            throw new RuntimeException("Unknown DB: " + getDriver());
        }
    }

    public static String getDateAdd(String field, String add) {
        return getDateAdd(field, add, "SECOND");
    }

    public static String getDateDiff(String field, String diff) {
        return getDateDiff(field, diff, "SECOND");
    }

    public static String getStringAgg(String field) {
        return getStringAgg(field, field, ", ", false);
    }

    public static String escapeLike(String value) {
        return value.replaceAll("([%_])", "\\$1");
    }

    /**
     * Get the current transaction.
     *
     * @return The current transaction
     */
    public static EntityTransaction getTransaction() {
        return getSession().getTransaction();
    }

    /**
     * Rollback the current transaction, and start a new one.
     *
     */
    public static void rollbackTransaction() {
        getTransaction().rollback();
        getTransaction().begin();
    }

    /**
     * Commit the current transaction, and start a new one.
     *
     */
    public static void commitTransaction() {
        getTransaction().rollback();
        getTransaction().begin();
    }

    /**
     * Get the current driver name.
     *
     * @return The driver name
     */
    public static String getDriver() {
        return "h2"; // FIXME
        //        return Play.configuration.getProperty("db.driver");
        //        return Play.configuration.getProperty("db.driver");
    }

    /**
     * Checks if the driver is H2.
     *
     * @return The condition
     */
    public static boolean isDriverH2() {
        return getDriver().contains("h2");
    }

    /**
     * Checks if the driver is PostgreSQL.
     *
     * @return The condition
     */
    public static boolean isDriverPostgresql() {
        String driver = getDriver();
        return driver.contains("postgresql");
    }
}
