/*
 * *
 *  Copyright 2012 Guido Steinacker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.otto.jsonhome.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Documentation of a ResourceLink or HrefVar.
 * <p/>
 * This implementation is immutable.
 *
 * @author Guido Steinacker
 * @since 10.10.12
 */
public final class Documentation {

    private final List<String> description;
    private final String detailedDescription;
    private final URI link;

    private Documentation(final List<String> description, final String detailedDescription, final URI link) {
        this.description = description != null
                ? unmodifiableList(new ArrayList<String>(description))
                : Collections.<String>emptyList();
        this.detailedDescription = detailedDescription != null ? detailedDescription : "";
        this.link = link;
    }

    public static Documentation emptyDocs() {
        return new Documentation(null, null, null);
    }

    public static Documentation documentation(final List<String> description, final String detailedDescription, final URI docUri) {
        return new Documentation(description, detailedDescription, docUri);
    }

    public static Documentation docLink(final URI docUri) {
        return new Documentation(null, null, docUri);
    }

    /**
     * Returns the list of paragraphs describing the annotated link-relation type or var type of a resource.
     * <p/>
     * This description is filled from {@link de.otto.jsonhome.annotation.Doc#value()}.
     *
     * @return list of paragraphs or empty list.
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * Returns true if there is a non-empty description, false otherwise.
     *
     * @return boolean
     */
    public boolean hasDescription() {
        return description.size() > 0 && !description.get(0).isEmpty();
    }

    /**
     * Returns the HTML description of the annotated link-relation type or var type of a resource.
     * <p/>
     * The detailed description is generated from a Markdown document included using
     * {@link de.otto.jsonhome.annotation.Doc#include()}.
     *
     * @return HTML or empty String.
     */
    public String getDetailedDescription() {
        return detailedDescription;
    }

    /**
     * Returns true if there is some non-empty detailed description, false otherwise.
     *
     * @return boolean
     */
    public boolean hasDetailedDescription() {
        return !detailedDescription.isEmpty();
    }

    /**
     * Returns a link to an external document providing additional documentation.
     * <p/>
     * The link is retrieved from {@link de.otto.jsonhome.annotation.Doc#link()}
     *
     * @return URI or null
     */
    public URI getLink() {
        return link;
    }

    /**
     * Returns true if there is a link to an external documentation, false otherwise.
     *
     * @return boolean
     */
    public boolean hasLink() {
        return link != null && !link.toString().isEmpty();
    }

    /**
     * Merges this Documentation instance with another instance.
     * <p/>
     * If both instances have values for an attribute, the other values will be discarded.
     *
     * @param other the other instance
     * @return a merged documentation.
     */
    public Documentation mergeWith(final Documentation other) {
        return new Documentation(
                description.isEmpty() ? other.description : description,
                detailedDescription.isEmpty() ? other.detailedDescription : detailedDescription,
                link == null ? other.getLink() : link
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Documentation that = (Documentation) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (detailedDescription != null ? !detailedDescription.equals(that.detailedDescription) : that.detailedDescription != null)
            return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (detailedDescription != null ? detailedDescription.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Documentation{" +
                "description=" + description +
                ", detailedDescription='" + detailedDescription + '\'' +
                ", link=" + link +
                '}';
    }
}
