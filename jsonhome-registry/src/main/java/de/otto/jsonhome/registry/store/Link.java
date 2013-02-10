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
package de.otto.jsonhome.registry.store;

import java.net.URI;

/**
 * A link to a resource, consisting of a title, link-relation type and href.
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
public final class Link {

    private final String title;
    private final URI href;

    /**
     * Creates a new link to a resource.
     *
     * @param title the title of the link.
     * @param href the URI of the linked json-home document.
     */
    public Link(final String title,
                final URI href) {
        if (href == null) {
            throw new NullPointerException("'rel' must not be null.");
        }
        if (!href.isAbsolute()) {
            throw new IllegalArgumentException("'href' must be an absolute URI.");
        }
        this.title = title != null ? title : "";
        this.href = href;
    }

    /**
     * Title of the system offering the json-home document.
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * The URI of the referred json-home document.
     *
     * @return absolute URI.
     */
    public URI getHref() {
        return href;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (href != null ? !href.equals(link.href) : link.href != null) return false;
        if (title != null ? !title.equals(link.title) : link.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (href != null ? href.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Link{" +
                "title='" + title + '\'' +
                ", href=" + href +
                '}';
    }
}
