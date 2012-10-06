package de.otto.jsonhome.controller;

import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.model.JsonHome;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Base class for controllers accessing JsonHome documents.
 *
 * In order to find all controller beans, the json-home is parsed lazily.
 */
public class JsonHomeControllerBase {

    private volatile JsonHome jsonHome = null;
    private Set<Class<?>> controllerTypes;
    private URI rootUri;

    @Value("${rootUri}")
    public void setRootUri(final String rootUri) {
        this.rootUri = URI.create(rootUri);
    }

    @Resource
    public void setApplicationContext(final ApplicationContext applicationContext) {
        final Map<String, Object> controllerBeans = applicationContext.getBeansWithAnnotation(Controller.class);
        controllerTypes = new HashSet<Class<?>>();
        for (Object o : controllerBeans.values()) {
            controllerTypes.add(o.getClass());
        }
    }

    /**
     * Directly inject controller types instead of injecting the ApplicationContext.
     * <p/>
     * Used for testing purposes.
     *
     * @param controllerTypes the list of controller classes.
     */
    public void setControllerTypes(final Class<?>... controllerTypes) {
        this.controllerTypes = new HashSet<Class<?>>(controllerTypes.length);
        for (final Class<?> controllerType : Arrays.asList(controllerTypes)) {
            this.controllerTypes.add(controllerType);
        }
    }

    public URI rootUri() {
        return rootUri;
    }

    public final JsonHome jsonHome() {
        if (jsonHome == null) {
            generateJsonHome();
        }
        return jsonHome;
    }

    private synchronized void generateJsonHome() {
        if (jsonHome == null) {
            jsonHome = JsonHomeGenerator.jsonHomeFor(rootUri).with(controllerTypes);
        }
    }
}