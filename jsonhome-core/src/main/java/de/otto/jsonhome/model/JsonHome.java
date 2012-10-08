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

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

/**
 * Immutable container of {@link ResourceLink resource links}, representing a json-home document.
 *
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02">http://tools.ietf.org/html/draft-nottingham-json-home-02</a>
 * @author Guido Steinacker
 * @since 15.09.12
 */
public final class JsonHome {

    private final Map<URI, ResourceLink> resources;

    private JsonHome(final Map<URI, ResourceLink> resources) {
        this.resources = unmodifiableMap(new HashMap<URI, ResourceLink>(resources));
    }

    public static JsonHome emptyJsonHome() {
        return new JsonHome(Collections.<URI, ResourceLink>emptyMap());

    }

    /**
     * Creates a JsonHome document from a collection of ResourceLinks. The link-relation types of the resources
     * must be unique, otherwise an IllegalArgumentException is thrown.
     *
     * @param resources collection of resource links.
     * @return JsonHome
     */
    public static JsonHome jsonHome(final Collection<ResourceLink> resources) {
        final Map<URI, ResourceLink> resourceMap = new HashMap<URI, ResourceLink>(resources.size());
        for (final ResourceLink resource : resources) {
            if (resourceMap.containsKey(resource.getLinkRelationType())) {
                throw new IllegalArgumentException("Unable to construct JsonHome. Link-relation types must be unique.");
            }
            resourceMap.put(resource.getLinkRelationType(), resource);
        }
        return new JsonHome(resourceMap);
    }

    /**
     * Returns an unmodifiable map containing the resources of this json-home document.
     *
     * @return mapping of link-relation types to resource links.
     */
    public Map<URI, ResourceLink> getResources() {
        return resources;
    }

    public boolean hasResourceFor(final URI relationTypeURI) {
        return resources.containsKey(relationTypeURI);
    }

    public ResourceLink getResourceFor(final URI relationTypeURI) {
        return resources.get(relationTypeURI);
    }

    public Map<String, ?> toJson() {
        final Map<String, Map<String, ?>> jsonResources = new HashMap<String, Map<String, ?>>();
        for (final ResourceLink resource : this.resources.values()) {
            jsonResources.put(resource.getLinkRelationType().toString(), resource.toJson());
        }
        return singletonMap(
                "resources",
                jsonResources
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonHome jsonHome = (JsonHome) o;

        if (resources != null ? !resources.equals(jsonHome.resources) : jsonHome.resources != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return resources != null ? resources.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "JsonHome{" +
                "resources=" + resources.values() +
                '}';
    }
}
