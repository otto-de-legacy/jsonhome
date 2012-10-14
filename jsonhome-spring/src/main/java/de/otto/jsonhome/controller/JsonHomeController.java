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

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Rel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeConverter.toJsonHome;


/**
 * A Spring controller, serving a json-home document.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@Rel("/rel/json-home")
@Doc(link = "http://tools.ietf.org/html/draft-nottingham-json-home-02")
@RequestMapping(value = "/json-home")
public class JsonHomeController extends JsonHomeControllerBase {

    @RequestMapping(produces = {"application/json-home", "application/json"})
    @ResponseBody
    public Map<String, ?> getHomeDocument(final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=3600");
        return toJsonHome(jsonHome());
    }

    @RequestMapping(produces = "text/html")
    @ResponseBody
    public ModelAndView getResourcesDoc(final HttpServletRequest request,
                                        final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=3600");
        final Map<String,Object> resources = new HashMap<String, Object>();
        resources.put("resources", jsonHome().getResources().values());
        resources.put("contextpath", request.getContextPath());
        return new ModelAndView("doc/resources", resources);
    }


}
