package de.otto.jsonhome.model;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author Guido Steinacker
 * @since 16.09.12
 */
public interface ResourceLink {

    public URI getLinkRelationType();

    public boolean isDirectLink();

    public Map<String, ?> toJson();

    public List<String> getAllows();

    public List<String> getRepresentations();

    public ResourceLink mergeWith(ResourceLink other);
}
