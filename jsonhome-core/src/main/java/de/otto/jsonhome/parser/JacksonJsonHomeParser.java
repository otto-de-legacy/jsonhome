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

package de.otto.jsonhome.parser;

import de.otto.jsonhome.model.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static de.otto.jsonhome.model.Authentication.authReq;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Documentation.documentation;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static de.otto.jsonhome.model.JsonHomeBuilder.jsonHomeBuilder;
import static de.otto.jsonhome.model.Precondition.preconditionOf;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;

/**
 * A JsonHomeParser that is implemented using Jackson.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class JacksonJsonHomeParser implements JsonHomeParser {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonJsonHomeParser.class);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public JsonHome parse(final InputStream stream) {
        try {
            final JsonNode jsonNode = OBJECT_MAPPER.readTree(stream);
            if (jsonNode != null && jsonNode.has("resources")) {
                final JsonHomeBuilder builder = jsonHomeBuilder();
                final JsonNode resourcesNode = jsonNode.get("resources");
                final Iterator<String> fieldNames = resourcesNode.getFieldNames();
                while (fieldNames.hasNext()) {
                    final String fieldName = fieldNames.next();
                    final JsonNode resourceNode = resourcesNode.get(fieldName);
                    builder.addResource(resourceLinkFrom(URI.create(fieldName), resourceNode));
                }
                return builder.build();
            }
        } catch (final JsonProcessingException e) {
            LOG.error("Unable to parse json-home document: {}", e.getMessage());
            throw new IllegalArgumentException("Error parsing json-home document: " + e.getMessage(), e);
        } catch (final IOException e) {
            LOG.error("Unable to get json-home from stream: {}", e.getMessage());
            // TODO: define more specific exceptions
            throw new IllegalStateException("Unable to get json-home document from stream: " + e.getMessage(), e);
        }
        throw new IllegalArgumentException("Unable to parse json-home document: no resources defined.");
    }

    private ResourceLink resourceLinkFrom(final URI relationTypeUri, final JsonNode resourceLinkNode) {
        if (resourceLinkNode.has("href")) {
            return directLink(
                    relationTypeUri,
                    URI.create(resourceLinkNode.get("href").getTextValue()),
                    hintsFrom(resourceLinkNode.get("hints"))
            );
        } else {
            return templatedLink(
                    relationTypeUri,
                    resourceLinkNode.get("href-template").getTextValue(),
                    hrefVarsFrom(resourceLinkNode.get("href-vars")),
                    hintsFrom(resourceLinkNode.get("hints"))
            );
        }
    }

    private List<HrefVar> hrefVarsFrom(final JsonNode jsonNode) {
        if (jsonNode.isContainerNode()) {
            final List<HrefVar> hrefVars = new ArrayList<HrefVar>();
            final Iterator<String> varNames = jsonNode.getFieldNames();
            while (varNames.hasNext()) {
                final String varName = varNames.next();
                final JsonNode varRefNode = jsonNode.get(varName);
                hrefVars.add(hrefVar(varName, URI.create(varRefNode.getTextValue())));
            }
            return hrefVars;
        } else {
            throw new IllegalStateException("Unable to construct a TemplatedLink without href-vars.");
        }
    }

    private Hints hintsFrom(final JsonNode hints) {
        final HintsBuilder builder = hintsBuilder();
        if (hints != null) {
            if (hints.has("allow")) {
                final Iterator<JsonNode> iterator = hints.get("allow").getElements();
                while (iterator.hasNext()) {
                    builder.allowing(Allow.valueOf(iterator.next().getTextValue()));
                }
            }
            if (hints.has("representations")) {
                final Iterator<JsonNode> iterator = hints.get("representations").getElements();
                while (iterator.hasNext()) {
                    builder.representedAs(iterator.next().getTextValue());
                }
            }
            if (hints.has("docs") || hints.has("description") || hints.has("detailedDescription")) {
                final URI docUri = hints.has("docs") ? URI.create(hints.get("docs").getTextValue()) : null;
                final List<String> description = new ArrayList<String>();
                final Iterator<JsonNode> elements = hints.has("description")
                        ? hints.get("description").getElements()
                        : Collections.<JsonNode>emptyIterator();
                while (elements.hasNext()) {
                    description.add(elements.next().getTextValue());
                }
                final String detailedDescription = hints.has("detailedDescription") ? hints.get("detailedDescription").getTextValue() : null;
                builder.with(documentation(description, detailedDescription, docUri));
            }
            if (hints.has("accept-ranges")) {
                final Iterator<JsonNode> iterator = hints.get("accept-ranges").getElements();
                while (iterator.hasNext()) {
                    final String textValue = iterator.next().getTextValue();
                    if (!textValue.isEmpty()) {
                        builder.acceptingRanges(textValue);
                    }
                }
            }
            if (hints.has("prefer")) {
                final Iterator<JsonNode> iterator = hints.get("prefer").getElements();
                while (iterator.hasNext()) {
                    final String textValue = iterator.next().getTextValue();
                    if (!textValue.isEmpty()) {
                        builder.preferring(textValue);
                    }
                }
            }
            if (hints.has("precondition-req")) {
                final Iterator<JsonNode> iterator = hints.get("precondition-req").getElements();
                while (iterator.hasNext()) {
                    final String textValue = iterator.next().getTextValue();
                    if (!textValue.isEmpty()) {
                        builder.requiring(preconditionOf(textValue));
                    }
                }
            }
            if (hints.has("auth-req")) {
                final List<Authentication> authentications = new ArrayList<Authentication>();
                final Iterator<JsonNode> iterator = hints.get("auth-req").getElements();
                while (iterator.hasNext()) {
                    final JsonNode authNode = iterator.next();
                    final String scheme = authNode.get("scheme").getTextValue();
                    final List<String> realms = new ArrayList<String>();
                    if (authNode.has("realms")) {
                        final Iterator<JsonNode> realmIter = authNode.get("realms").getElements();
                        while (realmIter.hasNext()) {
                            realms.add(realmIter.next().getTextValue());
                        }
                    }
                    authentications.add(authReq(scheme, realms));
                }
                builder.withAuthRequired(authentications);
            }
            if (hints.has("accept-put")) {
                final Iterator<JsonNode> iterator = hints.get("accept-put").getElements();
                while (iterator.hasNext()) {
                    builder.acceptingForPut(iterator.next().getTextValue());
                }
            }
            if (hints.has("accept-post")) {
                final Iterator<JsonNode> iterator = hints.get("accept-post").getElements();
                while (iterator.hasNext()) {
                    builder.acceptingForPost(iterator.next().getTextValue());
                }
            }
            if (hints.has("status")) {
                builder.withStatus(Status.valueOf(hints.get("status").getTextValue().toUpperCase()));
            }
        }
        return builder.build();
    }
}
