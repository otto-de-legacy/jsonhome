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

package de.otto.jsonhome.registry.fixture;

import de.otto.jsonhome.registry.store.Link;

import java.util.*;

import static de.otto.jsonhome.registry.controller.LinkConverter.linkToJson;
import static java.net.URI.create;
import static java.util.Collections.singletonMap;

/**
 * @author Guido Steinacker
 * @since 04.02.13
 */
public class RegistriesFixture {

    /**
     * {
     *      "self" : "http://example.org/registries",
     *      "registries" : []
     * }
     */
    public static Map<String, Object> emptyRegistries() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("self", "http://example.org/registries");
        map.put("registries", Collections.<String>emptyList());
        return map;
    }

    /**
     * {
     *      "self" : "http://example.org/registries",
     *      "registries" : [
     *          {"href" : "http://example.org/registries/live"}
     *      ]
     * }
     */
    public static Map<String, Object> oneRegistryNamed(final String registryName) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("self", "http://example.org/registries");
        map.put("registries", Collections.<Map<String,String>>singletonList(
                linkToJson(new Link(
                        create("http://example.org/registries/" + registryName),
                        "Registry of the " + registryName + " environment"))
        ));
        return map;
    }

    /**
     *  {
     *      "self" : "http://example.org/registries",
     *      "registries" : [
     *          {"href" : "http://example.org/registries/test"},
     *          {"href" : "http://example.org/registries/live"}
     *      ]
     *  }
     */
    public static Map<String, Object> twoRegistriesTestAndLive() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("self", "http://example.org/registries");
        final List<Map<String,String>> registries = new ArrayList<Map<String, String>>();
        registries.add(singletonMap("href", "http://example.org/registries/test"));
        registries.add(singletonMap("href", "http://example.org/registries/live"));
        map.put("registries", registries);
        return map;
    }
}
