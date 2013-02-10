package de.otto.jsonhome.registry.fixture;

import java.util.*;

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
     *          "http://example.org/registries/live"
     *      ]
     * }
     */
    public static Map<String, Object> oneRegistryNamed(final String registryName) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("self", "http://example.org/registries");
        map.put("registries", Collections.<String>singletonList("http://example.org/registries/" + registryName));
        return map;
    }

    /**
     *  {
     *      "self" : "http://example.org/registries",
     *      "registries" : [
     *          "http://example.org/registries/test",
     *          "http://example.org/registries/live"
     *      ]
     *  }
     */
    public static Map<String, Object> twoRegistriesTestAndLive() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("self", "http://example.org/registries");
        final List<String> registries = new ArrayList<String>();
        registries.add("http://example.org/registries/test");
        registries.add("http://example.org/registries/live");
        map.put("registries", registries);
        return map;
    }
}
