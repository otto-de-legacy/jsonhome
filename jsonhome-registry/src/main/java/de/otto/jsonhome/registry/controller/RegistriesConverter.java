package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.registry.store.Link;
import de.otto.jsonhome.registry.store.RegistryRepository;

import java.net.URI;
import java.util.*;

import static de.otto.jsonhome.registry.controller.LinkConverter.linksToJson;
import static java.util.Collections.sort;

/**
 * Converter used to convert the registries resource documents.
 *
 * @author Guido Steinacker
 * @since 10.02.13
 */
public class RegistriesConverter {

    private RegistriesConverter() {}

    public static Map<String, ?> registriesToJson(final URI baseUri, final RegistryRepository repository) {
        final Map<String, Object> json = new HashMap<String, Object>();
        json.put("self", baseUri + "/registries");
        json.put("registries", linksToJson(
                repositoryLinks(baseUri, repository))
        );
        return json;
    }

    private static List<Link> repositoryLinks(final URI baseUri, final RegistryRepository repository) {
        final List<Link> registries = new ArrayList<Link>();
        for (final String registryName : sortedNamesFrom(repository)) {
            registries.add(repository
                    .get(registryName)
                    .asLinkFor(baseUri)
            );
        }
        return registries;
    }

    private static List<String> sortedNamesFrom(final RegistryRepository repository) {
        final List<String> registryNames = new ArrayList<String>(repository.getKnownNames());
        sort(registryNames);
        return registryNames;
    }
}
