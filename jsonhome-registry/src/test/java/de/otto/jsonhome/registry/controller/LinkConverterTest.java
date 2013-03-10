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

package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.registry.store.Link;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.otto.jsonhome.registry.controller.LinkConverter.*;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * Unit tests of the LinkConverter
 *
 * @author Guido Steinacker
 * @since 11.02.13
 */
public class LinkConverterTest {

    @Test
    public void shouldConvertToMap() throws Exception {
        final Link link = new Link(create("http://example.org/foo"), "Foo");
        final Map<String, String> map = linkToJson(link);
        assertEquals(map.get("href"), "http://example.org/foo");
        assertEquals(map.get("title"), "Foo");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shouldThrowExceptionIfHrefIsNull() throws Exception {
        final Link link = new Link(null, "Foo");
        linkToJson(link);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfHrefIsRelative() throws Exception {
        final Link link = new Link(create("service"), "Foo");
        linkToJson(link);
    }

    @Test
    public void shouldHandleNullTitleProperly() throws Exception {
        final Link link = new Link(create("http://example.org/foo"), null);
        final Map<String, String> map = linkToJson(link);
        assertEquals(map.get("href"), "http://example.org/foo");
        assertEquals(map.containsKey("title"), false);
    }

    @Test
    public void shouldConvertMapToLink() throws Exception {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("href", "http://example.org/foo");
        map.put("title", "Foo");
        final Link link = jsonToLink(map);
        assertEquals(link, new Link(create("http://example.org/foo"), "Foo"));
    }

    @Test
    public void shouldConvertMapWithoutTitleToLink() throws Exception {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("href", "http://example.org/foo");
        final Link link = jsonToLink(map);
        assertEquals(link, new Link(create("http://example.org/foo"), null));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shouldFailToConvertMapWithoutHref() throws Exception {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("title", "Foo");
        jsonToLink(map);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shouldFailToConvertMapWithNullHref() throws Exception {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("href", null);
        map.put("title", "Foo");
        jsonToLink(map);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailToConvertMapWithRelativeHref() throws Exception {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("href", "foo");
        map.put("title", "Foo");
        jsonToLink(map);
    }

    @Test
    public void shouldConvertLinkBackAndForth() {
        final Link link = new Link(create("http://example.org/foo"), "Foo");
        final Link check = jsonToLink(linkToJson(link));
        assertEquals(check, link);
    }

    @Test
    public void shouldConvertLinks() {
        final List<Link> links = asList(
            new Link(create("http://example.org/foo"), "Foo"),
            new Link(create("http://example.org/bar"), "Bar")
        );
        final List<Map<String, String>> maps = linksToJson(links);
        assertEquals(maps.size(), 2);
        assertEquals(maps.get(0).get("href"), "http://example.org/foo");
        assertEquals(maps.get(0).get("title"), "Foo");
        assertEquals(maps.get(1).get("href"), "http://example.org/bar");
        assertEquals(maps.get(1).get("title"), "Bar");
    }

    @Test
    public void shouldConvertLinksBackAndForths() {
        final List<Link> links = asList(
                new Link(create("http://example.org/foo"), "Foo"),
                new Link(create("http://example.org/bar"), "Bar")
        );
        final List<Link> check = jsonToLinks(linksToJson(links));
        assertEquals(check, links);
    }
}
