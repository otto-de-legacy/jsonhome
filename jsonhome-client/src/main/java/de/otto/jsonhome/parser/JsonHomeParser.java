package de.otto.jsonhome.parser;

import de.otto.jsonhome.model.JsonHome;

import java.io.InputStream;

/**
 * @author Guido Steinacker
 * @since 26.10.12
 */
public interface JsonHomeParser {
    JsonHomeParser fromStream(InputStream is);

    JsonHome parse();
}
