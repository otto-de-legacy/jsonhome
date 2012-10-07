package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.Hints;
import de.otto.jsonhome.model.HrefVar;
import de.otto.jsonhome.model.HrefVarFlags;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static de.otto.jsonhome.generator.MethodHelper.getParameterInfos;
import static de.otto.jsonhome.model.HrefVarFlags.*;
import static java.util.EnumSet.of;

public class HrefVarsGenerator {


    /**
     * Analyses the method and returns a list of HrefVar instances, describing the variables of a templated resource.
     *
     * @param method the method to analyse.
     * @return list of href-vars.
     */
    public List<HrefVar> hrefVarsFor(final URI rootRelationType, final Method method, final Hints hints) {
        final List<HrefVar> hrefVars = new ArrayList<HrefVar>();
        for (final ParameterInfo parameterInfo : getParameterInfos(method)) {
            final URI relationType = rootRelationType.resolve("#" + parameterInfo.getName());
            if (parameterInfo.hasAnnotation(PathVariable.class)) {
                hrefVars.add(new HrefVar(
                        parameterInfo.getName(), relationType, "", of(REQUIRED))
                );
            }
            if (parameterInfo.hasAnnotation(RequestParam.class)) {
                final EnumSet<HrefVarFlags> flags = EnumSet.noneOf(HrefVarFlags.class);
                final RequestParam requestParam = parameterInfo.getAnnotation(RequestParam.class);
                if (requestParam.required()) {
                    flags.add(REQUIRED);
                }
                if (hints.getAllows().size() == 1) {
                    if (hints.getAllows().contains("POST")) {
                        flags.add(ONLY_POST);
                    }
                    if (hints.getAllows().contains("PUT")) {
                        flags.add(ONLY_PUT);
                    }
                }
                hrefVars.add(new HrefVar(
                        parameterInfo.getName(), relationType, "", flags)
                );
            }
        }
        return hrefVars;
    }
}