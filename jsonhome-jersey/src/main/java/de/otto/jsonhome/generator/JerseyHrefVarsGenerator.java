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

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.net.URI;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
public final class JerseyHrefVarsGenerator extends HrefVarsGenerator {

    public JerseyHrefVarsGenerator(final URI relationTypeBaseUri, final String docRootDir) {
        super(relationTypeBaseUri, docRootDir);
    }

    /**
     * @param parameterInfo information about a method parameter.
     * @return true if the parameterInfo is describing a request parameter, false otherwise.
     */
    @Override
    protected boolean hasRequestParam(ParameterInfo parameterInfo) {
        return parameterInfo.hasAnnotation(QueryParam.class);
    }

    /**
     * @param parameterInfo information about a method parameter.
     * @return true if the parameterInfo is describing a path variable, false otherwise.
     */
    @Override
    protected boolean hasPathVariable(ParameterInfo parameterInfo) {
        return parameterInfo.hasAnnotation(PathParam.class);
    }

}
