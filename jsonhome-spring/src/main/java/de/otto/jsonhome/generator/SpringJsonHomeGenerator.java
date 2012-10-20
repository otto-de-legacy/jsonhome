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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.net.URI;

/**
 * @author Guido Steinacker
 * @since 17.10.12
 */
@Component
public final class SpringJsonHomeGenerator extends JsonHomeGenerator {

    private URI applicationBaseUri;
    private URI relationTypeBaseUri;

    @Value("${applicationBaseUri}")
    public void setApplicationBaseUri(final String applicationBaseUri) {
        this.applicationBaseUri = URI.create(applicationBaseUri);
    }

    @Value("${relationTypeBaseUri}")
    public void setRelationTypeBaseUri(final String relationTypeBaseUri) {
        this.relationTypeBaseUri = URI.create(relationTypeBaseUri);
    }

    @PostConstruct
    public void postConstruct() {
        setResourceLinkGenerator(new SpringResourceLinkGenerator(applicationBaseUri, relationTypeBaseUri));
    }

    @Override
    protected boolean isCandidateForAnalysis(final Class<?> controller) {
        return controller.getAnnotation(Controller.class) != null;
    }

}
