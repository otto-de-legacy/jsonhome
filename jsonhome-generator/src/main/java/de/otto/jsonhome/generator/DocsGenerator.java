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
import de.otto.jsonhome.annotation.Docs;
import de.otto.jsonhome.model.Documentation;

import java.net.URI;
import java.util.Collections;

import static de.otto.jsonhome.model.Documentation.documentation;
import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static java.net.URI.create;
import static java.util.Arrays.asList;

/**
 * A generator used to create {@link Documentation} for a link-relation type or href-var.
 *
 * @author Guido Steinacker
 * @since 11.10.12
 */
public class DocsGenerator {

    private final URI relationTypeBaseUri;

    public DocsGenerator(final URI relationTypeBaseUri) {
        this.relationTypeBaseUri = relationTypeBaseUri;
    }

    public Documentation documentationFrom(final URI relationType, final Class<?> controller) {

        // TODO: simplify!
        Docs docs = controller.getAnnotation(Docs.class);
        if (docs != null) {
            for (final Doc relDoc : docs.value()) {
                if (linkRelationTypeOf(relDoc.rel()).equals(relationType)) {
                    return documentationFrom(relDoc);
                }
            }
        } else {
            Doc relDoc = controller.getAnnotation(Doc.class);
            if (relDoc != null && linkRelationTypeOf(relDoc.rel()).equals(relationType)) {
                return documentationFrom(relDoc);
            }
        }
        return emptyDocs();
    }

    public Documentation documentationFor(final ParameterInfo parameterInfo) {
        if (parameterInfo.hasAnnotation(Doc.class)) {
            Doc doc = parameterInfo.getAnnotation(Doc.class);
            return documentationFrom(doc);
        } else {
            return emptyDocs();
        }
    }

    private Documentation documentationFrom(final Doc doc) {
        final String link = doc.link();
        final String[] description = doc.value();
        return documentation(
                description != null ? asList(description) : Collections.<String>emptyList(),
                link != null && !link.isEmpty() ? URI.create(link) : null);
    }

    private URI linkRelationTypeOf(final String link) {
        return create(link.startsWith("http://")
                ? link
                : relationTypeBaseUri + link);
    }

}
