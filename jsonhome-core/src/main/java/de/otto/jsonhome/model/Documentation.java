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
    private final URI link;

    private Documentation(final List<String> description, final URI link) {
        this.description = description != null
                ? unmodifiableList(new ArrayList<String>(description))
                : Collections.<String>emptyList();
        this.link = link;
    }

    public static Documentation emptyDocs() {
        return new Documentation(Collections.<String>emptyList(), null);
    }

    public static Documentation documentation(final List<String> description) {
        return new Documentation(description, null);
    }

    public static Documentation documentation(final List<String> description, final URI docUri) {
        return new Documentation(description, docUri);
    }

    public static Documentation docLink(final URI docUri) {
        return new Documentation(Collections.<String>emptyList(), docUri);
    }

    public List<String> getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return description != null && description.size() > 0 && !description.get(0).isEmpty();
    }

    public URI getLink() {
        return link;
    }

    public boolean hasLink() {
        return link != null && !link.toString().isEmpty();
    }

    public Documentation mergeWith(final Documentation other) {
        return new Documentation(
                description.isEmpty() ? other.description : description,
                link == null ? other.getLink() : link
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Documentation that = (Documentation) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Documentation{" +
                "value=" + description +
                ", link=" + link +
                '}';
    }
}
