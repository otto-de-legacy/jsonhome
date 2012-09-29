package de.steinacker.jsonhome.model;

import java.net.URI;
import java.util.*;

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableList;

/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
public final class DirectLink implements ResourceLink {

    private final URI relationType;
    private final URI href;
    private final List<String> allows;
    private final List<String> representations;

    private DirectLink(final URI relationType,
                       final URI href,
                       final Collection<String> allows,
                       final Collection<String> representations) {
        this.relationType = relationType;
        this.href = href;
        this.allows = unmodifiableList(new ArrayList<String>(allows));
        this.representations = unmodifiableList(new ArrayList<String>(representations));
    }

    public static DirectLink directLink(final URI relationType,
                                        final URI href,
                                        final List<String> allows,
                                        final List<String> representations) {
        return new DirectLink(relationType, href, allows, representations);
    }

    public URI getLinkRelationType() {
        return relationType;
    }

    public URI getHref() {
        return href;
    }

    @Override
    public List<String> getAllows() {
        return allows;
    }

    @Override
    public List<String> getRepresentations() {
        return representations;
    }

    @Override
    public ResourceLink mergeWith(ResourceLink other) {
        if (!other.isDirectLink()) {
            throw new IllegalArgumentException("Merging DirectLink with TemplatedLink is not supported.");
        }


        if (!relationType.equals(other.getLinkRelationType())) {
            throw new IllegalArgumentException("Resource links with different relation types can not be merged. One resource must not have multiple relation types.");
        }
        final DirectLink otherDirectLink = (DirectLink)other;
        if (!href.equals(otherDirectLink.getHref())) {
            throw new IllegalArgumentException("Resource links with different hrefs can not be merged.");
        }
        final Set<String> allows = new LinkedHashSet<String>(this.allows);
        allows.addAll(otherDirectLink.getAllows());
        final Set<String> representations = new LinkedHashSet<String>(this.representations);
        representations.addAll(otherDirectLink.getRepresentations());
        return new DirectLink(
                relationType,
                href,
                allows,
                representations
        );
    }

    @Override
    public boolean isDirectLink() {
        return true;
    }

    @Override
    public Map<String, ?> toJson() {
        final Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("allow", allows);
        hints.put("representations", representations);
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("href", href.toString());
        map.put("hints", hints);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectLink that = (DirectLink) o;

        if (allows != null ? !allows.equals(that.allows) : that.allows != null) return false;
        if (href != null ? !href.equals(that.href) : that.href != null) return false;
        if (relationType != null ? !relationType.equals(that.relationType) : that.relationType != null) return false;
        if (representations != null ? !representations.equals(that.representations) : that.representations != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = relationType != null ? relationType.hashCode() : 0;
        result = 31 * result + (href != null ? href.hashCode() : 0);
        result = 31 * result + (allows != null ? allows.hashCode() : 0);
        result = 31 * result + (representations != null ? representations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DirectLink{" +
                "relationType=" + relationType +
                ", href=" + href +
                ", allow=" + allows +
                ", representations=" + representations +
                '}';
    }
}
