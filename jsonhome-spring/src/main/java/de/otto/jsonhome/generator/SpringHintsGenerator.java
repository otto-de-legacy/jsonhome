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

import de.otto.jsonhome.model.Allow;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static de.otto.jsonhome.model.Allow.GET;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;

public class SpringHintsGenerator extends HintsGenerator {

    public SpringHintsGenerator(final URI relationTypeBaseUri) {
        super(relationTypeBaseUri);
    }

    /**
     * Analyses the method with a RequestMapping and returns a list of allowed http methods (GET, PUT, etc.).
     * <p/>
     * If the RequestMapping does not specify the allowed HTTP methods, "GET" is returned in a singleton list.
     *
     * @return list of allowed HTTP methods.
     * @throws NullPointerException if method is not annotated with @RequestMapping.
     */
    @Override
    protected Set<Allow> allowedHttpMethodsOf(final Method method) {
        final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        final Set<Allow> allows = EnumSet.noneOf(Allow.class);
        for (Object o : methodRequestMapping.method()) {
            allows.add(Allow.valueOf(o.toString()));
        }
        if (allows.isEmpty()) {
            return of(GET);
        } else {
            return allows;
        }
    }

    @Override
    protected List<String> producedRepresentationsOf(final Method method) {
        final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        final String[] produces = methodRequestMapping.produces();
        return produces != null ? asList(produces) : Collections.<String>emptyList();

    }

    @Override
    protected List<String> consumedRepresentationsOf(final Method method) {
        final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        final String[] consumes = methodRequestMapping.consumes();
        return consumes != null ? asList(consumes) : Collections.<String>emptyList();

    }
}