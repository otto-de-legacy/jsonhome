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
import de.otto.jsonhome.model.Docs;
import de.otto.jsonhome.model.HrefVar;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static de.otto.jsonhome.generator.MethodHelper.getParameterInfos;
import static de.otto.jsonhome.generator.UriTemplateHelper.variableNamesFrom;
import static de.otto.jsonhome.model.Docs.emptyDocs;
import static de.otto.jsonhome.model.HrefVar.hrefVar;

public abstract class HrefVarsGenerator {

    private final DocsGenerator docsGenerator = new DocsGenerator();

    /**
     * Analyses the method and returns a list of HrefVar instances, describing the variables of a templated resource.
     *
     * @param method the method to analyse.
     * @return list of href-vars.
     */
    public final List<HrefVar> hrefVarsFor(final URI relationTypeUri, final Method method) {
        final List<HrefVar> hrefVars;
        final HrefTemplate hrefTemplateAnnotation = method.getAnnotation(HrefTemplate.class);
        if (hrefTemplateAnnotation != null) {
            hrefVars = new ArrayList<HrefVar>();
            final String template = hrefTemplateAnnotation.value();
            for (String varName : variableNamesFrom(template)) {
                final URI relationType = relationTypeUri.resolve("#" + varName);
                hrefVars.add(hrefVar(varName, relationType, emptyDocs()));
            }
            if(hrefVars.isEmpty()) {
                throw new IllegalArgumentException("no variables found");
            }
        } else {
            hrefVars = new ArrayList<HrefVar>();
            for (final ParameterInfo parameterInfo : getParameterInfos(method)) {
                final URI relationType = relationTypeUri.resolve("#" + parameterInfo.getName());
                final Docs docs = docsGenerator.documentationFor(parameterInfo);
                if (hasPathVariable(parameterInfo) || hasRequestParam(parameterInfo)) {
                    hrefVars.add(hrefVar(parameterInfo.getName(), relationType, docs));
                }
            }
        }
        return hrefVars;
    }

    protected abstract boolean hasRequestParam(final ParameterInfo parameterInfo);

    protected abstract boolean hasPathVariable(final ParameterInfo parameterInfo);

}
