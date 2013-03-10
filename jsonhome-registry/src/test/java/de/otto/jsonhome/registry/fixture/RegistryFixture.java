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

import java.util.*;

import static java.util.Collections.singletonList;

/**
 * @author Guido Steinacker
 * @since 04.02.13
 */
public class RegistryFixture {

    /**
     *     {
     *         "name" : "live",
     *         "title" : "Registry of the live environment",
     *         "self" : "http://example.org/registries/live",
     *         "container" : "http://example.org/registries",
     *         "service" : [
     *              {
     *                  "title" : "Home document of application foo",
     *                  "href" : "http://example.org/foo/json-home"
     *              }
     *         ]
     *     }
     * @param registryName the name of the registry
     */
    public static Map<String, Object> registryLiveWithSingleLinkTo(final String registryName) {

        final Map<String, String> link = new LinkedHashMap<String, String>();
        link.put("title", "Home document of application foo");
        link.put("href", "http://example.org/" + registryName + "/json-home");

        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", "live");
        map.put("title", "Registry of the live environment");
        map.put("self", "http://example.org/registries/live");
        map.put("container", "http://example.org/registries");
        map.put("service", singletonList(link));

        return map;
    }

    /**
     *  {
     *      "name" : "live",
     *      "title" : "Registry of the live environment",
     *      "self" : "http://example.org/registries/live",
     *      "container" : "http://example.org/registries",
     *      "service" : [
     *          {
     *              "title" : "Home document of the foo application",
     *              "href" : "http://example.org/foo/json-home",
     *          },
     *          {
     *              "title" : "Home document of the bar application",
     *              "href" : "http://example.org/bar/json-home",
     *          }
     *      ]
     *  }
     */
    public static Map<String, Object> doubleEntryRegistry() {

        final Map<String, String> foo = new HashMap<String, String>();
        foo.put("title", "Home document of the foo application");
        foo.put("href", "http://example.org/foo/json-home");

        final Map<String, String> bar = new HashMap<String, String>();
        bar.put("title", "Home document of the bar application");
        bar.put("href", "http://example.org/bar/json-home");

        final List<Map<String, String>> entries = new ArrayList<Map<String, String>>();
        entries.add(foo);
        entries.add(bar);

        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", "live");
        map.put("title", "Registry of the live environment");
        map.put("self", "http://example.org/registries/live");
        map.put("container", "http://example.org/registries");
        map.put("service", entries);
        return map;
    }
}
