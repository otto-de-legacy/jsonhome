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
* @author Guido Steinacker
* @since 15.10.12
*/
public enum Precondition {

    NONE(""), ETAG("etag"), LAST_MODIFIED("last-modified");

    private final String representation;

    private Precondition(final String representation) {
        this.representation = representation;
    }

    public static Precondition preconditionOf(final String value) {
        for (final Precondition precondition : Precondition.values()) {
            if (precondition.toString().equals(value)) {
                return precondition;
            }
        }
        throw new IllegalArgumentException("Unknown precondition " + value);
    }

    public String toString() {
        return representation;
    }

}
