package de.otto.jsonhome.controller;

import de.otto.jsonhome.annotation.LinkRelationType;
import de.otto.jsonhome.model.JsonHome;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.otto.jsonhome.generator.JsonHomeGenerator.jsonHomeFor;
import static de.otto.jsonhome.model.JsonHome.emptyJsonHome;


/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/json-home")
public class JsonHomeController {

    private JsonHome jsonHome = emptyJsonHome();
    private URI rootUri;
    private Set<Class<?>> controllerTypes;

    @Value ("${rootUri}")
    public void setRootUri(final String rootUri) {
        this.rootUri = URI.create(rootUri);
    }

    @Resource
    public void setApplicationContext(final ApplicationContext applicationContext) {
        Map<String, Object> controllerBeans = applicationContext.getBeansWithAnnotation(Controller.class);
        controllerTypes = new HashSet<Class<?>>();
        for (Object o : controllerBeans.values()) {
            controllerTypes.add(o.getClass());
        }
    }

    @PostConstruct
    public void postConstruct() {
        jsonHome = jsonHomeFor(rootUri).with(controllerTypes);
    }

    @LinkRelationType(
            value = "http://tools.ietf.org/html/draft-nottingham-json-home-02",
            description = "Json-Home Document."
    )
    @RequestMapping(produces = {"application/json-home", "application/json"})
    @ResponseBody
    public Map<String, ?> getHomeDocument(final HttpServletResponse response) {
        // home document should be cached:
        response.setHeader("Cache-Control", "max-age=3600");
        return jsonHome.toJson();
    }


}
