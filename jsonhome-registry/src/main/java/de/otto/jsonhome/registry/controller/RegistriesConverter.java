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
package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.registry.store.JsonHomeRef;
import de.otto.jsonhome.registry.store.Registries;
import de.otto.jsonhome.registry.store.Registry;

import java.net.URI;
import java.util.*;

import static java.util.Collections.singletonMap;

/**
 * A converter used to convert Registry items into maps.
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class RegistriesConverter {

    private RegistriesConverter() {}

    /**
     * Converts a Collection of registry URIs into a Map, usable to generate the json representation of the registryNames collection.
     *
     * @param registryNames a collection of registry links.
     * @return Map
     */
    public static Map<String, List<String>> registriesToMap(final URI baseUri, final Collection<String> registryNames) {
        final List<String> registryUris = new ArrayList<String>();
        for (final String name : registryNames) {
            registryUris.add(baseUri + "/registries/" + name);
        }
        return singletonMap("registries", registryUris);
    }

    public static Map<String, Map<String, ?>> registriesToMap(final Registries registries) {
        final Map<String, Map<String, ?>> content = new HashMap<String, Map<String, ?>>();
        for (final String registryName : registries.getKnownRegistryNames()) {
            final Map<String, ?> registryMap = registryEntriesToMap(registries.getRegistry(registryName));
            content.put(registryName, registryMap);
        }
        return content;
    }

    /**
     * Converts a Collection of registry entries into a Map, usable to generate the json representation of the registry.
     *
     * @param registry a collection of registry registryContent.
     * @return Map
     */
    public static Map<String, ?> registryEntriesToMap(final Registry registry) {
        final List<Map<String, String>> entries = new ArrayList<Map<java.lang.String, java.lang.String>>();
        for (final JsonHomeRef jsonHomeRef : registry.getAll()) {
            final Map<String, String> map = registryEntryToMap(jsonHomeRef);
            map.put("item", jsonHomeRef.getSelf().toString());
            map.remove("self");
            entries.add(map);
        }
        return singletonMap(registry.getName(), entries);
    }

    /**
     * Converts a single registry entry into a Map, usable to generate the json representation of the entry.
     *
     * @param jsonHomeRef the converted registry entry.
     * @return Map
     */
    public static Map<String, String> registryEntryToMap(final JsonHomeRef jsonHomeRef) {
        if (jsonHomeRef != null) {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("title", jsonHomeRef.getTitle());
            map.put("href", jsonHomeRef.getHref().toString());
            return map;
        } else {
            return null;
        }
    }

    /**
     * Converts a Map into a registry entry.
     *
     * @param map Map containing: self: URI of the entry; title: title of the entry; href: the URI of the json-home.
     * @return JsonHomeRef
     */
    public static JsonHomeRef registryEntryFromMap(final Map<String, String> map) {
        return new JsonHomeRef(
                URI.create(map.get("self")),
                map.get("title"),
                URI.create(map.get("href"))
        );
    }
}
