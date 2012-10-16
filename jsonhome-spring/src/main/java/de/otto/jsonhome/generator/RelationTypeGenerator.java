/*
 * *
 *  Copyright 2012 Guido Steinacker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.Rel;

import java.lang.reflect.Method;
import java.net.URI;

/**
 * @author Guido Steinacker
 * @since 11.10.12
 */
public class RelationTypeGenerator {

    /**
     * Analyses a method of a controller and returns the fully qualified URI of the link-relation type.
     *
     * If the neither the method, nor the controller is annotated with Rel, null is returned.
     *
     * The Rel of the method is overriding the Rel of the Controller.
     *
     * @param controller the controller
     * @param method the method
     * @return URI of the link-relation type, or null
     */
    public static URI relationTypeFrom(final URI relationTypeRootUri, final Class<?> controller, final Method method) {
        final Rel controllerRel = controller.getAnnotation(Rel.class);
        final Rel methodRel = method.getAnnotation(Rel.class);
        if (controllerRel == null && methodRel == null) {
            return null;
        } else {
            final String linkRelationType = methodRel != null
                    ? methodRel.value()
                    : controllerRel.value();
            return URI.create(linkRelationType.startsWith("http://")
                    ? linkRelationType
                    : relationTypeRootUri + linkRelationType);
        }
    }

}
