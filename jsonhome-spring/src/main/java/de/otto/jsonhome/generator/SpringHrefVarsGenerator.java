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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

/**
 * A HrefVarsGenerator used to generate HrefVars for Spring-based applications.
 */
public class SpringHrefVarsGenerator extends HrefVarsGenerator {

    /**
     * Creates a SpringHrefVarsGenerator.
     *
     * @param relationTypeBaseUri the base URI used to create absolute relation-type URIs.
     * @param docRootDir the root classpath directory containing Markdown documents. May be null.
     */
    public SpringHrefVarsGenerator(final URI relationTypeBaseUri, final String docRootDir) {
        super(relationTypeBaseUri, docRootDir);
    }

    @Override
    protected boolean hasRequestParam(final ParameterInfo parameterInfo) {
        return parameterInfo.hasAnnotation(RequestParam.class);
    }

    @Override
    protected boolean hasPathVariable(final ParameterInfo parameterInfo) {
        return parameterInfo.hasAnnotation(PathVariable.class);
    }

}