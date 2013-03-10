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

package de.otto.jsonhome.generator;

import de.otto.jsonhome.JsonHomeProperties;

import javax.ws.rs.Path;
import java.net.URI;
import java.util.Properties;

import static java.net.URI.create;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
public final class JerseyJsonHomeGenerator extends JsonHomeGenerator {

    public JerseyJsonHomeGenerator() {
        final Properties properties = JsonHomeProperties.getProperties();
        final String applicationBaseUri = properties.getProperty("applicationBaseUri");
        if (applicationBaseUri == null) {
            throw new IllegalStateException("applicationBaseUri property not set in jsonhome.properties");
        }
        final String relationTypeBaseUri = properties.getProperty("relationTypeBaseUri");
        if (relationTypeBaseUri == null) {
            throw new IllegalStateException("relationTypeBaseUri property not set in jsonhome.properties");
        }
        final String varTypeBaseUri = properties.getProperty("varTypeBaseUri", null);
        final String docRootDir = properties.getProperty("docRootDir", null);
        setResourceLinkGenerator(new JerseyResourceLinkGenerator(
                create(applicationBaseUri),
                create(relationTypeBaseUri),
                varTypeBaseUri != null ? create(varTypeBaseUri) : null,
                docRootDir));
    }

    JerseyJsonHomeGenerator(URI applicationBaseUri, URI relationTypeBaseUri) {
        setResourceLinkGenerator(new JerseyResourceLinkGenerator(applicationBaseUri, relationTypeBaseUri, null, null));
    }

    public JerseyJsonHomeGenerator(String applicationBaseUri, String relationTypeBaseUri) {
        this(create(applicationBaseUri), create(relationTypeBaseUri));
    }

    /**
     * Returns true if the resource is a candidate for further processing, false otherwise.
     *
     * @param resource the resource to check
     * @return boolean
     */
    @Override
    protected boolean isCandidateForAnalysis(Class<?> resource) {
        return resource.getAnnotation(Path.class) != null;
    }

}
