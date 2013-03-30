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

package de.otto.jsonhome.generator;

import java.net.URI;

import static java.net.URI.create;

/**
 * A build used to build normalized URIs in jsonhome.
 *
 * @author Guido Steinacker
 * @since 30.03.13
 */
public final class UriBuilder {

    private final StringBuilder uri;

    private UriBuilder(final URI baseUri) {
        this.uri = new StringBuilder(withoutTrailingSlash(baseUri.toString()));
    }

    public static UriBuilder normalized(final URI baseUri) {
        return new UriBuilder(baseUri);
    }

    public UriBuilder withPathSegment(final String segment) {
        if (segment != null && !segment.isEmpty()) {
            uri.append(withLeadingSlash(withoutTrailingSlash(segment)));
        }
        return this;
    }

    public UriBuilder withPathSegments(final String... pathSegments) {
        for (final String pathSegment : pathSegments) {
            withPathSegment(pathSegment);
        }
        return this;
    }

    public URI toUri() {
        return create(uri.toString());
    }

    public String toString() {
        return uri.toString();
    }

    private static String withoutTrailingSlash(final String uri) {
        if (uri.endsWith("/")) {
            return uri.substring(0, uri.length() - 1);
        } else {
            return uri;
        }
    }

    private static String withLeadingSlash(final String uri) {
        if (uri.startsWith("/")) {
            return uri;
        } else {
            return "/" + uri;
        }
    }
}
