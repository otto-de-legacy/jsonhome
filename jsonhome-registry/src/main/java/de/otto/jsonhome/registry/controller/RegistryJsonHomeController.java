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
import java.net.URI;
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeConverter.toRepresentation;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSON;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;
import static java.net.URI.create;

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

    @Autowired
    public void setJsonHomeSource(final JsonHomeEnvSource jsonHomeSource) {
        this.jsonHomeSource = jsonHomeSource;
    }

    @Value("${jsonhome.relationTypeBaseUri}")
    public void setRelationTypeBaseUri(String relationTypeBaseUri) {
        this.relationTypeBaseUri = create(relationTypeBaseUri);
        LOG.info("RelationTypeBaseUri is {}", relationTypeBaseUri);
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
    public Map<String, ?> getAsApplicationJsonHome(@RequestParam(defaultValue = "")
                                                   @Doc("Optionally selects an environment.")
                                                   final String environment,
                                                   final HttpServletResponse response) {
        LOG.info("Returning json-home in application/json-home format.");
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        return toRepresentation(jsonHomeSource.getJsonHome(environment), APPLICATION_JSONHOME);
    }

    @RequestMapping(produces = {"application/json"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJson(@RequestParam(defaultValue = "")
                                               @Doc("Optionally selects an environment.")
                                               final String environment,
                                               final HttpServletResponse response) {
        LOG.info("Returning json-home in application/json format.");
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        return toRepresentation(jsonHomeSource.getJsonHome(environment), APPLICATION_JSON);
    }}
