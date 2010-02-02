package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLDescriptionVisitorAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.rewriters.GenericOwlTermRewriter;

/**
 * User: nowi
 * Date: 13.03.2008
 * Time: 10:51:00
 */
class OWLRewritingDescriptionVisitor extends OWLDescriptionVisitorAdapter {

    public OWLRewritingDescriptionVisitor(GenericOwlTermRewriter rewriter){
        this.rewriter = rewriter;
    }

    GenericOwlTermRewriter rewriter;
    Map<OWLDescription, OWLDescription> replacings = new HashMap<OWLDescription, OWLDescription>() ;

    public void visitDescription(OWLDescription description){
        OWLDescription rewrittenDescription = rewriter.rewrite(new GenericOWLDescriptionRewritingContext(description));
        replacings.put(description,rewrittenDescription);

    }


    @Override
    public void visit(OWLClass owlClass) {
        visitDescription(owlClass);
        super.visit(owlClass);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectIntersectionOf owlObjectIntersectionOf) {
        visitDescription(owlObjectIntersectionOf);
        super.visit(owlObjectIntersectionOf);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectUnionOf owlObjectUnionOf) {
        visitDescription(owlObjectUnionOf);
        super.visit(owlObjectUnionOf);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectComplementOf owlObjectComplementOf) {
        visitDescription(owlObjectComplementOf);
        super.visit(owlObjectComplementOf);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectSomeRestriction owlObjectSomeRestriction) {
        visitDescription(owlObjectSomeRestriction);
        super.visit(owlObjectSomeRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectAllRestriction owlObjectAllRestriction) {
        visitDescription(owlObjectAllRestriction);
        super.visit(owlObjectAllRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectValueRestriction owlObjectValueRestriction) {
        visitDescription(owlObjectValueRestriction);
        super.visit(owlObjectValueRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectMinCardinalityRestriction owlObjectMinCardinalityRestriction) {
        visitDescription(owlObjectMinCardinalityRestriction);
        super.visit(owlObjectMinCardinalityRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectExactCardinalityRestriction owlObjectExactCardinalityRestriction) {
        visitDescription(owlObjectExactCardinalityRestriction);
        super.visit(owlObjectExactCardinalityRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectMaxCardinalityRestriction owlObjectMaxCardinalityRestriction) {
        visitDescription(owlObjectMaxCardinalityRestriction);
        super.visit(owlObjectMaxCardinalityRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectSelfRestriction owlObjectSelfRestriction) {
        visitDescription(owlObjectSelfRestriction);
        super.visit(owlObjectSelfRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLObjectOneOf owlObjectOneOf) {
        visitDescription(owlObjectOneOf);
        super.visit(owlObjectOneOf);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLDataSomeRestriction owlDataSomeRestriction) {
        visitDescription(owlDataSomeRestriction);
        super.visit(owlDataSomeRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLDataAllRestriction owlDataAllRestriction) {
        visitDescription(owlDataAllRestriction);
        super.visit(owlDataAllRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLDataValueRestriction owlDataValueRestriction) {
        visitDescription(owlDataValueRestriction);
        super.visit(owlDataValueRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLDataMinCardinalityRestriction owlDataMinCardinalityRestriction) {
        visitDescription(owlDataMinCardinalityRestriction);
        super.visit(owlDataMinCardinalityRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLDataExactCardinalityRestriction owlDataExactCardinalityRestriction) {
        visitDescription(owlDataExactCardinalityRestriction);
        super.visit(owlDataExactCardinalityRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(OWLDataMaxCardinalityRestriction owlDataMaxCardinalityRestriction) {
        visitDescription(owlDataMaxCardinalityRestriction);
        super.visit(owlDataMaxCardinalityRestriction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    void clear(){
        replacings.clear();
    }

    public Map<OWLDescription,OWLDescription> getReplacements(){
        return Collections.unmodifiableMap(replacings);
    }

}
