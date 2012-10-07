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
package de.otto.jsonhome.model;

import java.net.URI;
import java.util.EnumSet;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static java.util.EnumSet.noneOf;

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
    private final EnumSet<HrefVarFlags> flags;

    public HrefVar(final String var, final URI varType, final String description) {
        this.var = var;
        this.varType = varType;
        this.description = description;
        flags = noneOf(HrefVarFlags.class);
    }

    public HrefVar(final String var,
                   final URI varType,
                   final String description,
                   final EnumSet<HrefVarFlags> flags) {
        this.var = var;
        this.varType = varType;
        this.description = description;
        this.flags = flags;
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
     * Returns the HrefVarFlags of the href variable.
     *
     * @return set of flags
     */
    public EnumSet<HrefVarFlags> getFlags() {
        return flags;
    }

    /**
     * @return a Java representation of a JSON document used to serialize a JsonHome document into application-json format.
     */
    public Map<String, ?> toJson() {
        return singletonMap(
                var, varType.toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HrefVar hrefVar = (HrefVar) o;

        if (description != null ? !description.equals(hrefVar.description) : hrefVar.description != null) return false;
        if (var != null ? !var.equals(hrefVar.var) : hrefVar.var != null) return false;
        if (varType != null ? !varType.equals(hrefVar.varType) : hrefVar.varType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = var != null ? var.hashCode() : 0;
        result = 31 * result + (varType != null ? varType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HrefVar{" +
                "var='" + var + '\'' +
                ", varType=" + varType +
                ", description='" + description + '\'' +
                '}';
    }
}
