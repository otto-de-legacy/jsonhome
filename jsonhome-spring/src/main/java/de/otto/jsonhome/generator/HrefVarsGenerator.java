package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.HrefVar;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static de.otto.jsonhome.generator.MethodHelper.getParameterInfos;

public class HrefVarsGenerator {


    /**
     * Analyses the method and returns a list of HrefVar instances, describing the variables of a templated resource.
     *
     * @param method the method to analyse.
     * @return list of href-vars.
     */
    public List<HrefVar> hrefVarsFor(final URI rootRelationType, final Method method) {
        final List<HrefVar> hrefVars = new ArrayList<HrefVar>();
        for (final ParameterInfo parameterInfo : getParameterInfos(method)) {
            if (parameterInfo.hasAnnotation(PathVariable.class)) {
                hrefVars.add(new HrefVar(
                        parameterInfo.getName(), rootRelationType.resolve("#" + parameterInfo.getName()), "")
                );
            }
        }
        return hrefVars;
    }
}