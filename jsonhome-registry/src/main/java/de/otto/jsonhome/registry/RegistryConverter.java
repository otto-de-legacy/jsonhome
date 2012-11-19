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
package de.otto.jsonhome.registry;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * A converter used to convert Registry items into maps.
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class RegistryConverter {

    private RegistryConverter() {}

    /**
     * Converts a Collection of registry entries into a Map, usable to generate the json representation of the registry.
     *
     * @param registry a collection of registry entries.
     * @return Map
     */
    public static Map<String, ?> registryEntriesToMap(final Collection<RegistryEntry> registry) {
        final Map<String, Map<String, String>> entries = new HashMap<String, Map<String, String>>();
        for (final RegistryEntry registryEntry : registry) {
            final Map<String, String> map = registryEntryToMap(registryEntry);
            map.put("item", registryEntry.getSelf().toString());
            map.remove("self");
            entries.put(registryEntry.getHref().toString(), map);
        }
        return singletonMap("registry", entries);
    }

    /**
     * Converts a single registry entry into a Map, usable to generate the json representation of the entry.
     *
     * @param registryEntry the converted registry entry.
     * @return Map
     */
    public static Map<String, String> registryEntryToMap(final RegistryEntry registryEntry) {
        if (registryEntry != null) {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("title", registryEntry.getTitle());
            map.put("href", registryEntry.getHref().toString());
            return map;
        } else {
            return null;
        }
    }

    /**
     * Converts a Map into a registry entry.
     *
     * @param map Map containing: self: URI of the entry; title: title of the entry; href: the URI of the json-home.
     * @return RegistryEntry
     */
    public static RegistryEntry registryEntryFromMap(final Map<String, String> map) {
        return new RegistryEntry(
                URI.create(map.get("self")),
                map.get("title"),
                URI.create(map.get("href"))
        );
    }
}
