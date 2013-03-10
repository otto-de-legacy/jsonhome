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

package de.otto.jsonhome.registry.store;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory implementation of the {@link RegistryRepository} interface.
 */
public class InMemoryRegistryRepository implements RegistryRepository {

    public final ConcurrentMap<String, Registry> registry = new ConcurrentHashMap<String, Registry>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(Registry registry) {
        this.registry.put(registry.getName(), registry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String name) {
        registry.remove(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getKnownNames() {
        return registry.keySet();
    }

    @Override
    public Registry get(final String registryName) {
        return registry.get(registryName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        registry.clear();
    }
}
