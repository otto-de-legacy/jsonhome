package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.registry.store.Registries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.otto.jsonhome.registry.controller.RegistryClientHelper.*;
import static java.util.Collections.singletonList;
import static javax.servlet.http.HttpServletResponse.*;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 04.01.13
 */
@ContextConfiguration(locations = "classpath:/testSpringContext.xml")
public class RegistriesControllerTest extends AbstractTestNGSpringContextTests {

    public static final String REGISTRIES_URI = "http://www.example.org/registries";
    public static final String LIVE_REGISTRY_URI = "http://www.example.org/registries/live";
    public static final String DEVELOP_REGISTRY_URI = "http://www.example.org/registries/develop";
    public static final String JSON_HOME_URI = "http://www.example.org/foo/bar";
    public static final String JSON_HOME_DEV_URI = "http://www.example.org/dev/test";

    public static final Map<String, List<String>> LIVE_REGISTRY_CONTENT;
    public static final Map<String, List<String>> EMPTY_REGISTRY_CONTENT;
    static {
        LIVE_REGISTRY_CONTENT = new HashMap<String, List<String>>();
        LIVE_REGISTRY_CONTENT.put("registries", singletonList(LIVE_REGISTRY_URI));
        EMPTY_REGISTRY_CONTENT = new HashMap<String, List<String>>();
        EMPTY_REGISTRY_CONTENT.put("registries", Collections.<String>emptyList());
    }

    @Autowired
    private RegistriesController registriesController;

    @Autowired
    private Registries registries;

    @BeforeMethod
    public void beforeMethod() {
        registries.clear();
    }

    @Test
    public void shouldFindNoRegistriesInEmptyCollection() {
        // given: an empty registry
        // when:
        final Map<String, List<String>> registries = registriesController.getRegistries();
        // then:
        assertEquals(registries, EMPTY_REGISTRY_CONTENT);
    }

    @Test
    public void shouldCreateANewRegistry() {
        // given: an empty registry
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.createOrUpdateRegistry("live", response);
        // then:
        assertEquals(response.getStatus(), SC_CREATED);
        assertEquals(registriesController.getRegistries(), LIVE_REGISTRY_CONTENT);
    }

    @Test
    public void shouldIgnoreToCreateARegistryTwice() {
        // given:
        registriesController.createOrUpdateRegistry("live", new MockHttpServletResponse());
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.createOrUpdateRegistry("live", response);
        // then:
        assertEquals(response.getStatus(), SC_OK);
        assertEquals(registriesController.getRegistries(), LIVE_REGISTRY_CONTENT);
    }

    @Test
    public void shouldDeleteAnExistingRegistry() {
        // given:
        registriesController.createOrUpdateRegistry("live", new MockHttpServletResponse());
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.deleteRegistry("live", response);
        // then:
        assertEquals(response.getStatus(), SC_OK);
        assertEquals(registriesController.getRegistries(), EMPTY_REGISTRY_CONTENT);
    }

    @Test
    public void shouldFailToRegisterEntryInNonExistentRepository() throws IOException {
        // given:
        Map<String, String> entry = buildEntry(JSON_HOME_URI);
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.register("foo", entry, response);
        // then:
        assertEquals(response.getStatus(), SC_NOT_FOUND);
    }

