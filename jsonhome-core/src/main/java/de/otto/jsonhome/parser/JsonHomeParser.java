package de.otto.jsonhome.parser;

import de.otto.jsonhome.model.JsonHome;

import java.io.InputStream;

/**
 * JsonHomeParser is used to parse a json-home document and return a JsonHome instance.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public interface JsonHomeParser {

    /**
     * Parses the json-home document from an InputStream and returns a JsonHome instance.
     * @param stream the stream used to read the json-home document.
     * @return JsonHome
     */
    public JsonHome parse(final InputStream stream);

}
