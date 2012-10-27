package de.otto.jsonhome.client;

import de.otto.jsonhome.annotation.Precondition;
import de.otto.jsonhome.annotation.Status;
import de.otto.jsonhome.model.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Docs.docLink;
import static de.otto.jsonhome.model.Docs.emptyDocs;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.JsonHomeBuilder.jsonHomeBuilder;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;

/**
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class JacksonJsonHomeParser implements JsonHomeParser {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private InputStream stream;

    public JacksonJsonHomeParser() {
    }

    @Override
    public JsonHomeParser fromStream(final InputStream is) {
        this.stream = is;
        return this;
    }

    @Override
    public JsonHome parse() {
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
        } catch (JsonProcessingException e) {
            // TODO: define more specific exceptions
            throw new IllegalArgumentException("Error parsing json-home document: " + e.getMessage(), e);
        } catch (IOException e) {
            // TODO: define more specific exceptions
            throw new IllegalStateException("Unable to get json-home document from stream: " + e.getMessage(), e);
        }
        // TODO: define more specific exceptions
        throw new IllegalStateException("Unable to parse json-home document: no resources defined.");
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
                hrefVars.add(new HrefVar(varName, URI.create(varRefNode.getTextValue()), emptyDocs()));
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
            if (hints.has("docs")) {
                builder.with(docLink(URI.create(hints.get("docs").getTextValue())));
            }
            if (hints.has("precondition-req")) {
                final Iterator<JsonNode> iterator = hints.get("precondition-req").getElements();
                while (iterator.hasNext()) {
                    builder.requiring(Precondition.valueOf(iterator.next().getTextValue().toUpperCase()));
                }
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
