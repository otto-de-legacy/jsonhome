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

package de.otto.jsonhome.resource;

import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.resource.scanner.AnnotationScanner;

import java.util.Collection;

/**
 * @author Sebastian Schroeder
 * @since 11.12.2012
 */
public final class JerseyJsonHomeSource implements JsonHomeSource {

    private final JsonHome jsonHome;

    public JerseyJsonHomeSource(JsonHomeGenerator jsonHomeGenerator, AnnotationScanner annotationScanner) {
        jsonHome = jsonHomeGenerator.with(annotationScanner.scanClasses()).generate();
    }

    public JerseyJsonHomeSource(JsonHomeGenerator jsonHomeGenerator, Collection<Class<?>> classes) {
        jsonHome = jsonHomeGenerator.with(classes).generate();
    }

    /**
     * Returns a JsonHome instance.
     *
     * @return JsonHome.
     */
    @Override
    public JsonHome getJsonHome() {
        return jsonHome;
    }

}
