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

import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.EnumSet.copyOf;
import static java.util.EnumSet.noneOf;

/**
 * Hints are used to describe a resource: the allowed HTTP methods, the supported representations,
 * required preconditions of an operation, the current status and possibly some documentation.
 * <p/>
 * All these information are optional. You should not completely rely on the information of the hints.
 * For example, only because the hints do no contain a HTTP method, this must not necessarily mean that
 * the method is not allowed for a resource.
 * <p/>
 * However, the JsonHomeGenerator will fortunately always find useful and correct hints...
 * <p/>
 * This implementation is immutable.
 *
 * @author Guido Steinacker
 * @since 30.09.12
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5</a>
 */
public final class Hints {

    public static final Hints EMPTY_HINTS = hints(
            EnumSet.noneOf(Allow.class), Collections.<String>emptyList()
    );

    private final Set<Allow> allows;
    private final List<String> representations;
    private final List<String> acceptPut;
    private final List<String> acceptPost;
    private final List<String> acceptPatch;
    private final List<String> acceptRanges;
    private final List<String> preferences;
    private final List<Precondition> preconditionReq;
    private final List<Authentication> authReq;
    private final Status status;
    private final Documentation docs;

    /**
     * Returns an empty Hints instance.
     *
     * @return EMPTY_HINTS
     */
    public static Hints emptyHints() {
        return EMPTY_HINTS;
    }

    /**
     * Creates hints with information about allowed HTTP methods and the supported representations of the resource.
     * <p/>
     * The status is set to Status.OK, all other fields will be empty (but not null).
     *
     * @param allows the allowed HTTP methods
     * @param representations the representations of the resource
     * @return Hints
     */
    public static Hints hints(final Set<Allow> allows,
                              final List<String> representations) {
        return hints(allows,
                representations,
                Collections.<String>emptyList(),
                Collections.<String>emptyList(),
                Collections.<String>emptyList(),
                Collections.<String>emptyList(),
                Collections.<String>emptyList(),
                Collections.<Precondition>emptyList(),
                Collections.<Authentication>emptyList(),
                Status.OK,
                emptyDocs());
    }

    public static Hints hints(final Set<Allow> allows,
                              final List<String> representations,
                              final List<String> acceptPut,
                              final List<String> acceptPost,
                              final List<String> acceptPatch,
                              final List<String> acceptRanges,
                              final List<String> preferences,
                              final List<Precondition> preconditionReq,
                              final List<Authentication> authReq,
                              final Status status,
                              final Documentation docs) {
        return new Hints(
                allows,
                representations, acceptPut, acceptPost, acceptPatch,
                acceptRanges,
                preferences,
                preconditionReq,
                authReq,
                status,
                docs);
    }

