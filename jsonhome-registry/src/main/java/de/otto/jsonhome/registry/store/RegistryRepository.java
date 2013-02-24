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
 * A repository used to store {@link Registry}.
 * <p/>
 * The registries are identified by their names.
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
public interface RegistryRepository {

    /**
     * Creates or updates Registry.
     * <p/>
     * An existing Registry instance with the same name as the Registry provided as parameter will be be replaced.
     *
     * @param registry the Registry instance.
     */
    public void createOrUpdate(Registry registry);

    /**
     * Deletes a {@link Registry} instance identified by it's name.
     *
     * @param name the name of the deleted registry.
     */
    public void delete(String name);

    /**
     * Returns the names of all known Registry instances.
     *
     * @return registry names.
     */
    public Set<String> getKnownNames();

    /**
     * Returns the registry with the specifed name, or null if no such registry exists.
     */
    public Registry get(String registryName);

    /**
     * Removes all Registry from the repository.
     */
    void clear();
}
