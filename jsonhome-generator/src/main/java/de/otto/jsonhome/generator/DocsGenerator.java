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

    /**
     * Creates a DocsGenerator.
     *
     * @param relationTypeBaseUri base URI of all relation types.
     */
    public DocsGenerator(final URI relationTypeBaseUri) {
        this.relationTypeBaseUri = relationTypeBaseUri;
    }

    /**
     * Returns the Documentation for a single link-relation type supported by the controller.
     * <p/>
     * This implementation is parsing the {@link Doc} and {@link Docs} annotations of the controller.
     *
     * @param relationType URI of the link-relation type.
     * @param controller the controller, possibly annotated with Docs or Doc.
     * @return Documentation, possibly empty but never null.
     */
    public Documentation documentationFrom(final URI relationType, final Class<?> controller) {
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

    /**
     * Returns the documentation of a single href parameter.
     * <p/>
     * This implementation is using the optional {@link Doc} annotation of the parameter to get the documentation.
     *
     * @param parameterInfo information about the href-var parameter.
     * @return Documentation, possibly empty but never null.
     */
    public Documentation documentationFor(final ParameterInfo parameterInfo) {
        if (parameterInfo.hasAnnotation(Doc.class)) {
            Doc doc = parameterInfo.getAnnotation(Doc.class);
            return documentationFrom(doc);
        } else {
            return emptyDocs();
        }
    }

    /**
     * Creates a Documentation instance from a Doc annotation.
     * @param doc the annotation.
     * @return Documentation, possibly empty but never null.
     */
    private Documentation documentationFrom(final Doc doc) {
        final String link = doc.link();
        final String[] description = doc.value();
        return documentation(
                description != null ? asList(description) : Collections.<String>emptyList(),
                link != null && !link.isEmpty() ? URI.create(link) : null);
    }

    /**
     * Constructs an absolute URI.
     * <p/>
     * If the link is already absolute, URI.create(link) is returned. Otherwise, the {@link #relationTypeBaseUri}
     * is used to create the URI.
     * @param link absolute or relative relation-type URI.
     * @return absolute URI, uniquely identifying a link-relation type.
     */
    private URI linkRelationTypeOf(final String link) {
        return create(link.startsWith("http://")
                ? link
                : relationTypeBaseUri + link);
    }

}
