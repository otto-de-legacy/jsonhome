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

import java.net.URI;
import java.util.*;

/**
 * @author Guido Steinacker
 * @since 11.02.13
 */
public class LinkConverter {

    private LinkConverter() {}

    public static Map<String, String> linkToJson(final Link link) {
        final Map<String, String> json = new LinkedHashMap<String, String>();
        json.put("href", link.getHref().toString());
        if (!link.getTitle().isEmpty()) {
            json.put("title", link.getTitle());
        }
        return json;
    }

    public static Link jsonToLink(final Map<String, String> json) {
        return new Link(
                URI.create(json.get("href")),
                json.get("title")
        );
    }

    public static List<Map<String,String>> linksToJson(final Collection<Link> links) {
        final List<Map<String,String>> result = new ArrayList<Map<String, String>>();
        for (final Link link : links) {
            result.add(linkToJson(link));
        }
        return result;
    }

    public static List<Link> jsonToLinks(final Collection<Map<String,String>> json) {
        final List<Link> result = new ArrayList<Link>();
        for (Map<String, String> stringStringMap : json) {
            result.add(jsonToLink(stringStringMap));
        }
        return result;
    }
}
