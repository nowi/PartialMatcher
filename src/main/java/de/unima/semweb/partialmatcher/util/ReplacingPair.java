package de.unima.semweb.partialmatcher.util;

/**
 * User: nowi
 * Date: 05.03.2008
 * Time: 16:01:03
 */
public class ReplacingPair<F,S> {
    public F getReplaced() {
        return replaced;
    }

    public S getReplacement() {
        return replacement;
    }

    private F replaced;
    private S replacement;

    public ReplacingPair( F replaced, S replacement) {
        if( replaced == null || replacement == null )
            throw new NullPointerException();

        this.replaced = replaced;
        this.replacement = replacement;
    }

    public int hashCode() {
        return replaced.hashCode() + replacement.hashCode();
    }

    public boolean equals( Object o ) {
        if( o == this )
            return true;

        if( !(o instanceof Pair) )
            return false;

        ReplacingPair p = (ReplacingPair) o;

        return replaced.equals( p.getReplaced() ) && replacement.equals( p.getReplacement());
    }

    public String toString() {
        return "[" + replaced + ", " + replacement + "]";
    }
}

