/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.registry;

import java.net.URI;
import java.util.Collection;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public interface Registry {

    /**
     * Puts the registry entry to the registry.
     *
     * @param entry the registered entry.
     * @return true, if the entry was created, false if updated
     * @throws IllegalArgumentException if the entry's href is already registered with a different URI.
     */
    public boolean put(final RegistryEntry entry);

    /**
     * Removes the entry identified by the URI from the registry.
     *
     * @param uri the URI of the entry.
     */
    public void remove(final URI uri);

    /**
     * Returns a collection of all registered entries.
     * @return collection of entries.
     */
    public Collection<RegistryEntry> getAll();

    /**
     * Returns the entry identified by the URI.
     * @param uri URI of the entry.
     * @return RegistryEntry
     */
    public RegistryEntry findBy(final URI uri);

    /**
     * Returns the entry referring to the specified json-home URI.
     * @param href URI of the json-home document.
     * @return RegistryEntry
     */
    public RegistryEntry findByHref(final URI href);

}
