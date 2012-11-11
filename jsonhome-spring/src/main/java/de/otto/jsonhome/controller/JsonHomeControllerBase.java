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

import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.model.JsonHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;

/**
 * Base class for controllers accessing JsonHome documents.
 *
 * In order to find all controller beans, the json-home is parsed lazily.
 */
public class JsonHomeControllerBase {

    private JsonHomeGenerator jsonHomeGenerator;
    private volatile JsonHome jsonHome = null;
    private Set<Class<?>> controllerTypes = Collections.emptySet();
    private URI applicationBaseUri;

    @Value("${jsonhome.applicationBaseUri}")
    public void setApplicationBaseUri(final String baseUri) {
        this.applicationBaseUri = URI.create(baseUri);
    }

    @Autowired
    public void setJsonHomeGenerator(final JsonHomeGenerator jsonHomeGenerator) {
        this.jsonHomeGenerator = jsonHomeGenerator;
    }

    @Autowired
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

    public URI baseUri() {
        return applicationBaseUri;
    }

    public final JsonHome jsonHome() {
        if (jsonHome == null) {
            generateJsonHome();
        }
        return jsonHome;
    }

    private synchronized void generateJsonHome() {
        if (jsonHome == null) {
            jsonHome = jsonHomeGenerator.with(controllerTypes).generate();
        }
    }

}