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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.net.URI.create;
import static java.util.stream.Collectors.toList;

/**
 * @author Guido Steinacker
 * @since 11.02.13
 */
public class LinkConverter {

    private LinkConverter() {}

    public static Map<String, String> linkToJson(final Link link) {
        final Map<String, String> json = new LinkedHashMap<>();
        json.put("href", link.getHref().toString());
        if (!link.getTitle().isEmpty()) {
            json.put("title", link.getTitle());
        }
        return json;
    }

    public static Link jsonToLink(final Map<String, String> json) {
        return new Link(
                create(json.get("href")),
                json.get("title")
        );
    }

    public static List<Map<String,String>> linksToJson(final Collection<Link> links) {
        return links
                .stream()
                .map(LinkConverter::linkToJson)
                .collect(toList());
    }

    public static List<Link> jsonToLinks(final Collection<Map<String,String>> json) {
        return json
                .stream()
                .map(LinkConverter::jsonToLink)
                .collect(toList());
    }
}
