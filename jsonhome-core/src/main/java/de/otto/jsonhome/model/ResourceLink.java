package de.otto.jsonhome.model;

import java.net.URI;
import java.util.Map;

/**
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02">http://tools.ietf.org/html/draft-nottingham-json-home-02</a>
 * @author Guido Steinacker
 * @since 16.09.12
 */
public interface ResourceLink {

    /**
     * A human-readable name of the resource.
     */
    public String getName();

    /**
     * The link-relation type of the resource link
     *
     * @return URI of the link-relation type.
     * @see <a href="http://tools.ietf.org/html/rfc5988">http://tools.ietf.org/html/rfc5988</a>
     */
    public URI getLinkRelationType();

    /**
     * @return true, if the resource link is a direct link, false if it is a templated link.
     */
    public boolean isDirectLink();

    /**
     * Returns the hints of a ResourceLink object.
     *
     * @return hints
     * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5</a>
     */
    public Hints getHints();

    /**
     * Merges this resource link with another one and returns a new instance.
     *
     * There are a number of restrictions that must be met by the other ResourceLink:
     * <ul>
     *     <li>Both links must be of the same kind: either both DirectLinks or both TemplatedLinks</li>
     *     <li>The two link-relation types must be equal</li>
     *     <li>If both links are direct, the href must be equal.</li>
     *     <li>If both links are templated, the href-template and ref-vars must be equal.</li>
     * </ul>
     * Basically, only the hints are merged.
     *
     * @param other the other resource link
     * @return a new, merged ResourceLink instance
     */
    public ResourceLink mergeWith(ResourceLink other);

    /**
     * @return a Java representation of a JSON document used to serialize a JsonHome document into application-json format.
     */
    public Map<String, ?> toJson();
}
