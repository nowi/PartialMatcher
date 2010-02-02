package de.unima.semweb.partialmatcher.core.evaluation;

import de.unima.semweb.partialmatcher.core.MatchingResult;

/**
 * User: nowi
 * Date: 20.04.2009
 * Time: 17:53:33
 */
public interface ResultRanker {
    public Float rank(MatchingResult result);
}
