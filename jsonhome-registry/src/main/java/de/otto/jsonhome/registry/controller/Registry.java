package de.otto.jsonhome.registry.controller;

import java.net.URI;
import java.util.UUID;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public interface Registry {

    public void register(final RegistryEntry entry);

    public void unregister(final UUID uuid);

    public RegistryEntry get(final UUID uri);

    public RegistryEntry findByHref(final URI uri);

}
