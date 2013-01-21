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

import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collection;

import static java.net.URI.create;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class InMemoryRegistryTest {

    @Test
    public void shouldReturnRegistryName() {
        // given
        final Registry registry = new InMemoryRegistry("foo");
        // when
        final String name = registry.getName();
        // then
        assertEquals(name, "foo");
    }

    @Test
    public void shouldFindEntryByUri() {
        // given
        final Registry registry = new InMemoryRegistry("foo");
        final URI uri = create("http://example.org/registries/live/42");
        final JsonHomeRef jsonHomeRef = new JsonHomeRef(
                uri,
                "foo",
                create("http://example.org/foo/json-home")
        );
        registry.put(jsonHomeRef);
        // when
        final JsonHomeRef ref = registry.findBy(uri);
        // then
        assertEquals(ref, jsonHomeRef);
    }

    @Test
    public void shouldFindEntryByHref() {
        // given
        final Registry registry = new InMemoryRegistry("foo");
        final URI href = create("http://example.org/foo/json-home");
        final JsonHomeRef jsonHomeRef = new JsonHomeRef(
                create("http://example.org/registries/live/42"),
                "foo",
                href
        );
        registry.put(jsonHomeRef);
        // when
        final JsonHomeRef ref = registry.findByHref(href);
        // then
        assertEquals(ref, jsonHomeRef);
    }

    @Test
    public void shouldReturnRegisteredJsonHomeRef() {
        // given
        final Registry registry = new InMemoryRegistry("foo");
        final JsonHomeRef jsonHomeRef = new JsonHomeRef(
                create("http://example.org/registries/live/42"),
                "foo",
                create("http://example.org/foo/json-home")
        );
        registry.put(jsonHomeRef);
        // when
        final Collection<JsonHomeRef> entries = registry.getAll();
        // then
        assertEquals(entries, singletonList(jsonHomeRef));
    }

    @Test
    public void shouldRemoveRegisteredJsonHomeRef() {
        // given
        final Registry registry = new InMemoryRegistry("foo");
        final URI entry = create("http://example.org/registries/live/42");
        final JsonHomeRef jsonHomeRef = new JsonHomeRef(
                entry,
                "foo",
                create("http://example.org/foo/json-home")
        );
        registry.put(jsonHomeRef);
        // when
        registry.remove(entry);
        // then
        assertEquals(registry.getAll(), emptyList());
    }

    @Test
    public void shouldIgnoreRemovalOfUnknownJsonHomeRef() {
        // given
        final Registry registry = new InMemoryRegistry("foo");
        // when
        registry.remove(create("/doesNotExist"));
        // then
        assertEquals(registry.getAll(), emptyList());

    }

    @Test
    public void shouldReturnEmptyContent() {
        // given
        final Registry registry = new InMemoryRegistry("foo");
        // when
        final Collection<JsonHomeRef> refs = registry.getAll();
        // then
        assertEquals(refs, emptyList());
    }

    @Test
    public void shouldClearRegistry() {
        // given
        final Registry registry = new InMemoryRegistry("foo");
        final URI entry = create("http://example.org/registries/live/42");
        final JsonHomeRef jsonHomeRef = new JsonHomeRef(
                entry,
                "foo",
                create("http://example.org/foo/json-home")
        );
        registry.put(jsonHomeRef);
        // when
        registry.clear();
        // then
        assertEquals(registry.getAll(), emptyList());

    }

}
