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

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.net.URI;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * A Spring-based ResourceLinkGenerator.
 *
 * Used to analyse Spring MVC controllers.
 *
 * @author Guido Steinacker
 * @since 17.10.12
 */
public class SpringResourceLinkGenerator extends ResourceLinkGenerator {

    private final URI applicationBaseUri;

    public SpringResourceLinkGenerator(final URI applicationBaseUri, final URI relationTypeBaseUri) {
        super(
                applicationBaseUri,
                relationTypeBaseUri,
                new SpringHintsGenerator(relationTypeBaseUri),
                new SpringHrefVarsGenerator(relationTypeBaseUri)
        );
        this.applicationBaseUri = applicationBaseUri;
    }

    /**
     * {@inheritDoc}
     *
     * A method is a candidate if there is a RequestMapping annotation.
     *
     * @param method the current method of the controller.
     * @return true if method should be analysed
     */
    @Override
    public boolean isCandidateForAnalysis(final Method method) {
        return AnnotationUtils.findAnnotation(method, RequestMapping.class) != null;
    }

    @Override
    protected String resourcePathFor(final Method method) {
        if (isCandidateForAnalysis(method)) {
            final RequestMapping methodRequestMapping = findAnnotation(method, RequestMapping.class);
            final String resourcePathPrefix = parentResourcePathsFrom(method.getDeclaringClass());
            final String resourcePathSuffix = methodRequestMapping.value().length > 0
                    ? methodRequestMapping.value()[0]
                    : "";
            final String resourcePath;
            if (resourcePathPrefix.endsWith("/") && resourcePathSuffix.startsWith("/")) {
                resourcePath = resourcePathPrefix + resourcePathSuffix.substring(1);
            } else {
                resourcePath = resourcePathPrefix + resourcePathSuffix;
            }
            return applicationBaseUri + resourcePath + queryTemplateFrom(method);
        } else {
            return null;
        }
    }

    /**
     * Analyses the controller (possibly annotated with RequestMapping) and returns the list of resource paths defined by the mapping.
     *
     * @param controller the controller.
     * @return list of resource paths.
     */
    protected String parentResourcePathsFrom(final Class<?> controller) {
        final RequestMapping controllerRequestMapping = findAnnotation(controller, RequestMapping.class);
        final String firstResourcePathPrefix;
        if (controllerRequestMapping != null && controllerRequestMapping.value().length > 0) {
            firstResourcePathPrefix = controllerRequestMapping.value()[0];
        } else {
            firstResourcePathPrefix = "";
        }
        return firstResourcePathPrefix;
    }

}
