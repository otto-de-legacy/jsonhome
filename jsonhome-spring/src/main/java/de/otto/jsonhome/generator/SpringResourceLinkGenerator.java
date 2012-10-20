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

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

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
        super(relationTypeBaseUri, new SpringHintsGenerator(), new SpringHrefVarsGenerator());
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
        return method.getAnnotation(RequestMapping.class) != null;
    }

    @Override
    protected List<String> resourcePathsFor(final Method method) {
        final List<String> resourcePaths = new ArrayList<String>();
        if (isCandidateForAnalysis(method)) {
            final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
            for (final String resourcePathPrefix : parentResourcePathsFrom(method.getDeclaringClass())) {
                final String[] resourcePathSuffixes = methodRequestMapping.value().length > 0
                        ? methodRequestMapping.value()
                        : new String[] {""};

                for (String resourcePathSuffix : resourcePathSuffixes) {
                    resourcePaths.add(applicationBaseUri + resourcePathPrefix + resourcePathSuffix + queryTemplateFrom(method));
                }
            }
        }
        return resourcePaths;
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

}
