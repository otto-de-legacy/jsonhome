package de.otto.jsonhome.model;

import java.util.*;

import static java.util.Collections.unmodifiableList;

/**
 *
 * @author Guido Steinacker
 * @since 30.09.12
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5</a>
 */
public final class Hints {

    private final List<String> allows;
    private final List<String> representations;

    public Hints(Collection<String> allows, Collection<String> representations) {
        this.allows = unmodifiableList(new ArrayList<String>(allows));
        this.representations = unmodifiableList(new ArrayList<String>(representations));
    }

    /**
     * @return the list of allowed HTTP methods.
     * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5.1">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5.1</a>
     */
    public List<String> getAllows() {
        return allows;
    }

    /**
     * @return the list of representations supported for this resource link.
     * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5.2">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5.2</a>
     */
    public List<String> getRepresentations() {
        return representations;
    }

    /**
     * Merges the hints of two resource links..
     *
     * @param other the hints of the other resource link
     * @return a new, merged Hints instance
     */
    public Hints mergeWith(final Hints other) {
        final Set<String> allows = new LinkedHashSet<String>(this.allows);
        allows.addAll(other.getAllows());
        final Set<String> representations = new LinkedHashSet<String>(this.representations);
        representations.addAll(other.getRepresentations());
        return new Hints(allows, representations);
    }

    /**
     * @return a Java representation of a JSON document used to serialize a JsonHome document into application-json format.
     */
    public Map<String, ?> toJson() {
        final Map<String, Object> jsonHints = new HashMap<String, Object>();
        jsonHints.put("allow", allows);
        jsonHints.put("representations", representations);
        return jsonHints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hints hints = (Hints) o;

        if (allows != null ? !allows.equals(hints.allows) : hints.allows != null) return false;
        if (representations != null ? !representations.equals(hints.representations) : hints.representations != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = allows != null ? allows.hashCode() : 0;
        result = 31 * result + (representations != null ? representations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Hints{" +
                "allows=" + allows +
                ", representations=" + representations +
                '}';
    }
}
