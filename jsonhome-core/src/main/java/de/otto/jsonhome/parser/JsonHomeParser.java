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

package de.otto.jsonhome.parser;

import de.otto.jsonhome.model.JsonHome;

import java.io.InputStream;

/**
 * JsonHomeParser is used to parse a json-home document and return a JsonHome instance.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public interface JsonHomeParser {

    /**
     * Parses the json-home document from an InputStream and returns a JsonHome instance.
     * @param stream the stream used to read the json-home document.
     * @return JsonHome
     */
    public JsonHome parse(final InputStream stream);

}
