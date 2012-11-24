package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.JsonHome;

/**
 * A source for json-home documents.
 *
 * @author Guido Steinacker
 * @since 20.11.12
 */
public interface JsonHomeSource {

    /**
     * Returns a JsonHome instance.
     *
     * @return JsonHome.
     */
    public JsonHome getJsonHome();

}
