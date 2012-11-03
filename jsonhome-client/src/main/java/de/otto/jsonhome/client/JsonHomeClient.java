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
