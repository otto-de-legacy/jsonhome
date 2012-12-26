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

import de.otto.jsonhome.annotation.Href;
import de.otto.jsonhome.annotation.HrefTemplate;
import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.Hints;
import de.otto.jsonhome.model.ResourceLink;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

import static de.otto.jsonhome.generator.MethodHelper.getParameterInfos;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;
import static java.net.URI.create;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * A generator used to generate the {@link ResourceLink resource links} of a method.
 *
 * @author Guido Steinacker
 * @since 17.10.12
 */
public abstract class ResourceLinkGenerator {

    private final URI applicationBaseUri;
    private final URI relationTypeBaseUri;
    private final HintsGenerator hintsGenerator;
    private final HrefVarsGenerator hrefVarsGenerator;

    protected ResourceLinkGenerator(final URI applicationBaseUri,
                                    final URI relationTypeBaseUri,
                                    final HintsGenerator hintsGenerator,
                                    final HrefVarsGenerator hrefVarsGenerator) {
        this.applicationBaseUri = applicationBaseUri;
        this.relationTypeBaseUri = relationTypeBaseUri;
        this.hintsGenerator = hintsGenerator;
        this.hrefVarsGenerator = hrefVarsGenerator;
    }

    /**
     * Analyses the a method of a controller and returns the list of ResourceLinks of this method.
     *
     * @param method the method
     * @return resource link or null.
     */
    public ResourceLink resourceLinkFor(final Method method) {
        ResourceLink resourceLink = null;
        if (isCandidateForAnalysis(method)) {
            final String resourcePath = overriddenOrCalculatedResourcePathFor(method);
            final URI relationType = relationTypeFrom(method);
            if (relationType != null) {
                final Hints hints = hintsGenerator.hintsOf(relationType, method);
                if (resourcePath.matches(".*\\{.*\\}")) {
                    resourceLink = templatedLink(
                            relationType,
                            resourcePath,
                            hrefVarsGenerator.hrefVarsFor(relationType, method),
                            hints
                    );
                } else {
                    resourceLink = directLink(
                            relationType,
                            create(resourcePath),
                            hints
                    );
                }
            }
        }
        return resourceLink;
    }

    /**
     * Calculates the resource path (direct links or templated links) of the method.
     *
     * @param method the Method
     * @return resource path or null.
     */
    protected String overriddenOrCalculatedResourcePathFor(final Method method) {
        final Href methodHrefAnnotation = findAnnotation(method, Href.class);
        if (methodHrefAnnotation != null) {
            return applicationBaseUri.resolve(methodHrefAnnotation.value()).toString();
        } else {
            final HrefTemplate hrefTemplateAnnotation = findAnnotation(method, HrefTemplate.class);
            if (hrefTemplateAnnotation != null) {
                return hrefTemplateAnnotation.value().startsWith("http://")
                        ? hrefTemplateAnnotation.value()
                        : applicationBaseUri.toString() + hrefTemplateAnnotation.value();
            } else {
                return resourcePathFor(method);
            }
        }
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
        final Rel controllerRel = findAnnotation(method.getDeclaringClass(), Rel.class);
        final Rel methodRel = findAnnotation(method, Rel.class);
        if (controllerRel == null && methodRel == null) {
            return null;
        } else {
            final String linkRelationType = methodRel != null
                    ? methodRel.value()
                    : controllerRel.value();
            return create(linkRelationType.startsWith("http://")
                    ? linkRelationType
                    : relationTypeBaseUri + linkRelationType);
        }
    }

    /**
     * Returns true if the method is a candidate for further processing, false otherwise.
     *
     * @param method the method to check
     * @return boolean
     */
    protected abstract boolean isCandidateForAnalysis(final Method method);

    /**
     * Returns the resource path for the given method.
     * <p/>
     * If the method is able to handle multiple URIs, the implementation should select the first or
     * best URI.
     *
     * @param method the method of the controller, possibly handling one or more REST resources.
     * @return list of resource paths
     */
    protected abstract String resourcePathFor(final Method method);
}
