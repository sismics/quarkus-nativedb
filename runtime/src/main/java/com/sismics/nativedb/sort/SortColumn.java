package com.sismics.nativedb.sort;

/**
 * Sort column.
 *
 * @author jtremeaux
 */
public class SortColumn {
    /**
     * Path to sort.
     */
    private String path;

    /**
     * Sort in ascending order (or else descending).
     */
    private boolean direction;

    /**
     * Use table prefix.
     */
    private boolean prefixed;

    public SortColumn(String path, boolean direction, boolean prefixed) {
        this.path = path;
        this.direction = direction;
        this.prefixed = prefixed;
    }

    public SortColumn(String path, boolean direction) {
        this(path, direction, false);
    }

    public String getPath() {
        return path;
    }

    public boolean getDirection() {
        return direction;
    }

    public boolean isPrefixed() {
        return prefixed;
    }
}
