package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.VarType;
import de.otto.jsonhome.annotation.VarTypes;
import de.otto.jsonhome.model.HrefVar;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HrefVarsGenerator {

    /**
     * Analyses the method and returns a list of HrefVar instances, describing the variables of a templated resource.
     *
     * @param method the method to analyse.
     * @return list of href-vars.
     */
    public List<HrefVar> hrefVarsFor(final Method method) {
        final List<HrefVar> varBuilder = new ArrayList<HrefVar>();
        final VarTypes varTypes = method.getAnnotation(VarTypes.class);
        if (varTypes != null) {
            for (final VarType varType : varTypes.value()) {
                varBuilder.add(new HrefVar(
                        varType.value(),
                        URI.create(varType.reference()),
                        varType.description())
                );
            }
        } else {
            final VarType varType = method.getAnnotation(VarType.class);
            if (varType != null) {
                varBuilder.add(new HrefVar(
                        varType.value(),
                        URI.create(varType.reference()),
                        varType.description())
                );
            }
        }
        return varBuilder;
    }
}