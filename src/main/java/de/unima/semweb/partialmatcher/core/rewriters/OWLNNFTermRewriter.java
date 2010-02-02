package de.unima.semweb.partialmatcher.core.rewriters;

import de.unima.semweb.partialmatcher.core.rewriters.GenericOwlTermRewriter;

/**
 * User: nowi
 * Date: 14.03.2008
 * Time: 11:31:05
 */
public class OWLNNFTermRewriter extends GenericOwlTermRewriter {

    public OWLNNFTermRewriter(){
        super();
        setRewritingStrategy(new OWLNNFDescriptionRewritingStrategy());
    }
}
