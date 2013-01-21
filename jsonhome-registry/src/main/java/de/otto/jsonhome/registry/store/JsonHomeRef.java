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
 * A registry entry, referring to a json-home document by href.
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
public final class JsonHomeRef {

    private final URI self;
    private final String title;
    private final URI href;

    /**
     * Creates a new registry entry, referring to a json-home document.
     *
     * @param self the URI identifying the registry entry.
     * @param title the title, describing the system offering the json-home document.
     *                    It is expected to have different 'self' URIs for different environments.
     * @param href the URI of the linked json-home document.
     */
    public JsonHomeRef(final URI self,
                       final String title,
                       final URI href) {
        if (self == null || !self.isAbsolute()) {
            throw new IllegalArgumentException("'self' must be an absolute URI.");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("'title' must not be empty.");
        }
        if (href == null || !href.isAbsolute()) {
            throw new IllegalArgumentException("'href' must be an absolute URI.");
        }
        this.self = self;
        this.title = title;
        this.href = href;
    }

    /**
     * The URI of this registry entry.
     *
     * @return absolute URI
     */
    public URI getSelf() {
        return self;
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

        JsonHomeRef entry = (JsonHomeRef) o;

        if (href != null ? !href.equals(entry.href) : entry.href != null) return false;
        if (self != null ? !self.equals(entry.self) : entry.self != null) return false;
        if (title != null ? !title.equals(entry.title) : entry.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = self != null ? self.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (href != null ? href.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JsonHomeRef{" +
                "self=" + self +
                ", title='" + title + '\'' +
                ", href=" + href +
                '}';
    }
}
