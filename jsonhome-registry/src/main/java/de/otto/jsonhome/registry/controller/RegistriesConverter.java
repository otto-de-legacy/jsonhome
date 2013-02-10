package de.otto.jsonhome.registry.controller;

import java.net.URI;
import java.util.*;

import static java.util.Collections.sort;

/**
 * Converter used to convert the registries resource documents.
 *
 * @author Guido Steinacker
 * @since 10.02.13
 */
public class RegistriesConverter {

    private RegistriesConverter() {}

    public static Map<String, ?> knownRegistriesToJson(final URI baseUri, final Set<String> registryNames) {
        final Map<String, Object> json = new HashMap<String, Object>();
        final String containerUri = baseUri + "/registries";
        json.put("self", containerUri);
        final List<String> registries = new ArrayList<String>();
        for (final String registryName : registryNames) {
            registries.add(containerUri + "/" + registryName);
        }
        sort(registries);
        json.put("registries", registries);
        return json;
    }
}
