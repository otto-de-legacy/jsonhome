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

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.model.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static de.otto.jsonhome.generator.DocumentationGenerator.documentationFor;
import static de.otto.jsonhome.generator.MethodHelper.getParameterInfos;
import static de.otto.jsonhome.model.Allow.POST;
import static de.otto.jsonhome.model.Allow.PUT;
import static de.otto.jsonhome.model.Documentation.documentation;
import static de.otto.jsonhome.model.Documentation.emptyDocumentation;
import static de.otto.jsonhome.model.HrefVarFlags.*;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;

public class HrefVarsGenerator {

    private HrefVarsGenerator() {
    }

    /**
     * Analyses the method and returns a list of HrefVar instances, describing the variables of a templated resource.
     *
     * @param method the method to analyse.
     * @return list of href-vars.
     */
    public static List<HrefVar> hrefVarsFor(final URI rootRelationType, final Method method, final Hints hints) {
        final List<HrefVar> hrefVars = new ArrayList<HrefVar>();
        for (final ParameterInfo parameterInfo : getParameterInfos(method)) {
            final URI relationType = rootRelationType.resolve("#" + parameterInfo.getName());
            final Documentation documentation = documentationFor(parameterInfo);
            if (parameterInfo.hasAnnotation(PathVariable.class)) {
                hrefVars.add(new HrefVar(
                        parameterInfo.getName(), relationType, documentation, of(REQUIRED))
                );
            } else  if (parameterInfo.hasAnnotation(RequestParam.class)) {
                final EnumSet<HrefVarFlags> flags = EnumSet.noneOf(HrefVarFlags.class);
                final RequestParam requestParam = parameterInfo.getAnnotation(RequestParam.class);
                if (requestParam.required()) {
                    flags.add(REQUIRED);
                }
                if (hints.getAllows().size() == 1) {
                    if (hints.getAllows().contains(POST)) {
                        flags.add(ONLY_POST);
                    }
                    if (hints.getAllows().contains(PUT)) {
                        flags.add(ONLY_PUT);
                    }
                }
                hrefVars.add(new HrefVar(
                        parameterInfo.getName(), relationType, documentation, flags)
                );
            }
        }
        return hrefVars;
    }

}