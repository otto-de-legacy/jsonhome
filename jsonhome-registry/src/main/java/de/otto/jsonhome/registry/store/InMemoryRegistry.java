package de.otto.jsonhome.registry.store;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Guido Steinacker
 * @since 04.01.13
 */
public class InMemoryRegistry implements Registry {

    public static final ConcurrentMap<URI, RegistryEntry> registry = new ConcurrentHashMap<URI, RegistryEntry>();

    @Override
    public boolean put(final RegistryEntry entry) {
        final RegistryEntry registryEntry = findByHref(entry.getHref());
        if (registryEntry != null && !registryEntry.getSelf().equals(entry.getSelf())) {
            throw new IllegalArgumentException("An entry with same href but different URI already exists.");
        } else {
            final Object prev = registry.put(entry.getSelf(), entry);
            return prev == null;
        }
    }

    @Override
    public void remove(final URI uri) {
        registry.remove(uri);
    }

    @Override
    public Collection<RegistryEntry> getAllFrom(final String environment) {
        final Collection<RegistryEntry> filteredEntries = new ArrayList<RegistryEntry>();
        for (final RegistryEntry entry : registry.values()) {
            final String query = entry.getSelf().getQuery();
            if (query == null) {
                if (environment.isEmpty()) {
                    filteredEntries.add(entry);
                }
            } else {
                if (query.matches("environment=" + environment)) {
                    filteredEntries.add(entry);
                }
            }
        }
        return filteredEntries;
    }

    @Override
    public RegistryEntry findBy(final URI uri) {
        return registry.get(uri);
    }

    @Override
    public RegistryEntry findByHref(final URI href) {
        for (final RegistryEntry entry : registry.values()) {
            if (entry.getHref().equals(href)) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        registry.clear();
    }

}
