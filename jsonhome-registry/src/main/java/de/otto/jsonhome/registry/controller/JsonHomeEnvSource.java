package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.model.JsonHome;

/**
 * A source for json-home documents.
 *
 * @author Guido Steinacker
 * @since 20.11.12
 */
public interface JsonHomeEnvSource {

    /**
     * Returns a JsonHome instance for a specified environment.
     * <p/>
     * The default environment is "".
     */
    public JsonHome getJsonHome(String environment);

    /**
     * Returns a JsonHome instance.
     *
     * @return JsonHome.
     */
    public JsonHome getJsonHome();

}
