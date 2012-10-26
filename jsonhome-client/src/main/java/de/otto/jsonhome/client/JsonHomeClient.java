package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;

import java.net.URI;

/**
 * Client used to retrieve JsonHome documents from different sources.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public interface JsonHomeClient {

    /**
     * Gets the JsonHome document associated to the specified URI.
     *
     * @param uri the URI of the JsonHome document.
     * @return JsonHome
     */
    public JsonHome get(final URI uri);

}
