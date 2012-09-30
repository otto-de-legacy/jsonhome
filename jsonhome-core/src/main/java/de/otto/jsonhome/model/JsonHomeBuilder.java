package de.otto.jsonhome.model;

import java.util.ArrayList;
import java.util.List;

public final class JsonHomeBuilder {

    private final List<ResourceLink> resources = new ArrayList<ResourceLink>();

    private JsonHomeBuilder() {
    }

    public static JsonHomeBuilder jsonHomeBuilder() {
        return new JsonHomeBuilder();
    }

    public static JsonHomeBuilder copyFrom(final JsonHome jsonHome) {
        return jsonHomeBuilder().mergeWith(jsonHome);
    }

    public JsonHomeBuilder mergeWith(final JsonHome jsonHome) {
        resources.addAll(jsonHome.getResourceLinks());
        return this;
    }

    public JsonHomeBuilder addResource(final ResourceLink resource) {
        this.resources.add(resource);
        return this;
    }

    public JsonHomeBuilder addResources(final List<ResourceLink> resources) {
        this.resources.addAll(resources);
        return this;
    }

    public JsonHome build() {
        return JsonHome.jsonHome(resources);
    }
}