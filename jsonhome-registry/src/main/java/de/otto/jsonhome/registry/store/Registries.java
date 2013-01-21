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
package de.otto.jsonhome.registry.store;

import java.util.Set;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public interface Registries {

    /**
     * Creates a new empty {@link Registry}.
     * @param registryName the unique of the registry.
     */
    public void createRegistry(final String registryName);

    /**
     * Deletes a {@link Registry}.
     *
     * @param registryName the name of the deleted registry.
     */
    public void deleteRegistry(final String registryName);

    /**
     * Returns the names of all known registries.
     *
     * @return registry names.
     */
    public Set<String> getKnownRegistryNames();

    /**
     * Returns the registry with the specifed name, or null if no such registry exists.
     */
    public Registry getRegistry(String registryName);

    /**
     * Removes all registries.
     */
    void clear();
}
