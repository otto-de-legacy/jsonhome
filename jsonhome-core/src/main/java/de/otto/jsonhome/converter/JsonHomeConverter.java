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

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;

import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;
import static de.otto.jsonhome.converter.ResourceLinkConverter.toRepresentation;
import static java.util.Collections.singletonMap;

/**
 * Converter used to convert JsonHome instances into a map containing the information of a representation.
 *
 * The result may easily be converted into a format like JSON.
 *
 * @author Guido Steinacker
 * @since 14.10.12
 */
public final class JsonHomeConverter {

    private JsonHomeConverter() {}

    /**
     * Converts a JsonHome into json-home representation.
     *
     * @param jsonHome the JsonHome
     * @return Map containing information and structure of a json-home document.
     */
    public static Map<String, Map<String,Object>> toJsonHomeRepresentation(final JsonHome jsonHome) {
        return toRepresentation(jsonHome, APPLICATION_JSONHOME);
    }

    /**
     * Converts a JsonHome into the specified representation.
     *
     * @param jsonHome the JsonHome
     * @param mediaType the media type of the representation
     * @return Map containing information and structure specified by the media type.
     */
    public static Map<String, Map<String,Object>> toRepresentation(final JsonHome jsonHome,
                                                                   final JsonHomeMediaType mediaType) {
        final Map<String, Object> jsonResources = new HashMap<>();
        for (final ResourceLink resource : jsonHome.getResources().values()) {
            jsonResources.putAll(ResourceLinkConverter.toRepresentation(resource, mediaType));
        }
        return singletonMap(
                "resources",
                jsonResources
        );
    }

}
