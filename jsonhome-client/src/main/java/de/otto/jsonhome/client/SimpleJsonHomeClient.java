/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the JavaHomeClient interface, serving registered JavaHome documents stored in memory.
 *
 * This implementation is thread-safe.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class SimpleJsonHomeClient implements JsonHomeClient {

    private final ConcurrentMap<URI, JsonHome> registry = new ConcurrentHashMap<URI, JsonHome>();

    /**
     * Registers a JsonHome instance using an URI, uniquely identifying the instance.
     *
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
     *
     * This method has the same behaviour as {@link #get(java.net.URI)}. If you want to update an already
     * registered resource, use {@link #register(java.net.URI, de.otto.jsonhome.model.JsonHome)}.
     */
    @Override
    public JsonHome updateAndGet(URI uri) {
        return get(uri);
    }

    /**
     * {@inheritDoc}
     *
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
