/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.otto.jsonhome.model;

import java.net.URI;

import static de.otto.jsonhome.model.Documentation.emptyDocs;

/**
 * A single href-var used to describe the href-vars of templated resource links.
 * <p/>
 * This implementation is immutable.
 *
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4.1">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4.1</a>
 * @author Guido Steinacker
 * @since 16.09.12
 */
public final class HrefVar {

    private final String var;
    private final URI varType;
    private final Documentation docs;


    private HrefVar(final String var, final URI varType, final Documentation docs) {
        this.var = var;
        this.varType = varType;
        this.docs = docs;
    }

    public static HrefVar hrefVar(final String var, final URI varType) {
        return new HrefVar(var, varType, emptyDocs());
    }

    public static HrefVar hrefVar(final String var, final URI varType, final Documentation docs) {
        return new HrefVar(var, varType, docs);
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
     * Human-readable docs of the HrefVar.
     *
     * @return documenation
     */
    public Documentation getDocs() {
        return docs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HrefVar hrefVar = (HrefVar) o;

        if (docs != null ? !docs.equals(hrefVar.docs) : hrefVar.docs != null)
            return false;
        if (var != null ? !var.equals(hrefVar.var) : hrefVar.var != null) return false;
        if (varType != null ? !varType.equals(hrefVar.varType) : hrefVar.varType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = var != null ? var.hashCode() : 0;
        result = 31 * result + (varType != null ? varType.hashCode() : 0);
        result = 31 * result + (docs != null ? docs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HrefVar{" +
                "var='" + var + '\'' +
                ", varType=" + varType +
                ", docs=" + docs +
                '}';
    }
}
