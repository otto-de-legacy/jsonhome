package de.otto.jsonhome.registry.store;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory implementation of the {@link Registry} interface.
 */
public class InMemoryRegistries implements Registries {

    public final ConcurrentMap<String, Registry> registry = new ConcurrentHashMap<String, Registry>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void createRegistry(final String registryName) {
        registry.putIfAbsent(registryName, new InMemoryRegistry(registryName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRegistry(final String registryName) {
        registry.remove(registryName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getKnownRegistryNames() {
        return registry.keySet();
    }

    @Override
    public Registry getRegistry(final String registryName) {
        return registry.get(registryName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        registry.clear();
    }
}
