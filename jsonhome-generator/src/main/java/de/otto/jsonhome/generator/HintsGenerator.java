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

import de.otto.jsonhome.annotation.Auth;
import de.otto.jsonhome.model.*;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * Abstract generator used to generate {@link Hints} for a resource.
 *
 * @author Guido Steinacker
 * @since 18.10.12
 */
public abstract class HintsGenerator {

    private final DocsGenerator docsGenerator;

    /**
     * Creates a HintsGenerator.
     *
     * @param relationTypeBaseUri the base URI used to create absolute relation-type URIs.
     * @param docRootDir the root classpath directory containing Markdown documents. May be null.
     */
    protected HintsGenerator(final URI relationTypeBaseUri, final String docRootDir) {
        this.docsGenerator = new DocsGenerator(relationTypeBaseUri, docRootDir);
    }

    /**
     * Analyses the method with a RequestMapping and returns the corresponding Hints.
     *
     * If the RequestMapping does not specify the produced or consumed representations,
     * "text/html" is returned in a singleton list. In case of a POST method, the default representation
     * is "application/x-www-form-urlencoded".
     *
     *
     * @param relationType relation type
     * @param method method
     * @return Hints.
     * @throws NullPointerException if method is not annotated with @RequestMapping.
     */

    public final Hints hintsOf(final URI relationType, final Method method) {
        final Set<Allow> allows = allowedHttpMethodsOf(method);
        final HintsBuilder hintsBuilder = hintsBuilder()
                .allowing(allows)
                .with(docsGenerator.documentationFrom(relationType, method.getDeclaringClass()))
                .acceptingRanges(acceptedRangesFrom(method))
                .preferring(preferencesFrom(method))
                .requiring(preconditionsFrom(method))
                .withAuthRequired(requiredAuthenticationFrom(method))
                .withStatus(statusFrom(method));

        final List<String> produced = producedRepresentationsOf(method);
        final List<String> consumed = consumedRepresentationsOf(method);
        if (allows.contains(PUT)) {
            hintsBuilder.acceptingForPut(consumed);
            hintsBuilder.representedAs(produced);
        } else if (allows.contains(PATCH)) {
            hintsBuilder.acceptingForPatch(consumed);
            hintsBuilder.representedAs(produced);
        } else if (allows.contains(POST)) {
            hintsBuilder.acceptingForPost(consumed.isEmpty()
                    ? asList("application/x-www-form-urlencoded")
                    : consumed
            );
            hintsBuilder.representedAs(produced);
        } else if (allows.contains(GET) || allows.contains(HEAD)) {
            final List<String> representations = join(produced, consumed);
            hintsBuilder.representedAs(representations.isEmpty()
                    ? asList("text/html")
                    : representations
            );
        } else {
            hintsBuilder.representedAs(join(produced, consumed));
        }
        return hintsBuilder.build();
    }

    /**
     * Analyses the method and returns the list of accepted ranges supported by the method.
     *
     * This implementation is using the {@link de.otto.jsonhome.annotation.Hints} annotation to determine the
     * accepted ranges.
     *
     * @param method Method responsible for handling an HTTP request.
     * @return List of Strings, or an empty list.
     */
    protected List<String> acceptedRangesFrom(final Method method) {
        final de.otto.jsonhome.annotation.Hints annotation = findAnnotation(method, de.otto.jsonhome.annotation.Hints.class);
        if (annotation != null && annotation.acceptRanges() != null) {
            return asList(annotation.acceptRanges());
        } else {
            return emptyList();
        }
    }

    /**
     * Analyses the method and returns the list of preferences of the method.
     *
     * This implementation is using the {@link de.otto.jsonhome.annotation.Hints} annotation to determine the
     * preferences.
     *
     * @param method Method responsible for handling an HTTP request.
     * @return List of Strings, or an empty list.
     */
    protected List<String> preferencesFrom(final Method method) {
        final de.otto.jsonhome.annotation.Hints annotation = findAnnotation(method, de.otto.jsonhome.annotation.Hints.class);
        if (annotation != null && annotation.prefer() != null) {
            return asList(annotation.prefer());
        } else {
            return emptyList();
        }
    }

    /**
     * Analyses the method and returns the list of preconditions expected by the method.
     *
     * This implementation is using the {@link de.otto.jsonhome.annotation.Hints} annotation to determine the
     * expected preconditions.
     *
     * @param method Method responsible for handling an HTTP request.
     * @return List of Preconditions, or an empty list.
     */
    protected List<Precondition> preconditionsFrom(final Method method) {
        final de.otto.jsonhome.annotation.Hints annotation = findAnnotation(method, de.otto.jsonhome.annotation.Hints.class);
        if (annotation != null && annotation.preconditionReq() != null) {
            return asList(annotation.preconditionReq());
        } else {
            return emptyList();
        }
    }

    /**
     * Analyses the method and returns the list of required HTTP authentication methods expected by the method.
     *
     * This implementation is using the {@link de.otto.jsonhome.annotation.Hints} annotation to determine the
     * expected authentications.
     *
     * @param method Method responsible for handling an HTTP request.
     * @return List of Authentication, or an empty list.
     */
    protected List<Authentication> requiredAuthenticationFrom(final Method method) {
        final de.otto.jsonhome.annotation.Hints annotation = findAnnotation(method, de.otto.jsonhome.annotation.Hints.class);
        if (annotation != null && annotation.authReq() != null && annotation.authReq().length > 0) {
            final List<Authentication> authReq = new ArrayList<>();
            for (final Auth auth : annotation.authReq()) {
                authReq.add(Authentication.authReq(
                        auth.scheme(),
                        asList(auth.realms())
                ));
            }
            return authReq;
        } else {
            return emptyList();
        }
    }

    /**
     * Analyses the method and returns the Status of the resource handled by the method.
     *
     * @param method Method responsible for handling an HTTP request.
     * @return Status (ok, deprecated or gone)
     */
    protected Status statusFrom(final Method method) {
        final de.otto.jsonhome.annotation.Hints annotation = findAnnotation(method, de.otto.jsonhome.annotation.Hints.class);
        if (annotation != null && annotation.status() != null) {
            return annotation.status();
        } else {
            return Status.OK;
        }
    }

    private List<String> join(final List<String> list, final List<String> other) {
        final List<String> result = new ArrayList<>(list);
        for (final String s : other) {
            if (!result.contains(s)) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Returns the Set of allowed HTTP methods of the given method.
     *
     * @param method the method
     * @return set of allowed HTTP methods.
     * @throws NullPointerException if method is not annotated with @RequestMapping.
     */
    protected abstract Set<Allow> allowedHttpMethodsOf(Method method);

    /**
     * Returns the produced representations of the method, or an empty list if no representations are produced.
     *
     * @param method the Method of a controller, possibly producing the representation of a REST resource.
     * @return list of produced representations.
     */
    protected abstract List<String> producedRepresentationsOf(Method method);

    /**
     * Returns the consumed (accepted) representations of the method, or an empty list if no representations are consumed.
     *
     * @param method the Method of a controller, possibly consuming a representation of a REST resource.
     * @return list of consumed representations.
     */
    protected abstract List<String> consumedRepresentationsOf(Method method);
}
