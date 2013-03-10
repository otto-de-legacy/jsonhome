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

package de.otto.jsonhome.registry.store;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * An immutable named collection of Registry.
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
public final class Registry {

    private final String name;
    private final String title;
    private final List<Link> links;

    public Registry(final String name, final String title, final List<Link> links) {
        if (name == null) {
            throw new NullPointerException("Name of Links must not be null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name of Links must not be empty");
        }
        this.name = name;
        this.title = title;
        this.links = unmodifiableList(new ArrayList<Link>(links));
    }

    /**
     * Returns the name of the link collection.
     *
     * @return name of the links;
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the human-readable title of the registry.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns an unmodifiable collection of all registered links.
     *
     * @return collection of entries.
     */
    public Collection<Link> getAll() {
        return links;
    }

    /**
     * Returns the entry referring to the specified href.
     *
     * @param href URI of the link.
     * @return Link
     */
    public Link findByHref(final URI href) {
        for (final Link link : links) {
            if (link.getHref().equals(href)) {
                return link;
            }
        }
        return null;
    }

    public Link asLinkFor(final URI baseUri) {
        final URI uri;
        if (baseUri.toString().endsWith("/")) {
            uri = baseUri.resolve("registries/" + name);
        } else {
            uri = URI.create(baseUri.toString() + "/registries/" + name);
        }
        return new Link(
                uri,
                title
        );
    }
}
