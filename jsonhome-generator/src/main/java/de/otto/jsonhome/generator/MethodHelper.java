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

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Guido Steinacker
 * @since 03.10.12
 */
public final class MethodHelper {

    public static final CachingParanamer PARANAMER = new CachingParanamer(new BytecodeReadingParanamer());

    /**
     * Discovers parameter infos of a Method.
     *
     * Debug information must not be removed from the class files, otherwise the name of the method parameters can
     * not be resolved.
     *
     * @param method the Method
     * @return list of ParameterInfo in the correct ordering.
     */
    public static List<ParameterInfo> getParameterInfos(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final String[] parameterNames = parameterNamesOf(method);
        final List<ParameterInfo> parameterInfos = new ArrayList<ParameterInfo>();
        for (int i=0,n=parameterNames.length; i<n; ++i) {
            parameterInfos.add(new ParameterInfo(
                    parameterNames[i],
                    parameterTypes[i],
                    asList(parameterAnnotations[i])
            ));
        }
        return parameterInfos;
    }

    public static String[] parameterNamesOf(final Method method) {
        return PARANAMER.lookupParameterNames(method);
    }

}