    @Test
    public void shouldFailToRegisterEntryInUnspecifiedRepository() throws IOException {
        // given:
        Map<String, String> entry = buildEntry(JSON_HOME_URI);
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.register("", entry, response);
        // then:
        assertEquals(response.getStatus(), SC_NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRegisterResourceAndReturnIDAsLocationHeader() throws IOException {
        // given:
        registriesController.createOrUpdateRegistry("live", new MockHttpServletResponse());
        final MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, String> entry = buildEntry(JSON_HOME_URI);
        // when:
        registriesController.register("live", entry, response);
        // then:
        assertEquals(response.getStatus(), SC_CREATED);
        final String location = response.getHeader("Location");
        assertTrue(location.startsWith(REGISTRIES_URI));
        assertNotNull(registriesController.getEntry("live", idFromLocation(location), new MockHttpServletResponse()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRegisterResourceInDifferentEnvironmentsAndReturnIDAsLocationHeader() throws IOException {
        // given:
        registriesController.createOrUpdateRegistry("foo", new MockHttpServletResponse());
        registriesController.createOrUpdateRegistry("test", new MockHttpServletResponse());
        final MockHttpServletResponse firstResponse = new MockHttpServletResponse();
        final MockHttpServletResponse secondResponse = new MockHttpServletResponse();
        Map<String, String> defaultEntry = buildEntry(JSON_HOME_URI);
        Map<String, String> devEntry = buildEntry(JSON_HOME_DEV_URI);
        // when:
        registriesController.register("foo", defaultEntry, firstResponse);
        registriesController.register("test", devEntry, secondResponse);
        // then:
        final String defaultLocation = firstResponse.getHeader("Location");
        final String devLocation = secondResponse.getHeader("Location");
        assertNotEquals(defaultLocation, devLocation);
        assertTrue(devLocation.contains("/registries/test/"));
        assertEquals(firstResponse.getStatus(), 201);
        assertEquals(secondResponse.getStatus(), 201);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindEnvironmentSpecificJsonHome() throws IOException {
        // given:
        registriesController.createOrUpdateRegistry("foo", new MockHttpServletResponse());
        final MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, String> entry = buildEntry(JSON_HOME_DEV_URI);
        // when:
        registriesController.register("foo", entry, response);
        // then:
        final String location = response.getHeader("Location");
        assertTrue(location.startsWith(REGISTRIES_URI));
        assertNotNull(registriesController.getEntry("foo", idFromLocation(location), new MockHttpServletResponse()));
    }

    @Test
    public void shouldRegisterNewEntry() throws Exception {
        // given
        registriesController.createOrUpdateRegistry("foo", new MockHttpServletResponse());
        // when
        final MockHttpServletResponse response = postEntry(registriesController, "http://www.example.org/example/foo");
        // then
        assertEquals(response.getStatus(), 201 /* created */);
        final String location = response.getHeader("Location");
        assertTrue(location.startsWith("http://www.example.org/registries/"));
        assertFalse(location.endsWith("/"));
        final Map<String, String> entry = registriesController.getEntry(
                "foo",
                idFromLocation(location),
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://www.example.org/example/foo");
        assertEquals(entry.get("title"), "A Foo");
        assertEquals(entry.get("collection"), REGISTRIES_URI);
    }

    @Test
    public void shouldFailToRegisterEntryWithSameHref() throws Exception {
        // given
        registriesController.createOrUpdateRegistry("foo", new MockHttpServletResponse());
        MockHttpServletResponse response = postEntry(registriesController, "http://www.example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        response = postEntry(registriesController, "http://www.example.org/example/foo");
        // then
        assertEquals(response.getStatus(), SC_CONFLICT);
        final Map<String, String> entry = registriesController.getEntry(
                "foo",
                idFromLocation(location),
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://www.example.org/example/foo");
    }

    @Test
    public void shouldUpdateExistingEntry() throws Exception {
        // given
        registriesController.createOrUpdateRegistry("foo", new MockHttpServletResponse());
        final MockHttpServletResponse response = postEntry(registriesController, "http://www.example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        final MockHttpServletResponse updateResponse = putEntry(registriesController, location, "http://www.example.org/example/bar");
        // then
        assertEquals(updateResponse.getStatus(), SC_NO_CONTENT);
        final Map<String, String> entry = registriesController.getEntry(
                "foo",
                idFromLocation(location),
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://www.example.org/example/bar");
    }

    @Test
    public void shouldCreateEntryWithGivenId() throws Exception {
        // given
        registriesController.createOrUpdateRegistry("foo", new MockHttpServletResponse());
        // when
        final MockHttpServletResponse response = putEntry(registriesController, "http://www.example.org/registries/foo/42", "http://www.example.org/example/bar");
        // then
        assertEquals(response.getStatus(), SC_CREATED);
        final Map<String, String> entry = registriesController.getEntry(
                "foo",
                "42",
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://www.example.org/example/bar");
    }

    @Test
    public void shouldDeleteEntry() throws Exception {
        // given
        registriesController.createOrUpdateRegistry("foo", new MockHttpServletResponse());
        MockHttpServletResponse response = postEntry(registriesController, "http://www.example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        registriesController.unregister(
                "foo",
                idFromLocation(location),
                new MockHttpServletResponse());
        // then
        final MockHttpServletResponse secondResponse = new MockHttpServletResponse();
        final Map<String, String> entry = registriesController.getEntry(
                "foo",
                idFromLocation(location),
                secondResponse);

        assertNull(entry);
        assertEquals(secondResponse.getStatus(), SC_NOT_FOUND);
    }

    private String idFromLocation(String location) {
        return location.substring(location.lastIndexOf('/') + 1);
    }

}
