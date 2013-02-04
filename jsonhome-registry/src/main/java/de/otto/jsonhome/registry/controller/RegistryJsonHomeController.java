package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.controller.JsonHomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeConverter.toRepresentation;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSON;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;
import static java.net.URI.create;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * @author Guido Steinacker
 * @since 08.01.13
 */
@Controller
@RequestMapping("/json-home")
public class RegistryJsonHomeController {

    private static Logger LOG = LoggerFactory.getLogger(JsonHomeController.class);

    private JsonHomeEnvSource jsonHomeSource;
    private URI relationTypeBaseUri;
    private int maxAge = 3600;
    private String defaultRegistry = "default";

    @Autowired
    public void setJsonHomeSource(final JsonHomeEnvSource jsonHomeSource) {
        this.jsonHomeSource = jsonHomeSource;
    }

    @Value("${jsonhome.relationTypeBaseUri}")
    public void setRelationTypeBaseUri(String relationTypeBaseUri) {
        this.relationTypeBaseUri = create(relationTypeBaseUri);
        LOG.info("RelationTypeBaseUri is {}", relationTypeBaseUri);
    }

    @Value("${jsonhome.defaultRegistry}")
    public void setDefaultRegistry(final String defaultRegistry) {
        this.defaultRegistry = defaultRegistry;
    }

    public URI relationTypeBaseUri() {
        return relationTypeBaseUri;
    }

    public void setMaxAgeSeconds(int maxAge) {
        this.maxAge = maxAge;
        LOG.info("MaxAge is {}", maxAge);
    }

    @RequestMapping(produces = {"application/json-home"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJsonHome(@RequestParam(required = false)
                                                   @Doc(value = {
                                                           "Optionally selects a registry.",
                                                           "By default, the configured default registry is returned."}
                                                   )
                                                   final String registry,
                                                   final HttpServletResponse response) {
        LOG.info("Returning json-home in application/json-home format.");
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        final String selectedRegistry = registry != null ? registry : defaultRegistry;
        return toRepresentation(jsonHomeSource.getJsonHome(selectedRegistry), APPLICATION_JSONHOME);
    }

    @RequestMapping(produces = {"application/json"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJson(@RequestParam(required = false)
                                               @Doc(value = {
                                                       "Optionally selects a registry.",
                                                       "By default, the configured default registry is returned."}
                                               )
                                               final String registry,
                                               final HttpServletResponse response) {
        LOG.info("Returning json-home in application/json format.");
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        final String selectedRegistry = registry != null ? registry : defaultRegistry;
        try {
            return toRepresentation(jsonHomeSource.getJsonHome(selectedRegistry), APPLICATION_JSON);
        } catch (final IllegalArgumentException e) {
            try { response.sendError(SC_NOT_FOUND, e.getMessage()); } catch (IOException ignore) { }
            throw e;
        }
    }
}
