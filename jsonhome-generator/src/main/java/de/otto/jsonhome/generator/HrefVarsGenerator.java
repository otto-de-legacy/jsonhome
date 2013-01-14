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
package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.HrefTemplate;
import de.otto.jsonhome.model.Documentation;
import de.otto.jsonhome.model.HrefVar;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static de.otto.jsonhome.generator.MethodHelper.getParameterInfos;
import static de.otto.jsonhome.generator.UriTemplateHelper.variableNamesFrom;
import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static java.net.URI.create;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

public abstract class HrefVarsGenerator {

    private final DocsGenerator docsGenerator;

    protected HrefVarsGenerator(final URI relationTypeBaseUri) {
        docsGenerator = new DocsGenerator(relationTypeBaseUri);
    }

    /**
     * Analyzes the method and returns a list of HrefVar instances, describing the variables of a templated resource.
     *
     * @param varTypeBaseUri the base URI used to construct var-type URIs.
     * @param varTypeUrisAsFragment if true, the varType URI is created as a fragment of the varTypeBaseUri,
     *                              otherwise as a sub-resource.
     * @param method the method to analyse.
     * @return list of href-vars.
     */
    public final List<HrefVar> hrefVarsFor(final URI varTypeBaseUri, final boolean varTypeUrisAsFragment, final Method method) {
        final List<HrefVar> hrefVars;
        final HrefTemplate hrefTemplateAnnotation = findAnnotation(method, HrefTemplate.class);
        if (hrefTemplateAnnotation != null) {
            hrefVars = new ArrayList<HrefVar>();
            final String template = hrefTemplateAnnotation.value();
            for (String varName : variableNamesFrom(template)) {
                hrefVars.add(hrefVar(varName, varTypeUriFrom(varTypeBaseUri, varName, varTypeUrisAsFragment), emptyDocs()));
            }
            if(hrefVars.isEmpty()) {
                throw new IllegalArgumentException("no variables found");
            }
        } else {
            hrefVars = new ArrayList<HrefVar>();
            for (final ParameterInfo parameterInfo : getParameterInfos(method)) {
                final String varName = parameterInfo.getName();
                final Documentation docs = docsGenerator.documentationFor(parameterInfo);
                if (hasPathVariable(parameterInfo) || hasRequestParam(parameterInfo)) {
                    hrefVars.add(hrefVar(varName, varTypeUriFrom(varTypeBaseUri, varName, varTypeUrisAsFragment), docs));
                }
            }
        }
        return hrefVars;
    }

    /**
     * @param parameterInfo information about a method parameter.
     * @return true if the parameterInfo is describing a request parameter, false otherwise.
     */
    protected abstract boolean hasRequestParam(final ParameterInfo parameterInfo);

    /**
     * @param parameterInfo information about a method parameter.
     * @return true if the parameterInfo is describing a path variable, false otherwise.
     */
    protected abstract boolean hasPathVariable(final ParameterInfo parameterInfo);

    /**
     * Constructs the URI of a var-type as a fragment or sub-resource of a base URI.
     * @param varTypeBaseUri the base URI used to construct the URI of the var type.
     * @param varName the name of the variable
     * @param asFragment create resultung URI as a fragment (#varName) or sub-resource (/varName)
     * @return URI of the varType
     */
    private URI varTypeUriFrom(final URI varTypeBaseUri, final String varName, final boolean asFragment) {
        final String s = varTypeBaseUri.toString();
        if (asFragment) {
            return s.endsWith("/")
                    ? create(s.substring(0, s.length() - 1) + "#" + varName)
                    : create(s + "#" + varName);
        } else {
            return varTypeBaseUri.toString().endsWith("/")
                    ? create(s + varName)
                    : create(s + "/" + varName);
        }
    }

}
