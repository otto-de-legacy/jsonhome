package de.otto.jsonhome.controller;

import de.otto.jsonhome.annotation.Rel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * A Spring controller, serving a json-home document.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@Rel("/rel/json-home")
@RequestMapping(value = "/json-home")
public class JsonHomeController extends JsonHomeControllerBase {

    @RequestMapping(produces = {"application/json-home", "application/json"})
    @ResponseBody
    public Map<String, ?> getHomeDocument(final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=3600");
        return jsonHome().toJson();
    }

    @RequestMapping(produces = "text/html")
    @ResponseBody
    public ModelAndView getResourcesDoc(final HttpServletRequest request,
                                        final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=3600");
        final Map<String,Object> resources = new HashMap<String, Object>();
        resources.put("resources", jsonHome().getResources());
        resources.put("contextpath", request.getContextPath());
        return new ModelAndView("doc/resources", resources);
    }


}
