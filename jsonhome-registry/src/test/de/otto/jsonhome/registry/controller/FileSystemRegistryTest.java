package de.otto.jsonhome.registry.controller;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URI;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class FileSystemRegistryTest {

    @BeforeMethod
    public void beforeMethod() {
        new File(System.getProperty("user.home") + "/.jsonhome").delete();
    }

    @Test
    public void shouldCreateJsonHomeDirOnStartup() throws IOException {
        // given
        final FileSystemRegistry registry = new FileSystemRegistry();
        // when
        registry.postConstruct();
        // then
        final String dir = System.getProperty("user.home") + "/.jsonhome";
        assertTrue(new File(dir).exists());
        assertTrue(new File(dir).isDirectory());
    }

    @Test
    public void shouldCreateRegistryFileOnStartup() throws IOException {
        // given
        final FileSystemRegistry registry = new FileSystemRegistry();
        // when
        registry.postConstruct();
        // then
        final Map map = readRegistryAsMap();
        assertTrue(map.containsKey("registry"));
        assertEquals(map.get("registry"), emptyMap());
    }

    @Test
    public void shouldRegisterNewRegistryEntry() throws IOException {
        // given
        final FileSystemRegistry registry = new FileSystemRegistry();
        registry.postConstruct();
        final RegistryEntry entry = new RegistryEntry(
                "foo", URI.create("http://example.org/foo")
        );
        // when
        registry.register(entry);
        // then
        assertEquals(registry.get(entry.getUuid()), entry);
        final Map map = readRegistryAsMap();
        final Map registryMap = (Map) map.get("registry");
        final Map entryMap = (Map) registryMap.get("http://example.org/foo");
        assertNotNull(registryMap);
        assertNotNull(entryMap);
        assertEquals(entryMap.get("id"), entry.getUuid().toString());
        assertEquals(entryMap.get("title"), entry.getTitle());
    }

    private Map readRegistryAsMap() throws IOException {
        final File file = new File(System.getProperty("user.home") + "/.jsonhome/registry.json");
        final JsonFactory factory = new JsonFactory(new ObjectMapper());
        final JsonParser jsonParser = factory.createJsonParser(file);
        try {
            return jsonParser.readValueAs(Map.class);
        } finally {
            jsonParser.close();
        }
    }
}
