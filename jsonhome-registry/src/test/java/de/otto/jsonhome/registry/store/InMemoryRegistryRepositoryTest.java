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
import java.util.Collections;
import java.util.Set;

import static java.util.Collections.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class InMemoryRegistryRepositoryTest {

    @Test
    public void shouldReturnNullForNotExistingLinks() {
        // given
        final RegistryRepository registries = new InMemoryRegistryRepository();
        // when
        final Registry registry = registries.getLinks("does not exist");
        // then
        assertNull(registry);
    }

    @Test
    public void shouldCreateEmptyLinks() {
        // given
        final RegistryRepository registries = new InMemoryRegistryRepository();
        // when
        registries.createOrUpdateLinks(new Registry("foo", Collections.<Link>emptyList()));
        // then
        final Registry registry = registries.getLinks("foo");
        assertEquals(registry.getAll(), emptyList());
    }

    @Test
    public void shouldDeleteLinks() {
        // given
        final RegistryRepository registries = new InMemoryRegistryRepository();
        registries.createOrUpdateLinks(new Registry("foo", Collections.<Link>emptyList()));
        // when
        registries.deleteLinks("foo");
        // then
        assertNull(registries.getLinks("foo"));
    }

    @Test
    public void shouldClearAllRegistries() {
        // given
        final RegistryRepository registries = new InMemoryRegistryRepository();
        registries.createOrUpdateLinks(new Registry("foo", Collections.<Link>emptyList()));
        // when
        registries.clear();
        // then
        assertEquals(registries.getKnownNames(), emptySet());
    }

    @Test
    public void emptyRegistriesShouldReturnEmptyNameList() {
        // given
        final RegistryRepository registries = new InMemoryRegistryRepository();
        // when
        final Set<String> knownRegistryNames = registries.getKnownNames();
        // then
        assertEquals(knownRegistryNames, emptySet());
    }

    @Test
    public void shouldReturnKnownRegistryNames() {
        // given
        final RegistryRepository registries = new InMemoryRegistryRepository();
        registries.createOrUpdateLinks(new Registry("foo", Collections.<Link>emptyList()));
        // when
        final Set<String> knownRegistryNames = registries.getKnownNames();
        // then
        assertEquals(knownRegistryNames, singleton("foo"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindAllEntriesFromSelectedRegistry() throws IOException {
        // given
        final RegistryRepository repository = new InMemoryRegistryRepository();
        final Link liveJsonHome = new Link(
                "foo",
                URI.create("http://example.org/live/json-home")
        );
        final Link testJsonHome = new Link(
                "foo",
                URI.create("http://example.org/test/json-home")
        );
        repository.createOrUpdateLinks(new Registry("test", singletonList(testJsonHome)));
        repository.createOrUpdateLinks(new Registry("live", singletonList(liveJsonHome)));
        // when
        final Collection<Link> entries = repository.getLinks("live").getAll();
        // then
        assertEquals(entries, singletonList(liveJsonHome));
    }

}
