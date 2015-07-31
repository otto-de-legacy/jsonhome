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

package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.model.JsonHome;

/**
 * A source for json-home documents.
 *
 * @author Guido Steinacker
 * @since 20.11.12
 */
public interface RegistryJsonHomeSource {

    /**
     * Returns a JsonHome instance for a specified environment.
     *
     * The default environment is "".
     */
    public JsonHome getJsonHome(String environment);

}
