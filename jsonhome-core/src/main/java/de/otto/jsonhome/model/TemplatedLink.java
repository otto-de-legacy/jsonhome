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
import java.util.*;

import static com.damnhandy.uri.template.UriTemplate.fromTemplate;
import static java.lang.String.format;
import static java.net.URI.create;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableList;

/**
 * A templated resource link, referring to a REST resource using <a href="http://tools.ietf.org/html/rfc6570">RFC6570 URI Templates</a>
 * <p/>
 * The template processor supports <a href="http://tools.ietf.org/html/rfc6570#section-1.2">levels
 * 1 through 4</a> as well as supports composite types. In addition to supporting {@link Map}
 * and {@link List} values as composite types, the library also supports the use of Java objects
 * as well.
 * <p/>
 * In order to access the resource, you can expand the URI template to an URI like this:
 * <code><pre>
 *     URI queryVarType = URI.create("http://example.org/vartypes/query");
 *     // in the real world, you should get the TemplatedLink from a JsonHome instance:
 *     TemplatedLink resourceLink = templatedLink(
 *              URI.create("http://example.org/reltypes/my-resource-type"),
 *              "http://example.org/search/{?query}",
 *              asList(hrefVar("query", queryVarType)),
 *              emptyHints()
 *     );
 *     URI resourceUri = resourceLink.expandToUri(queryVarType, "shirt");
 *     // now you can GET the resource using your favorite REST client...
 * </pre></code>
 * Note that the variables of the template are not used by name, but by the var-type URI. This is similar
 * to the usage of URIs to identify link-relation types instead of directly using the URI of the resource.
 * <p/>
 * This implementation is immutable.
 *
 * @see <a href="ryan@damnhandy.com">Ryan J. McDonough</a>
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4.1">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4.1</a>
 * @author Guido Steinacker
 * @since 15.09.12
 */
public final class TemplatedLink implements ResourceLink {

    private final URI relationType;
    private final String hrefTemplate;
    private final List<HrefVar> hrefVars;
    private final Hints hints;

    private TemplatedLink(final URI relationType,
                          final String hrefTemplate,
                          final Collection<HrefVar> hrefVars,
                          final Hints hints) {
        this.relationType = relationType;
        this.hrefTemplate = hrefTemplate;
        this.hrefVars = unmodifiableList(new ArrayList<HrefVar>(hrefVars));
        this.hints = hints;
    }

    public static TemplatedLink templatedLink(final URI relationType,
                                              final String hrefTemplate,
                                              final List<HrefVar> hrefVars,
                                              final Hints hints) {
        return new TemplatedLink(relationType, hrefTemplate, hrefVars, hints);
    }

    @Override
    public URI getLinkRelationType() {
        return relationType;
    }

    public String getHrefTemplate() {
        return hrefTemplate;
    }

    public List<HrefVar> getHrefVars() {
        return hrefVars;
    }

    /**
     * Returns the HrefVar for a var-type URI, or null if no such HrefVar is used in this templated link.
     *
     * @param varType the URI of the requested var-type.
     * @return HrefVar or null
     */
    public HrefVar getHrefVar(final URI varType) {
        for (HrefVar hrefVar : hrefVars) {
            if (hrefVar.getVarType().equals(varType)) {
                return hrefVar;
            }
        }
        return null;
    }

    @Override
    public Hints getHints() {
        return hints;
    }

    @Override
    public boolean isDirectLink() {
        return false;
    }

    @Override
    public TemplatedLink asTemplatedLink() {
        return this;
    }

    @Override
    public DirectLink asDirectLink() {
        throw new IllegalStateException("not a direct link");
    }

    public URI expandToUri(final Map<URI, Object> values) {
        final Map<String, Object> varNameToValueMap = new HashMap<String, Object>();
        for (Map.Entry<URI, Object> entry : values.entrySet()) {
            final HrefVar hrefVar = getHrefVar(entry.getKey());
            if (hrefVar == null) {
                throw new IllegalArgumentException("TemplatedLink does not define a HrefVar for " + entry.getKey());
            }
            varNameToValueMap.put(hrefVar.getVar(), entry.getValue());
        }
        return create(fromTemplate(hrefTemplate).expand(varNameToValueMap));
    }

