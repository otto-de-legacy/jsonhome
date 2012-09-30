package de.otto.jsonhome.model;

import java.net.URI;
import java.util.*;

import static java.util.Collections.unmodifiableList;

/**
 * A templated resource link.
 *
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4.1">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-4.1</a>
 * @author Guido Steinacker
 * @since 15.09.12
 */
public final class TemplatedLink implements ResourceLink {

    private final URI relationType;
    private final String hrefTemplate;
    private final List<HrefVar> hrefVars;
    private final List<String> allows;
    private final List<String> representations;

    private TemplatedLink(final URI relationType,
                          final String hrefTemplate,
                          final Collection<HrefVar> hrefVars,
                          final Collection<String> allows,
                          final Collection<String> representations) {
        this.relationType = relationType;
        this.hrefTemplate = hrefTemplate;
        this.hrefVars = unmodifiableList(new ArrayList<HrefVar>(hrefVars));
        this.allows = unmodifiableList(new ArrayList<String>(allows));
        this.representations = unmodifiableList(new ArrayList<String>(representations));
    }

    public static TemplatedLink templatedLink(final URI relationType,
                                              final String hrefTemplate,
                                              final List<HrefVar> hrefVar,
                                              final List<String> allows,
                                              final List<String> representations) {
        return new TemplatedLink(relationType, hrefTemplate, hrefVar, allows, representations);
    }

    public URI getLinkRelationType() {
        return relationType;
    }

    public String getHrefTemplate() {
        return hrefTemplate;
    }

    public List<HrefVar> getHrefVars() {
        return hrefVars;
    }

    @Override
    public List<String> getAllows() {
        return allows;
    }

    @Override
    public List<String> getRepresentations() {
        return representations;
    }

    @Override
    public boolean isDirectLink() {
        return false;
    }

    @Override
    public ResourceLink mergeWith(ResourceLink other) {
        if (other.isDirectLink()) {
            throw new IllegalArgumentException("Merging TemplatedLink with DirectLink is not supported.");
        }
        if (!relationType.equals(other.getLinkRelationType())) {
            throw new IllegalArgumentException("Resource links with different relation types can not be merged. One resource must not have multiple relation types.");
        }
        final TemplatedLink otherTemplatedLink = (TemplatedLink)other;
        if (hrefTemplate.equals(otherTemplatedLink.getHrefTemplate())) {
            final Set<String> allows = new LinkedHashSet<String>(this.allows);
            allows.addAll(otherTemplatedLink.getAllows());
            final Set<String> representations = new LinkedHashSet<String>(this.representations);
            representations.addAll(otherTemplatedLink.getRepresentations());
            return new TemplatedLink(
                    relationType,
                    hrefTemplate,
                    hrefVars,
                    allows,
                    representations
            );
        } else {
            throw new IllegalArgumentException("Templated resource-links with different uri templates can not be merged.");
        }
    }

    @Override
    public Map<String, ?> toJson() {
        final Map<String,String> jsonHrefVars = new LinkedHashMap<String, String>(hrefVars.size());
        for (final HrefVar hrefVar : hrefVars) {
            jsonHrefVars.put(hrefVar.getVar(), hrefVar.getVarType().toString());
        }
        final Map<String, Object> jsonHints = new HashMap<String, Object>();
        jsonHints.put("allow", allows);
        jsonHints.put("representations", representations);
        final Map<String, Object> jsonTemplateVars = new HashMap<String, Object>();
        jsonTemplateVars.put("href-template", hrefTemplate);
        jsonTemplateVars.put("href-vars", jsonHrefVars);
        jsonTemplateVars.put("hints", jsonHints);
        return jsonTemplateVars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplatedLink that = (TemplatedLink) o;

        if (allows != null ? !allows.equals(that.allows) : that.allows != null) return false;
        if (hrefTemplate != null ? !hrefTemplate.equals(that.hrefTemplate) : that.hrefTemplate != null) return false;
        if (hrefVars != null ? !hrefVars.equals(that.hrefVars) : that.hrefVars != null) return false;
        if (relationType != null ? !relationType.equals(that.relationType) : that.relationType != null) return false;
        if (representations != null ? !representations.equals(that.representations) : that.representations != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = relationType != null ? relationType.hashCode() : 0;
        result = 31 * result + (hrefTemplate != null ? hrefTemplate.hashCode() : 0);
        result = 31 * result + (hrefVars != null ? hrefVars.hashCode() : 0);
        result = 31 * result + (allows != null ? allows.hashCode() : 0);
        result = 31 * result + (representations != null ? representations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TemplatedLink{" +
                "relationType=" + relationType +
                ", hrefTemplate='" + hrefTemplate + '\'' +
                ", hrefVars=" + hrefVars +
                ", allows=" + allows +
                ", representations=" + representations +
                '}';
    }
}
