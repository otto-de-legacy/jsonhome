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

/**
 * Client used to retrieve JsonHome documents from different sources.
 * <p/>
 * If the json-home document is retrieved using HTTP GET, the implementation should respect the
 * HTTP cache headers returned by the server.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public interface JsonHomeClient {

    /**
     * Updates the (possibly cached) JsonHome instance identified by the URI and returns the updated instance.
     *
     * @param uri the URI uniquely identifying the JsonHome instance.
     * @return JsonHome
     * @throws NotFoundException if the requested JsonHome was not found.
     * @throws JsonHomeClientException different kind of runtime exceptions, depending on the reason why processing of the
     * request failed. Most notably: HttpStatusCodeException indication a HTTP client or server error in HTTP based
     * implementations of the JsonHomeClient, or IllegalArgumentException in case of an invalid json-home document.
     */
    public JsonHome updateAndGet(final URI uri);

    /**
     * Gets the JsonHome document associated to the specified URI.
     * <p/>
     * The returned instance may be returned from a cache.
     *
     * @param uri the URI of the JsonHome document.
     * @return JsonHome
     * @throws NotFoundException if the requested JsonHome was not found.
     * @throws JsonHomeClientException different kind of runtime exceptions, depending on the reason why processing of the
     * request failed. Most notably: HttpStatusCodeException indication a HTTP client or server error in HTTP based
     * implementations of the JsonHomeClient, or IllegalArgumentException in case of an invalid json-home document.
     */
    public JsonHome get(final URI uri);

    /**
     * Shutdown the client and dispose all resources.
     * <p/>
     * Should be called when shutting down the client.
     */
    public void shutdown();
}
