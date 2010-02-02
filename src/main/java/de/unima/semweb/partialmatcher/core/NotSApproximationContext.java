package de.unima.semweb.partialmatcher.core;


import org.semanticweb.owl.model.OWLClass;

import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.List;


/**
 * Holds information about current notS set , available Vocabulary
 */
public class NotSApproximationContext {
    //    @NotNull
    final Set<URI> notS;

    //    @Nullable
    final Set<URI> vocabulary;

    final OWLClass requestClass;

    public NotSApproximationContext(OWLClass requestClass, Set<URI> notS, Set<URI> vocabulary) {
        this.requestClass = requestClass;
        this.notS = notS;
        this.vocabulary = vocabulary;
    }

    // copy constrcutor
    public NotSApproximationContext(NotSApproximationContext contextNotS) {
        this.requestClass = contextNotS.requestClass;
        this.notS = contextNotS.notS;
        this.vocabulary = contextNotS.vocabulary;
    }


    //    @NotNull
    public Set<URI> getNotS() {
        return Collections.unmodifiableSet(notS);
    }


    //    @Nullable
    public Set<URI> getVocabulary() {
        return Collections.unmodifiableSet(vocabulary);
    }

    // the inclusionSet
    // the available vocabulary
    public OWLClass getRequestClass() {
        return requestClass;
    }

    @Override
    public String toString() {
        return "NotSApproximationContext{" +
                "notS=" + notS +
                ", vocabulary=" + vocabulary +
                ", requestClass=" + requestClass +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotSApproximationContext)) return false;

        NotSApproximationContext that = (NotSApproximationContext) o;

        if (notS != null ? !notS.equals(that.notS) : that.notS != null) return false;
        if (requestClass != null ? !requestClass.equals(that.requestClass) : that.requestClass != null) return false;
        if (vocabulary != null ? !vocabulary.equals(that.vocabulary) : that.vocabulary != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = notS != null ? notS.hashCode() : 0;
        result = 31 * result + (vocabulary != null ? vocabulary.hashCode() : 0);
        result = 31 * result + (requestClass != null ? requestClass.hashCode() : 0);
        return result;
    }
}