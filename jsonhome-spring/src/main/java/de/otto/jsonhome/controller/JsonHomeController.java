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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeConverter.toRepresentation;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSON;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;
import static java.net.URI.create;


/**
 * A Spring controller, serving a json-home document.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/json-home")
public class JsonHomeController {

    private JsonHomeSource jsonHomeSource;
    private URI relationTypeBaseUri;
    private int maxAge = 3600;

    @Autowired
    public void setJsonHomeSource(final JsonHomeSource jsonHomeSource) {
        this.jsonHomeSource = jsonHomeSource;
    }

    @Value("${jsonhome.relationTypeBaseUri}")
    public void setRelationTypeBaseUri(String relationTypeBaseUri) {
        this.relationTypeBaseUri = create(relationTypeBaseUri);
    }

    public URI relationTypeBaseUri() {
        return relationTypeBaseUri;
    }

    public void setMaxAgeSeconds(int maxAge) {
        this.maxAge = maxAge;
    }

    @RequestMapping(produces = {"application/json-home"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJsonHome(final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        return toRepresentation(jsonHomeSource.getJsonHome(), APPLICATION_JSONHOME);
    }

    @RequestMapping(produces = {"application/json"})
    @ResponseBody
    public Map<String, ?> getAsApplicationJson(final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        return toRepresentation(jsonHomeSource.getJsonHome(), APPLICATION_JSON);
    }


}
