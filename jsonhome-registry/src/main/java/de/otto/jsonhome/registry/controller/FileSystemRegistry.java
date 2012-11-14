package de.otto.jsonhome.registry.controller;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.singletonMap;

/**
 * File based implementation of the Registry interface.
 * <p/>
 * The registry is stored in the <code>&lt;user.home>/.jsonhome/registry.json</code>
 *
 * @author Guido Steinacker
 * @since 14.11.12
 */
@Repository
public class FileSystemRegistry implements Registry {

    public static final String DIR = System.getProperty("user.home") + "/.jsonhome";
    public static final String FILE = DIR + "/registry.json";

    private final Map<URI, RegistryEntry> registry = new ConcurrentHashMap<URI, RegistryEntry>();

    /**
     * Initializes the registry from &lt;user.home>/.jsonhome/registry.json
     */
    @PostConstruct
    public void postConstruct() throws IOException {
        initJsonHomeDir(new File(DIR));
        initRegistry(new File(FILE));
        readRegistry(new File(FILE));
    }

    @Override
    public void register(final RegistryEntry entry) {
        this.registry.put(entry.getHref(), entry);
        writeRegistry(new File(FILE));
    }

    @Override
    public void unregister(final UUID uuid) {
        for (final RegistryEntry registryEntry : registry.values()) {
            if (registryEntry.getUuid().equals(uuid)) {
                registry.remove(registryEntry.getHref());
            }
        }
        writeRegistry(new File(FILE));
    }

    @Override
    public RegistryEntry get(final UUID uuid) {
        for (final RegistryEntry registryEntry : registry.values()) {
            if (registryEntry.getUuid().equals(uuid)) {
                return registryEntry;
            }
        }
        return null;
    }

    @Override
    public RegistryEntry findByHref(final URI uri) {
        return registry.get(uri);
    }

    /**
     * Creates a directory used to store registry data.
     * @param dir the directory
     */
    private void initJsonHomeDir(final File dir) {
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new IllegalStateException("Can not create directory " + dir);
            }
        }
    }

    /**
     * Initializes the registry.json if it does not exist.
     *
     * @param file file
     */
    private void initRegistry(final File file) {
        if (!file.exists()) {
            final JsonFactory factory = new JsonFactory();
            JsonGenerator generator = null;
            try {
                generator = factory.createJsonGenerator(file, JsonEncoding.UTF8);
                generator.writeStartObject(); // {

                generator.writeObjectFieldStart("registry");

                generator.writeEndObject();

                generator.writeEndObject(); // }
            } catch (IOException e) {
                /* ignore */
            } finally {
                if (generator != null) {
                    try { generator.close(); } catch (IOException e1) { /* ignore */}
                }
            }
        }
    }

    private void readRegistry(final File file) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        final Map map = objectMapper.readValue(file, Map.class);
        final Map registryMap = (Map) map.get("registry");
        for (Object o : registryMap.keySet()) {
            final Map entryMap = (Map) registryMap.get(o);
            final URI href = URI.create(o.toString());
            registry.put(
                    href,
                    new RegistryEntry(
                            UUID.fromString(entryMap.get("id").toString()),
                            entryMap.get("title").toString(),
                            href
                    )
            );
        }
    }

    private void writeRegistry(final File file) {
        final ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        final Map<String, Map> registryMap = new HashMap<String, Map>();
        for (final RegistryEntry entry : registry.values()) {
            final Map<String, String> entryMap = new HashMap<String, String>();
            entryMap.put("id", entry.getUuid().toString());
            entryMap.put("title", entry.getTitle());
            registryMap.put(entry.getHref().toString(), entryMap);
        }
        try {
            objectMapper.writeValue(file, singletonMap("registry", registryMap));
        } catch (final IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
