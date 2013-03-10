package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.controller.JsonHomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class RegistryJsonHomeController {

    private static Logger LOG = LoggerFactory.getLogger(JsonHomeController.class);

    private RegistryJsonHomeSource jsonHomeSource;
    private int maxAge = 3600;

    @Autowired
    public void setJsonHomeSource(final RegistryJsonHomeSource jsonHomeSource) {
        this.jsonHomeSource = jsonHomeSource;
    }

    public void setMaxAgeSeconds(int maxAge) {
        this.maxAge = maxAge;
        LOG.info("MaxAge is {}", maxAge);
    }

    @RequestMapping(
            value = "{registry}/json-home",
            produces = {"application/json-home"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJsonHome(@PathVariable
                                                   @Doc(value = "The name of the json-home registry.")
                                                   final String registry,
                                                   final HttpServletResponse response) {
        LOG.info("Returning json-home in application/json-home format.");
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        return toRepresentation(jsonHomeSource.getJsonHome(registry), APPLICATION_JSONHOME);
    }

    @RequestMapping(
            value = "{registry}/json-home",
            produces = {"application/json"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJson(@PathVariable
                                               @Doc(value = "The name of the json-home registry.")
                                               final String registry,
                                               final HttpServletResponse response) {
        LOG.info("Returning json-home in application/json format.");
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        try {
            return toRepresentation(jsonHomeSource.getJsonHome(registry), APPLICATION_JSON);
        } catch (final IllegalArgumentException e) {
            try { response.sendError(SC_NOT_FOUND, e.getMessage()); } catch (IOException ignore) { }
            throw e;
        }
    }
}
