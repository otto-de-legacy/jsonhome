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

/**
 * A direct or templated resource link.
 *
 * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02">http://tools.ietf.org/html/draft-nottingham-json-home-02</a>
 * @author Guido Steinacker
 * @since 16.09.12
 */
public interface ResourceLink {

    /**
     * The link-relation type of the resource link
     *
     * @return URI of the link-relation type.
     * @see <a href="http://tools.ietf.org/html/rfc5988">http://tools.ietf.org/html/rfc5988</a>
     */
    public URI getLinkRelationType();

    /**
     * Returns the hints of a ResourceLink object.
     *
     * @return hints
     * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5">http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5</a>
     */
    public Hints getHints();

    /**
     * @return true, if the resource link is a direct link, false if it is a templated link.
     */
    public boolean isDirectLink();

    /**
     * Returns the ResourceLink as a TemplatedLink.
     *
     * @return TemplatedLink
     * @throws IllegalStateException if this resource is a DirectLink.
     */
    public TemplatedLink asTemplatedLink();

    /**
     * Returns this ResourceLink as a DirectLink.
     *
     * @return DirectLink
     * @throws IllegalStateException if this resource is a TemplatedLink.
     */
    public DirectLink asDirectLink();

    /**
     * Merges this resource link with another one and returns a new instance.
     *
     * There are a number of restrictions that must be met by the other ResourceLink:
     * <ul>
     *     <li>Both links must be of the same kind: either both DirectLinks or both TemplatedLinks</li>
     *     <li>The two link-relation types must be equal</li>
     *     <li>If both links are direct, the href must be equal.</li>
     *     <li>If both links are templated, the href-template and ref-vars must be equal.</li>
     * </ul>
     * Basically, only the hints are merged.
     *
     * @param other the other resource link
     * @return a new, merged ResourceLink instance
     */
    public ResourceLink mergeWith(ResourceLink other);

}