    public URI expandToUri(final URI hrefVarBarUri, final Object value) {
        return expandToUri(singletonMap(hrefVarBarUri, value));
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        return expandToUri(values);
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1,
                           final URI hrefVarBarUri2, final Object value2) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        values.put(hrefVarBarUri2, value2);
        return expandToUri(values);
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1,
                           final URI hrefVarBarUri2, final Object value2,
                           final URI hrefVarBarUri3, final Object value3) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        values.put(hrefVarBarUri2, value2);
        values.put(hrefVarBarUri3, value3);
        return expandToUri(values);
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1,
                           final URI hrefVarBarUri2, final Object value2,
                           final URI hrefVarBarUri3, final Object value3,
                           final URI hrefVarBarUri4, final Object value4) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        values.put(hrefVarBarUri2, value2);
        values.put(hrefVarBarUri3, value3);
        values.put(hrefVarBarUri4, value4);
        return expandToUri(values);
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1,
                           final URI hrefVarBarUri2, final Object value2,
                           final URI hrefVarBarUri3, final Object value3,
                           final URI hrefVarBarUri4, final Object value4,
                           final URI hrefVarBarUri5, final Object value5) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        values.put(hrefVarBarUri2, value2);
        values.put(hrefVarBarUri3, value3);
        values.put(hrefVarBarUri4, value4);
        values.put(hrefVarBarUri5, value5);
        return expandToUri(values);
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1,
                           final URI hrefVarBarUri2, final Object value2,
                           final URI hrefVarBarUri3, final Object value3,
                           final URI hrefVarBarUri4, final Object value4,
                           final URI hrefVarBarUri5, final Object value5,
                           final URI hrefVarBarUri6, final Object value6) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        values.put(hrefVarBarUri2, value2);
        values.put(hrefVarBarUri3, value3);
        values.put(hrefVarBarUri4, value4);
        values.put(hrefVarBarUri5, value5);
        values.put(hrefVarBarUri6, value6);
        return expandToUri(values);
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1,
                           final URI hrefVarBarUri2, final Object value2,
                           final URI hrefVarBarUri3, final Object value3,
                           final URI hrefVarBarUri4, final Object value4,
                           final URI hrefVarBarUri5, final Object value5,
                           final URI hrefVarBarUri6, final Object value6,
                           final URI hrefVarBarUri7, final Object value7) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        values.put(hrefVarBarUri2, value2);
        values.put(hrefVarBarUri3, value3);
        values.put(hrefVarBarUri4, value4);
        values.put(hrefVarBarUri5, value5);
        values.put(hrefVarBarUri6, value6);
        values.put(hrefVarBarUri7, value7);
        return expandToUri(values);
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1,
                           final URI hrefVarBarUri2, final Object value2,
                           final URI hrefVarBarUri3, final Object value3,
                           final URI hrefVarBarUri4, final Object value4,
                           final URI hrefVarBarUri5, final Object value5,
                           final URI hrefVarBarUri6, final Object value6,
                           final URI hrefVarBarUri7, final Object value7,
                           final URI hrefVarBarUri8, final Object value8) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        values.put(hrefVarBarUri2, value2);
        values.put(hrefVarBarUri3, value3);
        values.put(hrefVarBarUri4, value4);
        values.put(hrefVarBarUri5, value5);
        values.put(hrefVarBarUri6, value6);
        values.put(hrefVarBarUri7, value7);
        values.put(hrefVarBarUri8, value8);
        return expandToUri(values);
    }

    public URI expandToUri(final URI hrefVarBarUri0, final Object value0,
                           final URI hrefVarBarUri1, final Object value1,
                           final URI hrefVarBarUri2, final Object value2,
                           final URI hrefVarBarUri3, final Object value3,
                           final URI hrefVarBarUri4, final Object value4,
                           final URI hrefVarBarUri5, final Object value5,
                           final URI hrefVarBarUri6, final Object value6,
                           final URI hrefVarBarUri7, final Object value7,
                           final URI hrefVarBarUri8, final Object value8,
                           final URI hrefVarBarUri9, final Object value9) {
        final Map<URI, Object> values = new HashMap<URI, Object>();
        values.put(hrefVarBarUri0, value0);
        values.put(hrefVarBarUri1, value1);
        values.put(hrefVarBarUri2, value2);
        values.put(hrefVarBarUri3, value3);
        values.put(hrefVarBarUri4, value4);
        values.put(hrefVarBarUri5, value5);
        values.put(hrefVarBarUri6, value6);
        values.put(hrefVarBarUri7, value7);
        values.put(hrefVarBarUri8, value8);
        values.put(hrefVarBarUri9, value9);
        return expandToUri(values);
    }

    @Override
    public ResourceLink mergeWith(ResourceLink other) {
        if (other.isDirectLink()) {
            throw new IllegalArgumentException(format(
                    "Merging TemplatedLink with DirectLink is not supported. "
                    + "\nTemplatedLink=%s, \nDirectLink=%s", this, other));
        }
        if (!relationType.equals(other.getLinkRelationType())) {
            throw new IllegalArgumentException(format(
                    "Resource links with different relation types can not be merged. "
                    + "One resource must not have multiple relation types. "
                    + "\nTemplatedLink=%s, \nDirectLink=%s", this, other));
        }
        final TemplatedLink otherTemplatedLink = (TemplatedLink)other;
        if (hrefTemplate.startsWith(otherTemplatedLink.getHrefTemplate())) {
            return new TemplatedLink(
                    relationType,
                    hrefTemplate,
                    hrefVars,
                    hints.mergeWith(other.getHints())
            );
        }
        if (otherTemplatedLink.getHrefTemplate().startsWith(hrefTemplate)) {
            return new TemplatedLink(
                    relationType,
                    otherTemplatedLink.getHrefTemplate(),
                    otherTemplatedLink.getHrefVars(),
                    hints.mergeWith(otherTemplatedLink.getHints())
            );
        }
        throw new IllegalArgumentException(format(
                "Templated resource-links with different uri templates can not be merged. "
                + "\nTemplatedLink=%s, \nDirectLink=%s", this, other));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplatedLink that = (TemplatedLink) o;

        if (hints != null ? !hints.equals(that.hints) : that.hints != null) return false;
        if (hrefTemplate != null ? !hrefTemplate.equals(that.hrefTemplate) : that.hrefTemplate != null) return false;
        if (hrefVars != null ? !hrefVars.equals(that.hrefVars) : that.hrefVars != null) return false;
        if (relationType != null ? !relationType.equals(that.relationType) : that.relationType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = relationType != null ? relationType.hashCode() : 0;
        result = 31 * result + (hrefTemplate != null ? hrefTemplate.hashCode() : 0);
        result = 31 * result + (hrefVars != null ? hrefVars.hashCode() : 0);
        result = 31 * result + (hints != null ? hints.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TemplatedLink{" +
                "relationType=" + relationType +
                ", hrefTemplate='" + hrefTemplate + '\'' +
                ", hrefVars=" + hrefVars +
                ", hints=" + hints +
                '}';
    }

}
