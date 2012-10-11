/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.model;

import java.util.*;

import static de.otto.jsonhome.model.Docs.emptyDocumentation;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

/**
 *
 * @author Guido Steinacker
 * @since 30.09.12
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5</a>
 */
public final class Hints {

    private final Set<Allow> allows;
    private final List<String> representations;
    private final List<String> acceptPut;
    private final List<String> acceptPost;
    private final Docs docs;

    public Hints(final Set<Allow> allows, final List<String> representations) {
        this(allows, representations, Collections.<String>emptyList(), Collections.<String>emptyList(), emptyDocumentation());
    }

    public Hints(final Set<Allow> allows,
                 final List<String> representations,
                 final List<String> acceptPut,
                 final List<String> acceptPost,
                 final Docs docs) {
        if (!acceptPost.isEmpty() && !allows.contains(Allow.POST)) {
            throw new IllegalArgumentException("POST is not allowed but accept-post is provided.");
        }
        if (!acceptPut.isEmpty() && !allows.contains(Allow.PUT)) {
            throw new IllegalArgumentException("PUT is not allowed but accept-put is provided.");
        }
        this.allows = unmodifiableSet(EnumSet.copyOf(allows));
        this.representations = unmodifiableList(new ArrayList<String>(representations));
        this.acceptPut = acceptPut;
        this.acceptPost = acceptPost;
        this.docs = docs;
    }

    /**
     * @return the list of allowed HTTP methods.
     * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5.1">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5.1</a>
     */
    public Set<Allow> getAllows() {
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
     * @return the accept-put hint.
     */
    public List<String> getAcceptPut() {
        return acceptPut;
    }

    /**
     * @return the accept-post hint.
     */
    public List<String> getAcceptPost() {
        return acceptPost;
    }

    /**
     * Human-readable documentation of a ResourceLink.
     *
     * @return Docs
     */
    public Docs getDocs() {
        return docs;
    }

    /**
     * Merges the hints of two resource links..
     *
     * @param other the hints of the other resource link
     * @return a new, merged Hints instance
     */
    public Hints mergeWith(final Hints other) {
        final EnumSet<Allow> allows = EnumSet.copyOf(this.allows);
        allows.addAll(other.getAllows());
        final Set<String> representations = new LinkedHashSet<String>(this.representations);
        representations.addAll(other.getRepresentations());
        final Set<String> acceptPut = new LinkedHashSet<String>(this.acceptPut);
        acceptPut.addAll(other.getAcceptPut());
        final Set<String> acceptPost = new LinkedHashSet<String>(this.acceptPost);
        acceptPost.addAll(other.getAcceptPost());
        return new Hints(
                allows,
                new ArrayList<String>(representations),
                new ArrayList<String>(acceptPut),
                new ArrayList<String>(acceptPost),
                docs.mergeWith(other.getDocs()));
    }

    /**
     * @return a Java representation of a JSON document used to serialize a JsonHome document into application-json format.
     */
    public Map<String, ?> toJson() {
        final Map<String, Object> jsonHints = new HashMap<String, Object>();
        jsonHints.put("allow", allows);
        jsonHints.put("representations", representations);
        if (!acceptPut.isEmpty()) {
            jsonHints.put("accept-put", acceptPut);
        }
        if (!acceptPost.isEmpty()) {
            jsonHints.put("accept-post", acceptPost);
        }
        if (docs.hasLink()) {
            jsonHints.put("docs", docs.getLink().toString());
        }
        return jsonHints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hints hints = (Hints) o;

        if (acceptPost != null ? !acceptPost.equals(hints.acceptPost) : hints.acceptPost != null) return false;
        if (acceptPut != null ? !acceptPut.equals(hints.acceptPut) : hints.acceptPut != null) return false;
        if (allows != null ? !allows.equals(hints.allows) : hints.allows != null) return false;
        if (docs != null ? !docs.equals(hints.docs) : hints.docs != null)
            return false;
        if (representations != null ? !representations.equals(hints.representations) : hints.representations != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = allows != null ? allows.hashCode() : 0;
        result = 31 * result + (representations != null ? representations.hashCode() : 0);
        result = 31 * result + (acceptPut != null ? acceptPut.hashCode() : 0);
        result = 31 * result + (acceptPost != null ? acceptPost.hashCode() : 0);
        result = 31 * result + (docs != null ? docs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Hints{" +
                "allows=" + allows +
                ", representations=" + representations +
                ", acceptPut=" + acceptPut +
                ", acceptPost=" + acceptPost +
                ", docs=" + docs +
                '}';
    }
}
