package de.otto.jsonhome.registry.store;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Guido Steinacker
 * @since 04.01.13
 */
public class InMemoryRegistry implements Registry {

    private final ConcurrentMap<URI, JsonHomeRef> registry = new ConcurrentHashMap<URI, JsonHomeRef>();
    private final String registryName;

    public InMemoryRegistry(final String registryName) {
        this.registryName = registryName;
    }

    public InMemoryRegistry(final String registryName, final List<JsonHomeRef> entries) {
        this.registryName = registryName;
        for (final JsonHomeRef entry : entries) {
            registry.put(entry.getSelf(), entry);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return registryName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean put(final JsonHomeRef entry) {
        final JsonHomeRef jsonHomeRef = findByHref(entry.getHref());
        if (jsonHomeRef != null && !jsonHomeRef.getSelf().equals(entry.getSelf())) {
            throw new IllegalArgumentException("An entry with same href but different URI already exists.");
        } else {
            final Object prev = registry.put(entry.getSelf(), entry);
            return prev == null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final URI uri) {
        registry.remove(uri);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<JsonHomeRef> getAll() {
        return registry.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonHomeRef findBy(final URI uri) {
        return registry.get(uri);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonHomeRef findByHref(final URI href) {
        for (final JsonHomeRef entry : registry.values()) {
            if (entry.getHref().equals(href)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        registry.clear();
    }

}
