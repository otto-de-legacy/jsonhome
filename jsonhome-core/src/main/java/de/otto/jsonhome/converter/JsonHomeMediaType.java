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
