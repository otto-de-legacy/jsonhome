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
package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static de.otto.jsonhome.model.ResourceLinkHelper.mergeResources;
import static java.util.Collections.emptyList;

/**
 * Abstract base class for JsonHome Generators.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */

public abstract class JsonHomeGenerator {

    private final Collection<Class<?>> controllers = new ArrayList<Class<?>>();
    private ResourceLinkGenerator resourceLinkGenerator;

    /**
     * Injects the generator implementation used to generate ResourceLink instances.
     *
     * @param resourceLinkGenerator implementation of the ResourceLinkGenerator
     */
    protected void setResourceLinkGenerator(final ResourceLinkGenerator resourceLinkGenerator) {
        this.resourceLinkGenerator = resourceLinkGenerator;
    }

    /**
     * Specifies a controller class, possibly providing one or more resource links.
     * <p/>
     * The injected generator is used to find the resource links supported by the controller.
     *
     * @param controller a class that is implementing a HTTP API.
     * @return this
     */
    public final JsonHomeGenerator with(final Class<?> controller) {
        if (isCandidateForAnalysis(controller)) {
            this.controllers.add(controller);
        }
        return this;
    }

    /**
     * Specifies a number of controller classes, possibly providing one or more resource links.
     * <p/>
     * The injected generator is used to find the resource links supported by the controllers.
     *
     * @param controllers classes implementing a HTTP API.
     * @return this
     */
    public final JsonHomeGenerator with(final Collection<Class<?>> controllers) {
        for (final Class<?> controller : controllers) {
            if (isCandidateForAnalysis(controller)) {
                this.controllers.add(controller);
            }
        }
        return this;
    }

    /**
     * Runs the generator and returns the JsonHome instance describing the HTTP API implemented by the controllers.
     * @return JsonHome instance.
     */
    public final JsonHome generate() {
        List<? extends ResourceLink> resources = new ArrayList<ResourceLink>();
        for (final Class<?> controllerClass : controllers) {
            resources = mergeResources(resources, resourceLinksFor(controllerClass));
        }
        return jsonHome(resources);
    }

    /**
     * Returns the ResourceLink instances of the controller.
     * @param controller the controller
     * @return list of ResourceLinks.
     */
    protected final List<? extends ResourceLink> resourceLinksFor(final Class<?> controller) {
        List<? extends ResourceLink> resourceLinks = emptyList();
        for (final Method method : controller.getMethods()) {
            resourceLinks = mergeResources(
                    resourceLinks,
                    resourceLinkGenerator.resourceLinkFor(method));
        }
        return resourceLinks;
    }

    /**
     * Returns true if the controller is a candidate for further processing, false otherwise.
     *
     * @param controller the controller to check
     * @return boolean
     */
    protected abstract boolean isCandidateForAnalysis(final Class<?> controller);

}
