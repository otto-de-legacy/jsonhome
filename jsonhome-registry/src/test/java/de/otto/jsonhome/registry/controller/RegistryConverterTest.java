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

import de.otto.jsonhome.registry.store.Registry;
import org.testng.annotations.Test;

import java.util.Map;

import static de.otto.jsonhome.registry.controller.RegistryConverter.jsonToRegistry;
import static de.otto.jsonhome.registry.controller.RegistryConverter.registryToJson;
import static de.otto.jsonhome.registry.fixture.RegistryFixture.doubleEntryRegistry;
import static de.otto.jsonhome.registry.fixture.RegistryFixture.registryLiveWithSingleLinkTo;
import static java.net.URI.create;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 09.02.13
 */
public class RegistryConverterTest {

    @Test
    public void shouldConvertRegistryWithSingleEntry() {
        // given:
        final Map<String, Object> linksMap = registryLiveWithSingleLinkTo("foo");
        // when:
        final Registry registry = jsonToRegistry(linksMap);
        // then:
        assertEquals(registryToJson(create("http://example.org"), registry), linksMap);
    }

    @Test
    public void shouldConvertRegistryWithMultipleEntries() {
        // given:
        final Map<String, Object> linksMap = doubleEntryRegistry();
        // when:
        final Registry registry = jsonToRegistry(linksMap);
        // then:
        assertEquals(registryToJson(create("http://example.org"), registry), linksMap);
    }
}
