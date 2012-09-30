package de.otto.jsonhome.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A helper class used to handle ResourceLink instances.
 */
public final class ResourceLinkHelper {

    private ResourceLinkHelper() {
    }

    /**
     * Merges two lists of ResourceLinks into one list of {@link ResourceLink#mergeWith(de.otto.jsonhome.model.ResourceLink) merged} instances.
     *
     * @param resourceLinks the list of resource links. This list will not be modified.
     * @param other the other list of resource links. This list will not be modified.
     * @return a list of merged resource links.
     */
    public static List<ResourceLink> mergeResources(final List<ResourceLink> resourceLinks,
                                                    final List<ResourceLink> other) {
        final List<ResourceLink> allCandidates = new ArrayList<ResourceLink>(resourceLinks.size() + other.size());
        allCandidates.addAll(resourceLinks);
        allCandidates.addAll(other);
        final Map<URI, ResourceLink> resourceLinkCandidates = new LinkedHashMap<URI, ResourceLink>();
        for (final ResourceLink candidate : allCandidates) {
            final URI linkRelationType = candidate.getLinkRelationType();
            final ResourceLink existingCandidate = resourceLinkCandidates.get(linkRelationType);
            if (existingCandidate != null) {
                // merge the candidates, they are belonging to the same resource link
                resourceLinkCandidates.put(linkRelationType, existingCandidate.mergeWith(candidate));
            } else {
                resourceLinkCandidates.put(linkRelationType, candidate);
            }
        }
        return new ArrayList<ResourceLink>(resourceLinkCandidates.values());
    }
}