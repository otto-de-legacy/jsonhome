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
    public String getName() {
        return relationType.toString().substring(relationType.toString().lastIndexOf("/"));
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
            return new TemplatedLink(
                    relationType,
                    hrefTemplate,
                    hrefVars,
                    hints.mergeWith(other.getHints())
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
        final Map<String, Object> jsonTemplateVars = new HashMap<String, Object>();
        jsonTemplateVars.put("href-template", hrefTemplate);
        jsonTemplateVars.put("href-vars", jsonHrefVars);
        jsonTemplateVars.put("hints", hints.toJson());
        return jsonTemplateVars;
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
