package de.steinacker.jsonhome.model;

import java.net.URI;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * @author Guido Steinacker
 * @since 16.09.12
 */
public final class HrefVar {

    private final String var;
    private final URI varType;
    private final String description;

    public HrefVar(final String var, final URI varType, final String description) {
        this.var = var;
        this.varType = varType;
        this.description = description;
    }

    public String getVar() {
        return var;
    }

    public URI getVarType() {
        return varType;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, ?> toJson() {
        return singletonMap(
                var, varType.toString()
        );
    }
}
