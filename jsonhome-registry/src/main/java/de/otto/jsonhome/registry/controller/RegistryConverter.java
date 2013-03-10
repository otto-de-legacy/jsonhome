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
import de.otto.jsonhome.registry.store.Registry;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static de.otto.jsonhome.registry.controller.LinkConverter.jsonToLink;
import static de.otto.jsonhome.registry.controller.LinkConverter.linkToJson;

/**
 * A converter used to convert Registry and Link items into maps and vice versa.
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class RegistryConverter {

    private RegistryConverter() {}

    public static Registry jsonToRegistry(final Map<String, ?> map) {
        try {
            final List<Link> links = new ArrayList<Link>();
            @SuppressWarnings("unchecked")
            final List<Map<String,String>> linksMap = (List<Map<String, String>>) map.get("service");
            for (final Map<String, String> linkMap : linksMap) {
                links.add(jsonToLink(linkMap));
            }
            return new Registry(
                    (String) map.get("name"),
                    (String) map.get("title"),
                    links
            );
        } catch (final NullPointerException e) {
            throw new IllegalArgumentException("Map does not contain valid links", e);
        }
    }

    public static Map<String, Object> registryToJson(final URI baseUri, final Registry registry) {
        final Map<String, Object> content = new LinkedHashMap<String, Object>();
        content.put("name", registry.getName());
        content.put("title", registry.getTitle());
        content.put("self", baseUri + "/registries/" + registry.getName());
        content.put("container", baseUri + "/registries");
        final List<Map<String,String>> linksList = new ArrayList<Map<String, String>>();
        for (final Link link : registry.getAll()) {
            linksList.add(linkToJson(link));
        }
        content.put("service", linksList);
        return content;
    }

}
