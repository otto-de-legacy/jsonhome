package de.otto.jsonhome.registry.fixture;

import de.otto.jsonhome.registry.store.Link;

import java.net.URI;

/**
 * @author Guido Steinacker
 * @since 11.02.13
 */
public class LinkFixture {

    private LinkFixture() {}

    public static Link registryLink(final String registryName) {
        return registryLink(registryName, null);
    }

    public static Link registryLink(final String registryName, final String title) {
        return new Link(
                URI.create("http://example.org/registries/" + registryName),
                title);
    }
}
