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

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;

import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.converter.ResourceLinkConverter.resourceLinkToJsonHome;
import static java.util.Collections.singletonMap;

/**
 * @author Guido Steinacker
 * @since 14.10.12
 */
public final class JsonHomeConverter {

    private JsonHomeConverter() {}

    public static Map<String, Map<String,Object>> toJsonHome(final JsonHome jsonHome) {
        final Map<String, Object> jsonResources = new HashMap<String, Object>();
        for (final ResourceLink resource : jsonHome.getResources().values()) {
            jsonResources.putAll(resourceLinkToJsonHome(resource));
        }
        return singletonMap(
                "resources",
                jsonResources
        );

    }
}
