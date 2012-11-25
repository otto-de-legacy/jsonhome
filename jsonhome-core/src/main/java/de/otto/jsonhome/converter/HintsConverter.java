/*
 * *
 *  Copyright 2012 Guido Steinacker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.otto.jsonhome.converter;

import de.otto.jsonhome.model.Hints;
import de.otto.jsonhome.model.Status;

import java.util.LinkedHashMap;
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;

/**
 * A converter used to convert Hints into other representations.
 *
 * @author Guido Steinacker
 * @since 14.10.12
 */
public final class HintsConverter {

    private HintsConverter() {}

    /**
     * Returns a json-home conforming representation of the hints as a map.
     *
     * @param hints the hints to represent as a map.
     * @return map
     */
    public static Map<String, ?> toJsonHomeRepresentation(final Hints hints) {
        return toRepresentation(hints, APPLICATION_JSONHOME);
    }

    /**
     * Returns a representation of the hints as a map.
     * <p/>
     * Depending on the media type, different data is included in the map.
     *
     * @param hints the hints to represent as a map.
     * @param mediaType the media type of the representation.
     * @return map
     */
    public static Map<String, ?> toRepresentation(final Hints hints, final JsonHomeMediaType mediaType) {
        final Map<String, Object> jsonHints = new LinkedHashMap<String, Object>();
        jsonHints.put("allow", hints.getAllows());
        jsonHints.put("representations", hints.getRepresentations());
        if (!hints.getAcceptPut().isEmpty()) {
            jsonHints.put("accept-put", hints.getAcceptPut());
        }
        if (!hints.getAcceptPost().isEmpty()) {
            jsonHints.put("accept-post", hints.getAcceptPost());
        }
        if (!hints.getPreconditionReq().isEmpty()) {
            jsonHints.put("precondition-req", hints.getPreconditionReq());
        }
        if (!hints.getStatus().equals(Status.OK)) {
            jsonHints.put("status", hints.getStatus().name().toLowerCase());
        }
        if (hints.getDocs().hasLink()) {
            jsonHints.put("docs", hints.getDocs().getLink().toString());
        }
        if (mediaType.equals(JsonHomeMediaType.APPLICATION_JSON) && hints.getDocs().hasDescription()) {
            jsonHints.put("description", hints.getDocs().getDescription());
        }
        return jsonHints;
    }

}
