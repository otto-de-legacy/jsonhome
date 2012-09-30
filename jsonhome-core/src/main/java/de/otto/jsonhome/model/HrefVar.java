package de.otto.jsonhome.model;

import java.net.URI;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * A single href-var used to describe the href-vars of templated resource links.
 *
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4.1">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4.1</a>
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

    /**
     * The name of the href variable.
     *
     * @return name
     */
    public String getVar() {
        return var;
    }

    /**
     * The absolute URI that is used as global identifier for the semantics and syntax of this variable.
     *
     * @return absolute URI
     */
    public URI getVarType() {
        return varType;
    }

    /**
     * A human-readable description of the href-var.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return a Java representation of a JSON document used to serialize a JsonHome document into application-json format.
     */
    public Map<String, ?> toJson() {
        return singletonMap(
                var, varType.toString()
        );
    }
}
