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