    private Hints(final Set<Allow> allows,
                  final List<String> representations,
                  final List<String> acceptPut,
                  final List<String> acceptPost,
                  final List<String> acceptPatch,
                  final List<String> acceptRanges,
                  final List<String> preferences,
                  final List<Precondition> preconditionReq,
                  final List<Authentication> authReq,
                  final Status status,
                  final Documentation docs) {
        if (!acceptPost.isEmpty() && !allows.contains(Allow.POST)) {
            throw new IllegalArgumentException("POST is not allowed but accept-post is provided.");
        }
        if (!acceptPut.isEmpty() && !allows.contains(Allow.PUT)) {
            throw new IllegalArgumentException("PUT is not allowed but accept-put is provided.");
        }
        if (!acceptPatch.isEmpty() && !allows.contains(Allow.PATCH)) {
            throw new IllegalArgumentException("PATCH is not allowed but accept-patch is provided.");
        }
        this.allows = unmodifiableSet(copyOf(allows));
        this.representations = unmodifiableList(new ArrayList<String>(representations));
        this.acceptPut = acceptPut;
        this.acceptPost = acceptPost;
        this.acceptPatch = acceptPatch;
        this.acceptRanges = unmodifiableList(acceptRanges);
        this.preferences = unmodifiableList(preferences);
        this.preconditionReq = unmodifiableList(new ArrayList<Precondition>(preconditionReq));
        this.authReq = unmodifiableList(new ArrayList<Authentication>(authReq));
        this.status = status != null ? status : Status.OK;
        this.docs = docs != null ? docs : emptyDocs();
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
     * @return the accept-put hint, declaring the accepted representations of a HTTP PUT request.
     */
    public List<String> getAcceptPut() {
        return acceptPut;
    }

    /**
     * @return the accept-post hint, declaring the accepted representations of a HTTP POST request.
     */
    public List<String> getAcceptPost() {
        return acceptPost;
    }

    /**
     * @return the accept-patch hint, declaring the accepted representations of a HTTP PATCH request.
     */
    public List<String> getAcceptPatch() {
        return acceptPatch;
    }

    /**
     * @return the accept-ranges hint, declaring the accepted range requests .
     */
    public List<String> getAcceptRanges() {
        return acceptRanges;
    }

    /**
     * @return the preferences supported by the hinted resource.
     */
    public List<String> getPreferences() {
        return preferences;
    }

    /**
     * @return the required preconditions.
     */
    public List<Precondition> getPreconditionReq() {
        return preconditionReq;
    }

    /**
     * @return the required authentication.
     */
    public List<Authentication> getAuthReq() {
        return authReq;
    }

    /**
     * @return Status specifies whether the resource is OK, DEPRECATED or GONE.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Human-readable documentation of a ResourceLink.
     *
     * @return Documentation
     */
    public Documentation getDocs() {
        return docs;
    }

    /**
     * Merges the hints of two resource links..
     *
     * @param other the hints of the other resource link
     * @return a new, merged Hints instance
     */
    public Hints mergeWith(final Hints other) {
        final EnumSet<Allow> allows = this.allows.isEmpty() ? noneOf(Allow.class) : copyOf(this.allows);
        allows.addAll(other.getAllows());
        final Set<String> representations = new LinkedHashSet<String>(this.representations);
        representations.addAll(other.getRepresentations());
        final Set<String> acceptPut = new LinkedHashSet<String>(this.acceptPut);
        acceptPut.addAll(other.getAcceptPut());
        final Set<String> acceptPost = new LinkedHashSet<String>(this.acceptPost);
        acceptPost.addAll(other.getAcceptPost());
        final Set<String> acceptPatch = new LinkedHashSet<String>(this.acceptPatch);
        acceptPatch.addAll(other.getAcceptPatch());
        final Set<String> acceptRanges = new LinkedHashSet<String>(this.acceptRanges);
        acceptRanges.addAll(other.getAcceptRanges());
        final Set<String> preferences = new LinkedHashSet<String>(this.preferences);
        preferences.addAll(other.getPreferences());
        final Set<Precondition> preconditionReq = new LinkedHashSet<Precondition>(this.preconditionReq);
        preconditionReq.addAll(other.getPreconditionReq());
        final List<Authentication> mergedAuth = mergeAuthReq(other.getAuthReq());
        return hints(
                allows,
                new ArrayList<String>(representations),
                new ArrayList<String>(acceptPut),
                new ArrayList<String>(acceptPost),
                new ArrayList<String>(acceptPatch),
                new ArrayList<String>(acceptRanges),
                new ArrayList<String>(preferences),
                new ArrayList<Precondition>(preconditionReq),
                mergedAuth,
                status.mergeWith(other.getStatus()),
                docs.mergeWith(other.getDocs())
        );
    }

    private List<Authentication> mergeAuthReq(final List<Authentication> otherAuthReq) {
        final Map<String, Set<String>> authReq = new TreeMap<String, Set<String>>();
        for (final Authentication auth : this.authReq) {
            authReq.put(auth.getScheme(), new TreeSet<String>(auth.getRealms()));
        }
        for (final Authentication auth : otherAuthReq) {
            if (authReq.containsKey(auth.getScheme())) {
                authReq.get(auth.getScheme()).addAll(auth.getRealms());
            } else {
                authReq.put(auth.getScheme(), new TreeSet<String>(auth.getRealms()));
            }
        }
        final List<Authentication> mergedAuth = new ArrayList<Authentication>();
        for (final String scheme : authReq.keySet()) {
            mergedAuth.add(Authentication.authReq(scheme, new ArrayList<String>(authReq.get(scheme))));
        }
        return mergedAuth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hints hints = (Hints) o;

        if (acceptPatch != null ? !acceptPatch.equals(hints.acceptPatch) : hints.acceptPatch != null) return false;
        if (acceptPost != null ? !acceptPost.equals(hints.acceptPost) : hints.acceptPost != null) return false;
        if (acceptPut != null ? !acceptPut.equals(hints.acceptPut) : hints.acceptPut != null) return false;
        if (acceptRanges != null ? !acceptRanges.equals(hints.acceptRanges) : hints.acceptRanges != null) return false;
        if (allows != null ? !allows.equals(hints.allows) : hints.allows != null) return false;
        if (authReq != null ? !authReq.equals(hints.authReq) : hints.authReq != null) return false;
        if (docs != null ? !docs.equals(hints.docs) : hints.docs != null) return false;
        if (preconditionReq != null ? !preconditionReq.equals(hints.preconditionReq) : hints.preconditionReq != null)
            return false;
        if (preferences != null ? !preferences.equals(hints.preferences) : hints.preferences != null) return false;
        if (representations != null ? !representations.equals(hints.representations) : hints.representations != null)
            return false;
        if (status != hints.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = allows != null ? allows.hashCode() : 0;
        result = 31 * result + (representations != null ? representations.hashCode() : 0);
        result = 31 * result + (acceptPut != null ? acceptPut.hashCode() : 0);
        result = 31 * result + (acceptPost != null ? acceptPost.hashCode() : 0);
        result = 31 * result + (acceptPatch != null ? acceptPatch.hashCode() : 0);
        result = 31 * result + (acceptRanges != null ? acceptRanges.hashCode() : 0);
        result = 31 * result + (preferences != null ? preferences.hashCode() : 0);
        result = 31 * result + (preconditionReq != null ? preconditionReq.hashCode() : 0);
        result = 31 * result + (authReq != null ? authReq.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
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
                ", acceptPatch=" + acceptPatch +
                ", acceptRanges=" + acceptRanges +
                ", preferences=" + preferences +
                ", preconditionReq=" + preconditionReq +
                ", authReq=" + authReq +
                ", status=" + status +
                ", docs=" + docs +
                '}';
    }

}
