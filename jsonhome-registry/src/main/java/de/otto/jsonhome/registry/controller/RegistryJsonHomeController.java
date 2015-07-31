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
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeConverter.toRepresentation;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSON;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;
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
    private String defaultRegistry = "default";

    @Autowired
    public void setRegistryJsonHomeSource(final RegistryJsonHomeSource registryJsonHomeSource) {
        this.jsonHomeSource = registryJsonHomeSource;
    }

    public void setMaxAgeSeconds(int maxAge) {
        this.maxAge = maxAge;
        LOG.info("MaxAge is {}", maxAge);
    }

    @Value("${jsonhome.defaultRegistry}")
    public void setDefaultRegistry(final String defaultRegistry) {
        this.defaultRegistry = defaultRegistry;
    }

    @RequestMapping(
            value = "/json-home",
            produces = {"application/json-home"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJsonHome(@RequestParam(required = false)
                                                   @Doc(value = "The name of the json-home registry.")
                                                   final String registry,
                                                   final HttpServletResponse response) {
        LOG.info("Returning json-home in application/json-home format.");
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        final String selectedRegistry = registry != null ? registry : defaultRegistry;
        return toRepresentation(jsonHomeSource.getJsonHome(selectedRegistry), APPLICATION_JSONHOME);
    }

    @RequestMapping(
            value = "/json-home",
            produces = {"application/json"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJson(@RequestParam(required = false)
                                               @Doc(value = "The name of the json-home registry.")
                                               final String registry,
                                               final HttpServletResponse response) {
        LOG.info("Returning json-home in application/json format.");
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        try {
            final String selectedRegistry = registry != null ? registry : defaultRegistry;
            return toRepresentation(jsonHomeSource.getJsonHome(selectedRegistry), APPLICATION_JSON);
        } catch (final IllegalArgumentException e) {
            try { response.sendError(SC_NOT_FOUND, e.getMessage()); } catch (IOException ignore) { }
            throw e;
        }
    }
}
