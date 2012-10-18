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

import de.otto.jsonhome.annotation.Precondition;
import de.otto.jsonhome.annotation.Status;
import de.otto.jsonhome.model.Allow;
import de.otto.jsonhome.model.Hints;
import de.otto.jsonhome.model.HintsBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static de.otto.jsonhome.generator.DocsGenerator.documentationFrom;
import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.HintsBuilder.hints;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 * @author Guido Steinacker
 * @since 18.10.12
 */
public abstract class AbstractHintsGenerator {

    public final Hints hintsOf(final Method method) {
        final Set<Allow> allows = allowedHttpMethodsOf(method);
        final HintsBuilder hintsBuilder = hints()
                .allowing(allows)
                .with(documentationFrom(method.getDeclaringClass(), method))
                .requiring(preconditionsFrom(method))
                .withStatus(statusFrom(method));
        final List<String> supportedRepresentations = supportedRepresentationsOf(method);
        if (allows.contains(PUT)) {
            hintsBuilder.acceptingForPut(supportedRepresentations);
        }
        if (allows.contains(POST)) {
            hintsBuilder.acceptingForPost(supportedRepresentations);
        }
        if (allows.contains(GET) || allows.contains(HEAD)) {
            hintsBuilder.representedAs(supportedRepresentations);
        }
        // TODO: PATCH

        return hintsBuilder.build();
    }

    protected List<Precondition> preconditionsFrom(final Method method) {
        final de.otto.jsonhome.annotation.Hints annotation = method.getAnnotation(de.otto.jsonhome.annotation.Hints.class);
        if (annotation != null && annotation.preconditionReq() != null) {
            return asList(annotation.preconditionReq());
        } else {
            return emptyList();
        }
    }

    protected Status statusFrom(final Method method) {
        final de.otto.jsonhome.annotation.Hints annotation = method.getAnnotation(de.otto.jsonhome.annotation.Hints.class);
        if (annotation != null && annotation.status() != null) {
            return annotation.status();
        } else {
            return Status.OK;
        }
    }

    /**
     * Analyses the method with a RequestMapping and returns a list of supported representations.
     * <p/>
     * If the RequestMapping does not specify the produced or consumed representations,
     * "text/html" is returned in a singleton list.
     * <p/>
     * TODO: in case of a POST, text/html is not correct.
     *
     * @return list of allowed HTTP methods.
     * @throws NullPointerException if method is not annotated with @RequestMapping.
     */
    protected List<String> supportedRepresentationsOf(final Method method) {
        final LinkedHashSet<String> representations = new LinkedHashSet<String>();
        representations.addAll(producedRepresentationsOf(method));
        final List<String> consumes = consumedRepresentationsOf(method);
        if (consumes.size() > 0) {
            // preserve order from methodRequestMapping:
            for (final String consumesRepresentation : consumes) {
                if (!representations.contains(consumesRepresentation)) {
                    representations.add(consumesRepresentation);
                }
            }
        } else {
            if (allowedHttpMethodsOf(method).equals(singleton(POST))) {
                representations.add("application/x-www-form-urlencoded");
            }
        }
        // default is HTTP GET
        if (representations.isEmpty()) {
            representations.add("text/html");
        }
        return new ArrayList<String>(representations);
    }

    protected abstract Set<Allow> allowedHttpMethodsOf(Method method);

    protected abstract List<String> producedRepresentationsOf(Method method);

    protected abstract List<String> consumedRepresentationsOf(Method method);
}
