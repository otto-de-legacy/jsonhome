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

    public void setResourceLinkGenerator(final ResourceLinkGenerator resourceLinkGenerator) {
        this.resourceLinkGenerator = resourceLinkGenerator;
    }

    public final JsonHomeGenerator with(final Class<?> controller) {
        if (isCandidateForAnalysis(controller)) {
            this.controllers.add(controller);
        }
        return this;
    }

    public final JsonHomeGenerator with(final Collection<Class<?>> controllers) {
        for (final Class<?> controller : controllers) {
            if (isCandidateForAnalysis(controller)) {
                this.controllers.add(controller);
            }
        }
        return this;
    }

    public final JsonHome generate() {
        List<ResourceLink> resources = new ArrayList<ResourceLink>();
        for (final Class<?> controllerClass : controllers) {
            resources = mergeResources(resources, resourceLinksFor(controllerClass));
        }
        return jsonHome(resources);
    }

    protected final List<ResourceLink> resourceLinksFor(final Class<?> controller) {
        List<ResourceLink> resourceLinks = emptyList();
        for (final Method method : controller.getMethods()) {
            resourceLinks = mergeResources(
                    resourceLinks,
                    resourceLinkGenerator.resourceLinksFor(method));
        }
        return resourceLinks;
    }

    protected abstract boolean isCandidateForAnalysis(final Class<?> controller);

}
