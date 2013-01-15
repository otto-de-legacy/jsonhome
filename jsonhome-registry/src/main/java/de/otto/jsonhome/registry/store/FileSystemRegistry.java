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

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static de.otto.jsonhome.registry.controller.RegistryConverter.registryEntriesFromMap;
import static de.otto.jsonhome.registry.controller.RegistryConverter.registryEntriesToMap;

/**
 * File based implementation of the Registry interface.
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
@Repository
public class FileSystemRegistry implements Registry {

    public static final String DEFAULT_FILE = System.getProperty("user.home") + "/.jsonhome/registry.json";

    private final Map<URI, RegistryEntry> registry = new ConcurrentHashMap<URI, RegistryEntry>();
    private final File registryFile;

    /**
     * Initializes the registry from &lt;user.home>/.jsonhome/registry.json
     *
     * @throws IOException if reading the registry failed with an IOException.
     */
    public FileSystemRegistry() throws IOException {
        registryFile = new File(DEFAULT_FILE);
        initRegistry();
        readRegistry();
    }

    /**
     * Initializes the registry from the specified file.
     *
     * @param registryFile the file containing the registry data.
     * @throws IOException if reading the registry failed with an IOException.
     */
    public FileSystemRegistry(final File registryFile) throws IOException {
        this.registryFile = registryFile;
        initRegistry();
        readRegistry();
    }

    /** {@inheritDoc} */
    @Override
    public boolean put(final RegistryEntry entry) {
        final RegistryEntry registryEntry = findByHref(entry.getHref());
        if (registryEntry != null && !registryEntry.getSelf().equals(entry.getSelf())) {
            throw new IllegalArgumentException("An entry with same href but different URI already exists.");
        } else {
            final boolean created = this.registry.put(entry.getSelf(), entry) == null;
            writeRegistry();
            return created;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void remove(final URI entryUri) {
        registry.remove(entryUri);
        writeRegistry();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<RegistryEntry> getAllFrom(final String environment) {
        final Collection<RegistryEntry> filteredEntries = new ArrayList<RegistryEntry>();
        for (final RegistryEntry entry : registry.values()) {
            final String query = entry.getSelf().getQuery();
            if (query == null) {
                if (environment.isEmpty()) {
                    filteredEntries.add(entry);
                }
            } else {
                if (query.matches("environment=" + environment)) {
                    filteredEntries.add(entry);
                }
            }
        }
        return filteredEntries;
    }

    /** {@inheritDoc} */
    @Override
    public RegistryEntry findBy(final URI uri) {
        return registry.get(uri);
    }

    /** {@inheritDoc} */
    @Override
    public RegistryEntry findByHref(final URI href) {
        for (final RegistryEntry registryEntry : registry.values()) {
            if (registryEntry.getHref().equals(href)) {
                return registryEntry;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        registry.clear();
        writeRegistry();
    }

    /**
     * Initializes the file used to store the registry if it does not exist.
     *
     */
    private void initRegistry() throws IOException {
        if (!registryFile.exists()) {
            // init directory
            final File dir = registryFile.getParentFile();
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new IllegalStateException("Can not create directory " + dir);
                }
            }
            writeRegistry();
        }
    }

    /**
     * Reads the registry from the file.
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private void readRegistry() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        final Map<String, ?> map = objectMapper.readValue(registryFile, Map.class);
        final List<RegistryEntry> entries = registryEntriesFromMap(map);
        for (final RegistryEntry entry : entries) {
            registry.put(entry.getSelf(), entry);
        }
    }

    /**
     * Writes the registry to the file.
     *
     */
    private void writeRegistry() {
        final ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        final Map<String, ?> registryMap = registryEntriesToMap(registry.values());
        try {
            objectMapper.writeValue(registryFile, registryMap);
        } catch (final IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
