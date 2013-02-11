package de.otto.jsonhome.registry.store;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory implementation of the {@link RegistryRepository} interface.
 */
public class InMemoryRegistryRepository implements RegistryRepository {

    public final ConcurrentMap<String, Registry> registry = new ConcurrentHashMap<String, Registry>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(Registry registry) {
        this.registry.put(registry.getName(), registry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String name) {
        registry.remove(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getKnownNames() {
        return registry.keySet();
    }

    @Override
    public Registry get(final String registryName) {
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
