package de.otto.jsonhome.generator;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Guido Steinacker
 * @since 03.10.12
 */
public final class MethodHelper {

    public static final LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

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
        final String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        final List<ParameterInfo> parameterInfos = new ArrayList<ParameterInfo>(parameterNames.length);
        for (int i=0,n=parameterNames.length; i<n; ++i) {
            parameterInfos.add(new ParameterInfo(
                    parameterNames[i],
                    parameterTypes[i],
                    asList(parameterAnnotations[i])
            ));
        }
        return parameterInfos;
    }
}
