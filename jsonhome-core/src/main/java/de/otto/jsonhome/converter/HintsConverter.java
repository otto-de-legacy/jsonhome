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

import de.otto.jsonhome.annotation.Status;
import de.otto.jsonhome.model.Hints;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A converter used to convert Hints into other representations.
 *
 * @author Guido Steinacker
 * @since 14.10.12
 */
public final class HintsConverter {

    private HintsConverter() {}

    /**
     * @return a Java representation of the hints of a JSON document.
     */
    public static Map<String, ?> hintsToJsonHome(final Hints hints) {
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
        return jsonHints;
    }

}
