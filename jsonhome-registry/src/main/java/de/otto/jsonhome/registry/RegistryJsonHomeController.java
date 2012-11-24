package de.otto.jsonhome.registry;

import de.otto.jsonhome.generator.JsonHomeSource;
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
 * @author Guido Steinacker
 * @since 20.11.12
 */
@Controller
@RequestMapping("/json-home")
public class RegistryJsonHomeController {

    private JsonHomeSource source;
    private int maxAge = 3600;

    public void setMaxAgeSeconds(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setSource(final JsonHomeSource source) {
        this.source = source;
    }

    @RequestMapping(produces = {"application/json-home", "application/json"})
    @ResponseBody
    public Map<String, ?> getJsonHomeDocument(final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        return toJsonHome(source.getJsonHome());
    }

    @RequestMapping(produces = "text/html")
    @ResponseBody
    public ModelAndView getHtmlHomeDocument(final HttpServletRequest request,
                                            final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        final Map<String,Object> resources = new HashMap<String, Object>();
        resources.put("resources", source.getJsonHome().getResources().values());
        resources.put("contextpath", request.getContextPath());
        return new ModelAndView("resources", resources);
    }

}
