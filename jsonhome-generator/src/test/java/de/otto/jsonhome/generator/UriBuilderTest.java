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

import org.testng.annotations.Test;

import java.net.URI;

import static de.otto.jsonhome.generator.UriBuilder.normalized;
import static org.testng.Assert.assertEquals;

public class UriBuilderTest {

    @Test
    public void shouldIgnoreNullSegments() {
        // given
        final URI baseUri = URI.create("http://example.org/");
        // when
        final URI normalizedUri = normalized(baseUri).withPathSegment(null).withPathSegments(null, null).toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org");
    }

    @Test
    public void shouldIgnoreEmptySegments() {
        // given
        final URI baseUri = URI.create("http://example.org/");
        // when
        final URI normalizedUri = normalized(baseUri).withPathSegment("").withPathSegments("", "").toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org");
    }

    @Test
    public void shouldKeepUriWithoutTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org");
        // when
        final URI normalizedUri = normalized(baseUri).toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org");
    }

    @Test
    public void shouldBuildUriWithoutTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org/");
        // when
        final URI normalizedUri = normalized(baseUri).toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org");
    }

    @Test
    public void shouldAppendStringToUriWithoutTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org");
        // when
        final URI normalizedUri = normalized(baseUri)
                .withPathSegment("foo")
                .toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org/foo");
    }

    @Test
    public void shouldAppendStringsToUriWithoutTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org");
        // when
        final URI normalizedUri = normalized(baseUri)
                .withPathSegments("foo", "bar")
                .toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org/foo/bar");
    }

    @Test
    public void shouldAppendStringWithLeadingSlashToUriWithoutTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org");
        // when
        final URI normalizedUri = normalized(baseUri)
                .withPathSegment("/foo")
                .toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org/foo");
    }

    @Test
    public void shouldAppendStringsWithLeadingAndTrailingSlashToUriWithoutTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org");
        // when
        final URI normalizedUri = normalized(baseUri)
                .withPathSegments("foo/", "/bar/")
                .toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org/foo/bar");
    }

    @Test
    public void shouldAppendStringToUriWithTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org/");
        // when
        final URI normalizedUri = normalized(baseUri)
                .withPathSegment("foo")
                .toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org/foo");
    }

    @Test
    public void shouldAppendStringsToUriWithTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org/");
        // when
        final URI normalizedUri = normalized(baseUri)
                .withPathSegments("foo", "bar")
                .toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org/foo/bar");
    }

    @Test
    public void shouldAppendStringWithLeadingSlashToUriWithTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org/");
        // when
        final URI normalizedUri = normalized(baseUri)
                .withPathSegment("/foo")
                .toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org/foo");
    }

    @Test
    public void shouldAppendStringsWithLeadingAndTrailingSlashToUriWithTrailingSlash() {
        // given
        final URI baseUri = URI.create("http://example.org/");
        // when
        final URI normalizedUri = normalized(baseUri)
                .withPathSegments("foo/", "/bar/")
                .toUri();
        // then
        assertEquals(normalizedUri.toString(), "http://example.org/foo/bar");
    }
}
