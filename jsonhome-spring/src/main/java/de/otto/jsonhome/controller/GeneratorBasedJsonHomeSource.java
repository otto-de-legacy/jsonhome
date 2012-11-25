package de.otto.jsonhome.controller;

import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @author Guido Steinacker
 * @since 24.11.12
 */
@Component
public class GeneratorBasedJsonHomeSource implements JsonHomeSource {

    private static Logger LOG = LoggerFactory.getLogger(GeneratorBasedJsonHomeSource.class);

    private JsonHomeGenerator jsonHomeGenerator;
    private Set<Class<?>> controllerTypes = Collections.emptySet();
    private volatile JsonHome jsonHome = null;

    @Autowired
    public void setJsonHomeGenerator(final JsonHomeGenerator jsonHomeGenerator) {
        LOG.info("Using {} to generate JsonHome.", jsonHomeGenerator.getClass().getName());
        this.jsonHomeGenerator = jsonHomeGenerator;
    }

    @Autowired
    public void setApplicationContext(final ApplicationContext applicationContext) {
        final Map<String, Object> controllerBeans = applicationContext.getBeansWithAnnotation(Controller.class);
        controllerTypes = new HashSet<Class<?>>();
        for (Object o : controllerBeans.values()) {
            controllerTypes.add(o.getClass());
        }
        LOG.info("Found {} controllers in application context", controllerTypes.size());
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

    @Override
    public final JsonHome getJsonHome() {
        if (jsonHome == null) {
            synchronized (this) {
                if (jsonHome == null) {
                    LOG.info("Generating JsonHome...");
                    jsonHome = jsonHomeGenerator.with(controllerTypes).generate();
                    LOG.info("Generated JsonHome containing {} relation types.", jsonHome.getResources().size());
                }
            }
        }
        return jsonHome;
    }

}
