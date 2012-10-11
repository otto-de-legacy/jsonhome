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

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.model.Docs;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;

import static de.otto.jsonhome.model.Docs.documentation;
import static de.otto.jsonhome.model.Docs.emptyDocumentation;
import static java.util.Arrays.asList;

/**
 * @author Guido Steinacker
 * @since 11.10.12
 */
public class DocumentationGenerator {

    private DocumentationGenerator() {
    }

    public static Docs documentationFrom(final Class<?> controller, final Method method) {
        Doc doc = method.getAnnotation(Doc.class);
        if (doc == null) {
            doc = controller.getAnnotation(Doc.class);
        }

        if (doc != null) {
            return documentationFrom(doc);
        } else {
            return emptyDocumentation();
        }
    }

    public static Docs documentationFor(final ParameterInfo parameterInfo) {
        if (parameterInfo.hasAnnotation(Doc.class)) {
            Doc doc = parameterInfo.getAnnotation(Doc.class);
            return documentationFrom(doc);
        } else {
            return emptyDocumentation();
        }
    }

    private static Docs documentationFrom(final Doc doc) {
        final String link = doc.link();
        final String[] description = doc.value();
        return documentation(
                description != null ? asList(description) : Collections.<String>emptyList(),
                link != null && !link.isEmpty() ? URI.create(link) : null);
    }

}
