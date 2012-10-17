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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Guido Steinacker
 * @since 03.10.12
 */
public final class MethodHelper {

    /**
     * Discovers parameter infos of a Method.
     * <p/>
     * Debug information must not be removed from the class files, otherwise the name of the method parameters can
     * not be resolved.
     *
     * @param method the Method
     * @return list of ParameterInfo in the correct ordering.
     */
    public static List<ParameterInfo> getParameterInfos(final Method method) {
        final Iterator<Class<?>> parameterTypes = asList(method.getParameterTypes()).iterator();
        final Iterator<Annotation[]> parameterAnnotations = asList(method.getParameterAnnotations()).iterator();
        final Iterator<String> parameterNames = getParameterNames(method).iterator();
        final List<ParameterInfo> parameterInfos = new ArrayList<ParameterInfo>();
        while (parameterTypes.hasNext()) {
            parameterInfos.add(new ParameterInfo(
                    parameterNames.next(),
                    parameterTypes.next(),
                    asList(parameterAnnotations.next())
            ));
        }
        return parameterInfos;
    }

    private static List<String> getParameterNames(final Method method) {
        try {
            final String declaringClassName = method.getDeclaringClass().getName();
            final AsmMethodVisitor visitor = new AsmMethodVisitor(method);
            new ClassReader(declaringClassName).accept(visitor, 0);
            return visitor.getParameterNames();
        } catch (IOException e) {
            throw new IllegalStateException("IOException caught while parsing class.");
        }
    }

    private static class AsmMethodVisitor extends EmptyVisitor {
        private final Method method;
        private final List<String> parameterNames = new ArrayList<String>();

        public AsmMethodVisitor(final Method method) {
            this.method = method;
        }

        public List<String> getParameterNames() {
            return parameterNames;
        }

        @Override
        public MethodVisitor visitMethod(int access,
                                         String name, String desc, String signature,
                                         String[] exceptions) {

            if (name.equals(method.getName())) {
                org.objectweb.asm.commons.Method asmMethod = org.objectweb.asm.commons.Method
                        .getMethod(method.toString());

                if (Arrays.equals(org.objectweb.asm.Type
                        .getArgumentTypes(desc), asmMethod
                        .getArgumentTypes())) {

                    return new EmptyVisitor() {
                        int currentArgumentCount;

                        @Override
                        public void visitLocalVariable(
                                String name, String desc,
                                String signature, Label start,
                                Label end, int index) {
                            final Class<?>[] parameterTypes = method.getParameterTypes();
                            if (currentArgumentCount++ <= parameterTypes.length && !name.equals("this")) {
                                parameterNames.add(name);
                            }
                        }
                    };
                }
            }

            return null;
        }
    }

}
