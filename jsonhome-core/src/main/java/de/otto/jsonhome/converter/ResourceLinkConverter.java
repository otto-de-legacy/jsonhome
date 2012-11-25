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

import de.otto.jsonhome.model.DirectLink;
import de.otto.jsonhome.model.HrefVar;
import de.otto.jsonhome.model.ResourceLink;
import de.otto.jsonhome.model.TemplatedLink;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converter used to convert a ResourceLink into a json-home resource.
 *
 * @author Guido Steinacker
 * @since 14.10.12
 */
public final class ResourceLinkConverter {

    public static Map<String, Map<String, Object>> toRepresentation(final ResourceLink resourceLink,
                                                                    final JsonHomeMediaType mediaType) {
        if (resourceLink.isDirectLink()) {
            return directLinkToJsonHome(resourceLink.asDirectLink(), mediaType);
        } else {
            return templatedLinkToJsonHome(resourceLink.asTemplatedLink(), mediaType);
        }
    }

    public static Map<String, Map<String, Object>> templatedLinkToJsonHome(final TemplatedLink resourceLink,
                                                                           final JsonHomeMediaType mediaType) {
        final Map<String,String> jsonHrefVars = new LinkedHashMap<String, String>();
        for (final HrefVar hrefVar : resourceLink.getHrefVars()) {
            jsonHrefVars.put(hrefVar.getVar(), hrefVar.getVarType().toString());
        }

        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("href-template", resourceLink.getHrefTemplate());
        map.put("href-vars", jsonHrefVars);
        map.put("hints", HintsConverter.toRepresentation(resourceLink.getHints(), mediaType));
        return Collections.singletonMap(resourceLink.getLinkRelationType().toString(), map);
    }

    public static Map<String, Map<String, Object>> directLinkToJsonHome(final DirectLink resourceLink,
                                                                        final JsonHomeMediaType mediaType) {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("href", resourceLink.getHref().toString());
        map.put("hints", HintsConverter.toRepresentation(resourceLink.getHints(), mediaType));
        return Collections.singletonMap(resourceLink.getLinkRelationType().toString(), map);
    }

}
