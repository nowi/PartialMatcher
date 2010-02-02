package de.unima.semweb.partialmatcher.core;

/**
 * User: nowi
 * Date: 23.01.2008
 * Time: 14:21:02
 */
public enum ApproximationType {
    UPPER, LOWER;

    public ApproximationType invert() {
        if (this == UPPER) return LOWER;
        else
            return UPPER;
    }

}
