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

    public static List<? extends ResourceLink> mergeResources(final List<? extends ResourceLink> resourceLinks,
                                                    final List<? extends ResourceLink> other) {
        if (other.isEmpty()) {
            return resourceLinks;
        } else {
            List<? extends ResourceLink> result = resourceLinks;
            for (final ResourceLink resourceLink : other) {
                result = mergeResources(result, resourceLink);
            }
            return result;
        }
    }

    /**
     * Merges two lists of ResourceLinks into one list of {@link ResourceLink#mergeWith(de.otto.jsonhome.model.ResourceLink) merged} instances.
     *
     * @param resourceLinks the list of resource links. This list will not be modified.
     * @param other the other list of resource links. This list will not be modified.
     * @return a list of merged resource links.
     */
    public static List<? extends ResourceLink> mergeResources(final List<? extends ResourceLink> resourceLinks,
                                                    final ResourceLink other) {
        final List<ResourceLink> allCandidates = new ArrayList<ResourceLink>(resourceLinks);
        if (other != null) {
            allCandidates.add(other);
        }
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