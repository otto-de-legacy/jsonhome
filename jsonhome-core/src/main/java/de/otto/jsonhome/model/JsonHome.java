package de.otto.jsonhome.model;

import java.net.URI;
import java.util.*;

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableList;

/**
 * Immutable container of {@link ResourceLink resource links}, representing a json-home document.
 *
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02">http://tools.ietf.org/html/draft-nottingham-json-home-02</a>
 * @author Guido Steinacker
 * @since 15.09.12
 */
public final class JsonHome {

    private final List<ResourceLink> resources;

    private JsonHome(final List<ResourceLink> resources) {
        this.resources = unmodifiableList(new ArrayList<ResourceLink>(resources));
    }

    public static JsonHome emptyJsonHome() {
        return new JsonHome(Collections.<ResourceLink>emptyList());

    }

    public static JsonHome jsonHome(final List<ResourceLink> resources) {
        return new JsonHome(resources);
    }

    public List<ResourceLink> getResources() {
        return resources;
    }

    public boolean hasResourceFor(final URI relationTypeURI) {
        return getResourceFor(relationTypeURI) != null;
    }

    public ResourceLink getResourceFor(URI relationTypeURI) {
        for (final ResourceLink resource : resources) {
            if (resource.getLinkRelationType().equals(relationTypeURI)) {
                return resource;
            }
        }
        return null;
    }

    public Map<String, ?> toJson() {
        final Map<String, Map<String, ?>> jsonResources = new HashMap<String, Map<String, ?>>();
        for (final ResourceLink resource : this.resources) {
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
                "resources=" + resources +
                '}';
    }
}
