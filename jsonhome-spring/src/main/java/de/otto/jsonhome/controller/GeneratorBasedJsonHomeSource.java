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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

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
        controllerTypes = controllerBeans.values().stream().map(Object::getClass).collect(toSet());
        LOG.info("Found {} controllers in application context", controllerTypes.size());
    }

    /**
     * Directly inject controller types instead of injecting the ApplicationContext.
     *
     * Used for testing purposes.
     *
     * @param controllerTypes the list of controller classes.
     */
    public void setControllerTypes(final Class<?>... controllerTypes) {
        this.controllerTypes = new HashSet<>(controllerTypes.length);
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
