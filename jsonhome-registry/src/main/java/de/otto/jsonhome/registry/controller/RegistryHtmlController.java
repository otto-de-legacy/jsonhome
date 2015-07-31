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
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;


/**
 * This controller is handling the /rel resource of a registry.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
public class RegistryHtmlController {

    private static Logger LOG = LoggerFactory.getLogger(RegistryHtmlController.class);

    private RegistryJsonHomeSource registryJsonHomeSource;
    private int maxAge = 3600;
    private String defaultRegistry = "default";


    @Autowired
    public void setRegistryJsonHomeSource(final RegistryJsonHomeSource registryJsonHomeSource) {
        this.registryJsonHomeSource = registryJsonHomeSource;
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
            method = RequestMethod.GET,
            produces = "text/html")
    @ResponseBody
    public ModelAndView getHtmlHomeDocument(@RequestParam(required = false)
                                            @Doc(value = "The name of the json-home registry.")
                                            final String registry,
                                            final HttpServletRequest request,
                                            final HttpServletResponse response) {
        final String selectedRegistry = registry != null ? registry : defaultRegistry;
        final JsonHome jsonHome = registryJsonHomeSource.getJsonHome(selectedRegistry);
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Vary", "Accept");
        final Map<String,Object> resources = new HashMap<>();
        resources.put("resources", jsonHome.getResources().values());
        resources.put("contextpath", request.getContextPath());
        if (registry != null) {
            resources.put("relQuery", "?registry=" + registry);
        }
        return new ModelAndView("resources", resources);
    }

    @RequestMapping(
            value = "/rel/**",
            method = RequestMethod.GET,
            produces = "text/html"
    )
    public ModelAndView getRelationshipType(@RequestParam(required = false)
                                            @Doc(value = "The name of the json-home registry.")
                                            final String registry,
                                            final HttpServletRequest request) {

        final URI relationTypeURI = URI.create(request.getRequestURL().toString());
        final String selectedRegistry = registry != null ? registry : defaultRegistry;
        final ResourceLink resourceLink = resourceLink(relationTypeURI, selectedRegistry);
        if (resourceLink != null) {
            final Map<String,Object> model = new HashMap<>();
            model.put("contextpath", request.getContextPath());
            model.put("resource", resourceLink);
            if (resourceLink.isDirectLink()) {
                return new ModelAndView("directresource", model);
            } else {
                return new ModelAndView("templatedresource", model);
            }
        } else {
            throw new IllegalArgumentException("Unknown relation type " + relationTypeURI);
        }
    }

    @ResponseStatus(value = NOT_FOUND)
    @ExceptionHandler({IllegalArgumentException.class})
    public void handleNotFound(final IllegalArgumentException e, final HttpServletResponse response) throws IOException {
        response.sendError(404, e.getMessage());
    }

    private ResourceLink resourceLink(final URI relationType, final String selectedRegistry) {
        final JsonHome jsonHome = registryJsonHomeSource.getJsonHome(selectedRegistry);
        return jsonHome.getResourceFor(relationType);
    }
}
