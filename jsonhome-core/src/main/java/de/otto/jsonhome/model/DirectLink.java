/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.otto.jsonhome.model;

import java.net.URI;

import static java.lang.String.format;

/**
 * A resource link describing a resource that is directly accessible using an URI.
 *
 * The type of the referred resource is defined by the relationType URI. Relation-type URIs should
 * be URLs, pointing to a documentation of the relationType semantics. Beside of finding documentation,
 * you can treat the relationType just like any other identifier.
 *
 * The resource itself is identified by the href attribute. The href attribute should always be an URL.
 *
 * The {@link Hints hints} of the DirectLink contain meta-information about the resource: which HTTP methods
 * are allowed, the supported representations, and so on. Because the json-home specification marks hints as optional,
 * you should not completely rely on the hints.
 *
 * This implementation is immutable.
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

    @Override
    public URI getLinkRelationType() {
        return relationType;
    }

    public URI getHref() {
        return href;
    }

    @Override
    public Hints getHints() {
        return hints;
    }

    @Override
    public boolean isDirectLink() {
        return true;
    }

    @Override
    public TemplatedLink asTemplatedLink() {
        throw new IllegalStateException("Not a templated link.");
    }

    @Override
    public DirectLink asDirectLink() {
        return this;
    }

    @Override
    public ResourceLink mergeWith(ResourceLink other) {
        if (!other.isDirectLink()) {
            throw new IllegalArgumentException(format(
                    "Merging DirectLink with TemplatedLink is not supported. \nDirectLink = %s \nTemplatedLink = %s", toString(), other));
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
