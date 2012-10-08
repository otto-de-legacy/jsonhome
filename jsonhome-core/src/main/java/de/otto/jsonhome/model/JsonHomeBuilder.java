/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.model;

import java.net.URI;
import java.util.*;

/**
 * A builder used to build JsonHome instances.
 */
public final class JsonHomeBuilder {

    private final Map<URI, ResourceLink> resources = new HashMap<URI, ResourceLink>();

    private JsonHomeBuilder() {
    }

    public static JsonHomeBuilder jsonHomeBuilder() {
        return new JsonHomeBuilder();
    }

    public static JsonHomeBuilder copyFrom(final JsonHome jsonHome) {
        return jsonHomeBuilder().mergeWith(jsonHome);
    }

    public JsonHomeBuilder mergeWith(final JsonHome jsonHome) {
        resources.putAll(jsonHome.getResources());
        return this;
    }

    public JsonHomeBuilder addResource(final ResourceLink resource) {
        this.resources.put(resource.getLinkRelationType(), resource);
        return this;
    }

    public JsonHomeBuilder addResources(final Collection<ResourceLink> resources) {
        for (final ResourceLink resource : resources) {
            this.resources.put(resource.getLinkRelationType(), resource);
        }
        return this;
    }

    public JsonHome build() {
        return JsonHome.jsonHome(resources.values());
    }
}