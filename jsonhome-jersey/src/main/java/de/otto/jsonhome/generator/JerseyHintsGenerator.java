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

import de.otto.jsonhome.model.Allow;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
public final class JerseyHintsGenerator extends HintsGenerator {

    public JerseyHintsGenerator(final URI relationTypeBaseUri, final String docRootDir) {
        super(relationTypeBaseUri, docRootDir);
    }

    @Override
    protected Set<Allow> allowedHttpMethodsOf(final Method method) {
        final Annotation[] annotations = method.getDeclaredAnnotations();
        final Set<Allow> allows = EnumSet.noneOf(Allow.class);
        for (final Annotation annotation : annotations) {
            if (HttpMethods.isHttpMethod(annotation)) {
                allows.add(Allow.valueOf(annotation.annotationType().getSimpleName()));
            }
        }
        return allows;
    }

    @Override
    protected List<String> producedRepresentationsOf(final Method method) {
        final Produces produces = method.getAnnotation(Produces.class);
        return produces == null
                ? Collections.<String>emptyList()
                : Arrays.asList(produces.value());
    }

    @Override
    protected List<String> consumedRepresentationsOf(final Method method) {
        final Consumes consumes = method.getAnnotation(Consumes.class);
        return consumes == null
                ? Collections.<String>emptyList()
                : Arrays.asList(consumes.value());
    }

}
