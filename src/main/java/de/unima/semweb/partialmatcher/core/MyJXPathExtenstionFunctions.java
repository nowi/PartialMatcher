package de.unima.semweb.partialmatcher.core;

import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Pointer;
import org.semanticweb.owl.model.*;

/**
 * User: nowi
 * Date: 14.01.2008
 * Time: 15:10:16
 */
public class MyJXPathExtenstionFunctions {

    public static boolean isOWLCardinalityRestriction(ExpressionContext context) {
        Pointer pointer = context.getContextNodePointer();
        return pointer != null && pointer.getValue() instanceof OWLCardinalityRestriction;
    }
    public static boolean isOWLObjectMinCardinalityRestriction(ExpressionContext context) {
        Pointer pointer = context.getContextNodePointer();
        return pointer != null && pointer.getValue() instanceof OWLObjectMinCardinalityRestriction;
    }

    public static boolean isOWLObjectMaxCardinalityRestriction(ExpressionContext context) {
        Pointer pointer = context.getContextNodePointer();
        return pointer != null && pointer.getValue() instanceof OWLObjectMaxCardinalityRestriction;
    }

    public static boolean isOWLObjectSomeRestriction(ExpressionContext context) {
        Pointer pointer = context.getContextNodePointer();
        return pointer != null && pointer.getValue() instanceof OWLObjectSomeRestriction;
    }

    public static boolean isOWLObjectAllRestriction(ExpressionContext context) {
        Pointer pointer = context.getContextNodePointer();
        return pointer != null && pointer.getValue() instanceof OWLObjectAllRestriction;
    }

    public static boolean isOWLClass(ExpressionContext context) {
        Pointer pointer = context.getContextNodePointer();
        return pointer != null && pointer.getValue() instanceof OWLClass;
    }

    public static boolean isOWLObject(ExpressionContext context) {
        Pointer pointer = context.getContextNodePointer();
        return pointer != null && pointer.getValue() instanceof OWLObject;
    }

    public static boolean isOWLNamedObject(ExpressionContext context) {
        Pointer pointer = context.getContextNodePointer();
        return pointer != null && pointer.getValue() instanceof OWLNamedObject;
    }

}
         