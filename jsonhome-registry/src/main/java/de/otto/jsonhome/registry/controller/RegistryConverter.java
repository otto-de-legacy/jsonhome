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
import java.util.stream.Collectors;

import static de.otto.jsonhome.registry.controller.LinkConverter.jsonToLink;
import static de.otto.jsonhome.registry.controller.LinkConverter.linkToJson;
import static java.util.stream.Collectors.toList;

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
            @SuppressWarnings("unchecked")
            final List<Map<String,String>> linksMap = (List<Map<String, String>>) map.get("service");
            return new Registry(
                    (String) map.get("name"),
                    (String) map.get("title"),
                    linksMap
                            .stream()
                            .map(LinkConverter::jsonToLink)
                            .collect(toList())
            );
        } catch (final NullPointerException e) {
            throw new IllegalArgumentException("Map does not contain valid links", e);
        }
    }

    public static Map<String, Object> registryToJson(final URI baseUri, final Registry registry) {
        final Map<String, Object> content = new LinkedHashMap<>();
        content.put("name", registry.getName());
        content.put("title", registry.getTitle());
        content.put("self", baseUri + "/registries/" + registry.getName());
        content.put("container", baseUri + "/registries");
        content.put("service", registry.getAll()
                .stream()
                .map(LinkConverter::linkToJson)
                .collect(toList()));
        return content;
    }

}
