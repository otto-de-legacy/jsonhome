package de.otto.jsonhome.model;

import java.net.URI;
import java.util.*;

/**
 * A direct resource link.
 *
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4</a>
 * @author Guido Steinacker
 * @since 15.09.12
 */
public final class DirectLink implements ResourceLink {

    private final URI relationType;
    private final URI href;
    private final Hints hints;

    private DirectLink(final URI relationType,
                       final URI href,
                       final Hints hints) {
        this.relationType = relationType;
        this.href = href;
        this.hints = hints;
    }

    public static DirectLink directLink(final URI relationType,
                                        final URI href,
                                        final Hints hints) {
        return new DirectLink(relationType, href, hints);
    }

    public URI getLinkRelationType() {
        return relationType;
    }

    public URI getHref() {
        return href;
    }

    public Hints getHints() {
        return hints;
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
        return new DirectLink(
                relationType,
                href,
                hints.mergeWith(other.getHints())
        );
    }

    @Override
    public boolean isDirectLink() {
        return true;
    }

    @Override
    public Map<String, ?> toJson() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("href", href.toString());
        map.put("hints", hints.toJson());
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectLink that = (DirectLink) o;

        if (hints != null ? !hints.equals(that.hints) : that.hints != null) return false;
        if (href != null ? !href.equals(that.href) : that.href != null) return false;
        if (relationType != null ? !relationType.equals(that.relationType) : that.relationType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = relationType != null ? relationType.hashCode() : 0;
        result = 31 * result + (href != null ? href.hashCode() : 0);
        result = 31 * result + (hints != null ? hints.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DirectLink{" +
                "relationType=" + relationType +
                ", href=" + href +
                ", hints=" + hints +
                '}';
    }
}
