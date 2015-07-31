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

/**
 * Status of the linked resource.
 *
 * @author Guido Steinacker
 * @since 15.10.12
 */
public enum Status {
    /** The resource is available. */
    OK,
    /** The resource is deprecated and should not be used anymore. */
    DEPRECATED,
    /** The resource is gone, request will be answered as HTTP 410 gone. */
    GONE;

    /**
     * Merges this status with another one.
     *
     * This is primarily used internally.
     *
     * @param other the other status.
     * @return Status
     */
    public Status mergeWith(final Status other) {
        return  this.ordinal() > other.ordinal() ? this : other;
    }

    /**
     * {@inheritDoc}
     *
     * Returns a lower-case representation of the status.
     *
     * @return ok, deprecated or gone.
     */
    public String toString() {
        return name().toLowerCase();
    }
}
