package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the JavaHomeClient interface, serving registered JavaHome documents stored in memory.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class SimpleJsonHomeClient implements JsonHomeClient {

    private final ConcurrentMap<URI, JsonHome> registry = new ConcurrentHashMap<URI, JsonHome>();

    public void register(final URI uri, final JsonHome jsonHome) {
        this.registry.put(uri, jsonHome);
    }

    public void unregister(final URI uri) {
        registry.remove(uri);
    }

    public JsonHome get(final URI uri) {
        return registry.get(uri);
    }
}
