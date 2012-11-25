package de.otto.jsonhome.converter;

import java.util.EnumSet;

/**
 * @author Guido Steinacker
 * @since 25.11.12
 */
public enum JsonHomeMediaType {

    APPLICATION_JSON("application/json"),
    APPLICATION_JSONHOME("application/json-home");

    public static JsonHomeMediaType mediaTypeFrom(final String value) {
        for (final JsonHomeMediaType mediaType : EnumSet.allOf(JsonHomeMediaType.class)) {
            if (mediaType.toString().equals(value)) {
                return mediaType;
            }
        }
        throw new IllegalArgumentException("Unknown media type '" + value + "'.");
    }

    private final String value;

    private JsonHomeMediaType(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the media type: application/json, application/json-home.
     * @return value
     */
    @Override
    public String toString() {
        return value;
    }
}
