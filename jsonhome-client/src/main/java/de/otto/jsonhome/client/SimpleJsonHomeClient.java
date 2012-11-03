package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the JavaHomeClient interface, serving registered JavaHome documents stored in memory.
 * <p/>
 * This implementation is thread-safe.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class SimpleJsonHomeClient implements JsonHomeClient {

    private final ConcurrentMap<URI, JsonHome> registry = new ConcurrentHashMap<URI, JsonHome>();

    /**
     * Registers a JsonHome instance using an URI, uniquely identifying the instance.
     * <p/>
     * Registration of multiple instances using the same URI will only keep the last value.
     *
     * @param uri the URI of the JsonHome instance.
     * @param jsonHome the registered JsonHome instance.
     */
    public void register(final URI uri, final JsonHome jsonHome) {
        this.registry.put(uri, jsonHome);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This method has the same behaviour as {@link #get(java.net.URI)}
     */
    @Override
    public JsonHome updateAndGet(URI uri) {
        return get(uri);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation simply returns a previously registered instance.
     */
    public JsonHome get(final URI uri) {
        final JsonHome jsonHome = registry.get(uri);
        if (jsonHome != null) {
            return jsonHome;
        } else {
            throw new NotFoundException("JsonHome " + uri.toString() + " not found.");
        }
    }

    @Override
    public void shutdown() {
        registry.clear();
    }
}
