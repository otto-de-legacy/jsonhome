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

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Docs;
import de.otto.jsonhome.model.Documentation;
import org.markdown4j.Markdown4jProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static de.otto.jsonhome.generator.UriBuilder.normalized;
import static de.otto.jsonhome.model.Documentation.documentation;
import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * A generator used to create {@link Documentation} for a link-relation type or href-var.
 *
 * @author Guido Steinacker
 * @since 11.10.12
 */
public class DocsGenerator {

    private final URI relationTypeBaseUri;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private Resource rootDir;

    /**
     * Creates a DocsGenerator.
     *
     * @param relationTypeBaseUri base URI of all relation types.
     * @param docRootDir root of the Markdown documents. May be null if not used.
     */
    public DocsGenerator(final URI relationTypeBaseUri, final String docRootDir) {
        this.rootDir = docRootDir != null ? new ClassPathResource(docRootDir) : null;
        this.relationTypeBaseUri = normalized(relationTypeBaseUri).toUri();
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
        Docs docs = findAnnotation(controller, Docs.class);
        if (docs != null) {
            for (final Doc relDoc : docs.value()) {
                if (!relDoc.rel().isEmpty() && absoluteUriOf(relDoc.rel()).equals(relationType)) {
                    return documentationFrom(relDoc);
                }
            }
        } else {
            Doc relDoc = findAnnotation(controller, Doc.class);
            if (relDoc != null && !relDoc.rel().isEmpty() && absoluteUriOf(relDoc.rel()).equals(relationType)) {
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
        final List<String> values = asList(doc.value());
        final String detailedDescription = (doc.include() != null && !doc.include().isEmpty())
            ? htmlFromMarkdown(doc.include())
            : "";
        return documentation(
                removeEmptyStringsAndNullValuesFrom(values),
                detailedDescription,
                absoluteUriOf(link)
        );
    }

    /**
     * Constructs an absolute URI.
     * <p/>
     * If the link is already absolute, URI.create(link) is returned. Otherwise, the {@link #relationTypeBaseUri}
     * is used to create the URI.
     * @param link absolute or relative relation-type URI.
     * @return absolute URI, uniquely identifying a link-relation type.
     */
    private URI absoluteUriOf(final String link) {
        if (link != null && !link.isEmpty()) {
            if (link.startsWith("http://")) {
                return create(link);
            } else {
                final String baseUri = relationTypeBaseUri.toString();
                if (baseUri.endsWith("/") || link.startsWith("/")) {
                    return create(baseUri + link);
                } else {
                    return create(baseUri + "/" + link);
                }

            }
        } else {
            return null;
        }
    }

    private String htmlFromMarkdown(final String path) {
        try {
            if (rootDir != null) {
                final InputStream file = rootDir.createRelative(path).getInputStream();
                try {
                    final BufferedReader reader = new BufferedReader( new InputStreamReader(file));
                    String line;
                    final StringBuilder  markdown = new StringBuilder();

                    while((line = reader.readLine()) != null) {
                        markdown.append(line);
                        markdown.append(LINE_SEPARATOR);
                    }
                    return new Markdown4jProcessor().process(markdown.toString()).trim();
                } finally {
                    file.close();
                }
            } else {
                return null;
            }
        } catch (final IOException e) {
            // TODO: log exception + proceed.
            return null;
        }
    }

    private List<String> removeEmptyStringsAndNullValuesFrom(final List<String> list) {
        final List<String> result = new ArrayList<>();
        for (final String s : list) {
            if (s != null && !s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }
}
