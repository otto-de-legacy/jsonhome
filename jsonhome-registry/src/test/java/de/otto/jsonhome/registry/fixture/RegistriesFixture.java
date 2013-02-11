package de.otto.jsonhome.registry.fixture;

import de.otto.jsonhome.registry.store.Link;

import java.net.URI;
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
