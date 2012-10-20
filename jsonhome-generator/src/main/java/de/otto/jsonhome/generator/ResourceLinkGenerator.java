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

package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.Hints;
import de.otto.jsonhome.model.ResourceLink;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static de.otto.jsonhome.generator.MethodHelper.getParameterInfos;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;

/**
 * @author Guido Steinacker
 * @since 17.10.12
 */
public abstract class ResourceLinkGenerator {

    private final URI relationTypeBaseUri;
    private final HintsGenerator hintsGenerator;
    private final HrefVarsGenerator hrefVarsGenerator;

    protected ResourceLinkGenerator(final URI relationTypeBaseUri,
                                    final HintsGenerator hintsGenerator,
                                    final HrefVarsGenerator hrefVarsGenerator) {
        this.relationTypeBaseUri = relationTypeBaseUri;
        this.hintsGenerator = hintsGenerator;
        this.hrefVarsGenerator = hrefVarsGenerator;
    }

    /**
     * Analyses the a method of a controller (having a RequestMapping) and returns the list of ResourceLinks of this method.
     *
     * @param method the method
     * @return list of resource links.
     */
    public List<ResourceLink> resourceLinksFor(final Method method) {
        final List<ResourceLink> resourceLinks = new ArrayList<ResourceLink>();
        if (isCandidateForAnalysis(method)) {
            final List<String> resourcePaths = resourcePathsFor(method);
            for (final String resourcePath : resourcePaths) {
                final URI relationType = relationTypeFrom(method);
                if (relationType != null) {
                    final Hints hints = hintsGenerator.hintsOf(method);
                    if (resourcePath.matches(".*\\{.*\\}")) {
                        resourceLinks.add(templatedLink(
                                relationType,
                                resourcePath,
                                hrefVarsGenerator.hrefVarsFor(relationType, method, hints),
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
        return resourceLinks;
    }


    /**
     * Parses the method parameter annotations, looking for parameters annotated as @RequestParam, and returns the
     * query part of a href-template.
     *
     * @param method method, optionally having parameters annotated as being a @RequestParam.
     * @return query part of a href-template, like {?param1,param2,param3}
     */
    protected String queryTemplateFrom(final Method method) {
        final StringBuilder sb = new StringBuilder();
        final List<ParameterInfo> parameterInfos = getParameterInfos(method);
        boolean first = true;
        for (final ParameterInfo parameterInfo : parameterInfos) {
            if (hrefVarsGenerator.hasRequestParam(parameterInfo)) {
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
     * Analyses a method of a controller and returns the fully qualified URI of the link-relation type.
     *
     * If the neither the method, nor the controller is annotated with Rel, null is returned.
     *
     * The Rel of the method is overriding the Rel of the Controller.
     *
     * @param method the method
     * @return URI of the link-relation type, or null
     */
    protected URI relationTypeFrom(final Method method) {
        final Rel controllerRel = method.getDeclaringClass().getAnnotation(Rel.class);
        final Rel methodRel = method.getAnnotation(Rel.class);
        if (controllerRel == null && methodRel == null) {
            return null;
        } else {
            final String linkRelationType = methodRel != null
                    ? methodRel.value()
                    : controllerRel.value();
            return URI.create(linkRelationType.startsWith("http://")
                    ? linkRelationType
                    : relationTypeBaseUri + linkRelationType);
        }
    }

    protected abstract boolean isCandidateForAnalysis(final Method method);

    /**
     * Returns the resource paths for the given method.
     *
     * The resource paths are the paths of the URIs the given method is responsible for.
     * @param method the method of the controller, possibly handling one or more REST resources.
     * @return list of resource paths
     */
    protected abstract List<String> resourcePathsFor(final Method method);
}
