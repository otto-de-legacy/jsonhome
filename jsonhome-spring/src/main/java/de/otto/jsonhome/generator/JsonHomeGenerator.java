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

import de.otto.jsonhome.model.Hints;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.otto.jsonhome.generator.HrefVarsGenerator.hrefVarsFor;
import static de.otto.jsonhome.generator.MethodHelper.getParameterInfos;
import static de.otto.jsonhome.generator.RelationTypeGenerator.relationTypeFrom;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static de.otto.jsonhome.model.ResourceLinkHelper.mergeResources;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * A generator used to create JsonHome documents from Spring controllers.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */

public class JsonHomeGenerator {

    private final Collection<Class<?>> controllers;
    private URI rootUri;
    private URI relationTypeRootUri;

    public static JsonHomeGenerator jsonHomeFor(final URI rootUri) {
        return new JsonHomeGenerator(rootUri);
    }

    /**
     * Sets the root URI for link-relation types.
     *
     * @param relationTypeRootUri URI
     * @return this
     */
    public JsonHomeGenerator withRelationTypeRoot(final URI relationTypeRootUri) {
        this.relationTypeRootUri = relationTypeRootUri;
        return this;
    }

    protected JsonHomeGenerator(final URI rootUri) {
        if (rootUri == null) {
            throw new NullPointerException("Parameter rootUri must not be null.");
        }
        this.rootUri = rootUri;
        this.relationTypeRootUri = rootUri;
        this.controllers = new ArrayList<Class<?>>();
    }

    public JsonHomeGenerator with(final Class<?> controller) {
        if (isSpringController(controller)) {
            this.controllers.add(controller);
        }
        return this;
    }

    public JsonHomeGenerator with(final Collection<Class<?>> controllers) {
        for (final Class<?> controller : controllers) {
            if (isSpringController(controller)) {
                this.controllers.add(controller);
            }
        }
        return this;
    }

    public JsonHome get() {
        List<ResourceLink> resources = new ArrayList<ResourceLink>();
        for (final Class<?> controllerClass : controllers) {
            resources = mergeResources(resources, resourceLinksFor(controllerClass));
        }
        return jsonHome(resources);
    }

    protected List<ResourceLink> resourceLinksFor(final Class<?> controller) {
        List<ResourceLink> resourceLinks = emptyList();
        for (final Method method : controller.getMethods()) {
            resourceLinks = mergeResources(resourceLinks, resourceLinksForMethod(controller, method));
        }
        return resourceLinks;
    }

    /**
     * Parses the method parameter annotations, looking for parameters annotated as @RequestParam, and returns the
     * query part of a href-template.
     *
     * @param method method, optionally having parameters annotated as being a @RequestParam.
     * @return query part of a href-template, like {?param1,param2,param3}
     */
    public static String queryTemplateFrom(final Method method) {
        final StringBuilder sb = new StringBuilder();
        final List<ParameterInfo> parameterInfos = getParameterInfos(method);
        boolean first = true;
        for (final ParameterInfo parameterInfo : parameterInfos) {
            if (parameterInfo.hasAnnotation(RequestParam.class)) {
                if (first) {
                    first = false;
                    sb.append("{?").append(parameterInfo.getName());
                } else {
                    sb.append(",").append(parameterInfo.getName());
                }
            }
        }
        final String s = sb.toString();
        return s.isEmpty() ? s : s + "}";
    }

    /**
     * Analyses the a method of a controller (having a RequestMapping) and returns the list of ResourceLinks of this method.
     *
     * @param controller the controller of the method.
     * @param method the method
     * @return list of resource links.
     */
    protected List<ResourceLink> resourceLinksForMethod(final Class<?> controller,
                                                        final Method method) {
        final List<String> parentResourcePaths = parentResourcePathsFrom(controller);
        final List<ResourceLink> resourceLinks = new ArrayList<ResourceLink>();
        final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        if (methodRequestMapping != null) {
            for (String resourcePathPrefix : parentResourcePaths) {
                final String[] resourcePathSuffixes = methodRequestMapping.value().length > 0
                        ? methodRequestMapping.value()
                        : new String[] {""};
                for (String resourcePathSuffix : resourcePathSuffixes) {
                    final String resourcePath = rootUri + resourcePathPrefix + resourcePathSuffix + queryTemplateFrom(method);
                    final URI relationType = relationTypeFrom(relationTypeRootUri, controller, method);
                    if (relationType != null) {
                        final Hints hints = HintsGenerator.hintsOf(controller, method);
                        if (resourcePath.matches(".*\\{.*\\}")) {
                            resourceLinks.add(templatedLink(
                                    relationType,
                                    resourcePath,
                                    hrefVarsFor(relationType, method, hints),
                                    hints
                            ));
                        } else {
                            resourceLinks.add(directLink(
                                    relationType,
                                    URI.create(resourcePath),
                                    hints
                            ));
                        }
                    }
                }
            }
        }
        return resourceLinks;
    }

    /**
     * Analyses the controller (possibly annotated with RequestMapping) and returns the list of resource paths defined by the mapping.
     *
     * @param controller the controller.
     * @return list of resource paths.
     */
    protected List<String> parentResourcePathsFrom(final Class<?> controller) {
        final RequestMapping controllerRequestMapping = controller.getAnnotation(RequestMapping.class);
        final List<String> resourcePathPrefixes;
        if (controllerRequestMapping != null) {
            resourcePathPrefixes = asList(controllerRequestMapping.value());
        } else {
            resourcePathPrefixes = asList("");
        }
        return resourcePathPrefixes;
    }


    private boolean isSpringController(final Class<?> controller) {
        return controller.getAnnotation(Controller.class) != null;
    }

}
