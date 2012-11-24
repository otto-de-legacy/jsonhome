/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.controller;

import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;


/**
 * This controller is handling the /rel resource: link-relation types.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/rel")
public class RelController {

    private JsonHomeSource jsonHomeSource;
    private URI relationTypeBaseUri;

    @Autowired
    public void setJsonHomeSource(final JsonHomeSource jsonHomeSource) {
        this.jsonHomeSource = jsonHomeSource;
    }

    @Value("${jsonhome.relationTypeBaseUri}")
    public void setRelationTypeBaseUri(String relationTypeBaseUri) {
        this.relationTypeBaseUri = URI.create(relationTypeBaseUri);
    }

    public URI relationTypeBaseUri() {
        return relationTypeBaseUri;
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.GET,
            produces = "text/html"
    )
    public ModelAndView getRelationshipType(final HttpServletRequest request) {

        final URI relationTypeURI = URI.create(request.getRequestURL().toString());


        final Map<String,Object> model = new HashMap<String, Object>();
        model.put("contextpath", request.getContextPath());
        final JsonHome jsonHome = jsonHomeSource.getJsonHome();
        if (jsonHome.hasResourceFor(relationTypeURI)) {
            final ResourceLink resourceLink = jsonHome.getResourceFor(relationTypeURI);
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
    public void handleNotFound(final HttpServletResponse response) throws IOException {
        response.sendError(404, "Unknown link-relation type.");
    }
}
