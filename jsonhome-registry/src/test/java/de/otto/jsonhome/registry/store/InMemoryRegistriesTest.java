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

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Set;

import static java.util.Collections.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class InMemoryRegistriesTest {

    @Test
    public void shouldReturnNullForNotExistingRegistry() {
        // given
        final Registries registries = new InMemoryRegistries();
        // when
        final Registry registry = registries.getRegistry("does not exist");
        // then
        assertNull(registry);
    }

    @Test
    public void shouldCreateRegistryWithEmptyContent() {
        // given
        final Registries registries = new InMemoryRegistries();
        // when
        registries.createRegistry("foo");
        // then
        final Registry registry = registries.getRegistry("foo");
        assertEquals(registry.getAll(), emptyList());
    }

    @Test
    public void shouldDeleteRegistry() {
        // given
        final Registries registries = new InMemoryRegistries();
        final String fooRegistry = "foo";
        registries.createRegistry(fooRegistry);
        // when
        registries.deleteRegistry(fooRegistry);
        // then
        assertNull(registries.getRegistry(fooRegistry));
    }

    @Test
    public void shouldClearAllRegistries() {
        // given
        final Registries registries = new InMemoryRegistries();
        registries.createRegistry("foo");
        registries.createRegistry("bar");
        // when
        registries.clear();
        // then
        assertEquals(registries.getKnownRegistryNames(), emptySet());
    }

    @Test
    public void emptyRegistriesShouldReturnEmptyNameList() {
        // given
        final Registries registries = new InMemoryRegistries();
        // when
        final Set<String> knownRegistryNames = registries.getKnownRegistryNames();
        // then
        assertEquals(knownRegistryNames, emptySet());
    }

    @Test
    public void shouldReturnKnownRegistryNames() {
        // given
        final Registries registries = new InMemoryRegistries();
        registries.createRegistry("foo");
        // when
        final Set<String> knownRegistryNames = registries.getKnownRegistryNames();
        // then
        assertEquals(knownRegistryNames, singleton("foo"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindAllEntriesFromSelectedRegistry() throws IOException {
        // given
        final Registries registries = new InMemoryRegistries();
        final JsonHomeRef liveJsonHome = new JsonHomeRef(
                URI.create("http://example.org/registries/live/42"),
                "foo",
                URI.create("http://example.org/foo/json-home")
        );
        final JsonHomeRef testJsonHome = new JsonHomeRef(
                URI.create("http://example.org/registries/test/0815"),
                "foo",
                URI.create("http://example.org/test/json-home")
        );
        registries.createRegistry("live");
        registries.createRegistry("test");
        registries.getRegistry("live").put(liveJsonHome);
        registries.getRegistry("test").put(testJsonHome);
        // when
        final Collection<JsonHomeRef> entries = registries.getRegistry("live").getAll();
        // then
        assertEquals(entries, singletonList(liveJsonHome));
    }

}
