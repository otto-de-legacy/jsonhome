package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.registry.store.Registry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static de.otto.jsonhome.registry.controller.RegistryClientHelper.*;
import static javax.servlet.http.HttpServletResponse.*;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 04.01.13
 */
@ContextConfiguration(locations = "classpath:/testSpringContext.xml")
public class RegistryControllerTest extends AbstractTestNGSpringContextTests {

    public static final String BASE_LOCATION = "http://www.example.org/registry/";
    public static final String JSON_HOME_URI = "http://www.example.org/foo/bar";
    public static final String JSON_HOME_DEV_URI = "http://www.example.org/dev/test";

    @Autowired
    private RegistryController registryController;

    @Autowired
    private Registry registry;

    @BeforeMethod
    public void beforeMethod() {
        registry.clear();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRegisterResourceAndReturnIDAsLocationHeader() throws IOException {
        // given:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, String> entry = buildEntry(JSON_HOME_URI);
        // when:
        registryController.register("", entry, response);
        // then:
        final String location = response.getHeader("Location");
        final String id = location.substring(BASE_LOCATION.length());
        assertTrue(location.startsWith(BASE_LOCATION));
        assertNotNull(registryController.getEntry(id, "", new MockHttpServletResponse()));
        assertEquals(response.getStatus(), 201);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRegisterResourceInEnvironmentAndReturnIDAsLocationHeader() throws IOException {
        // given:
        final MockHttpServletResponse firstResponse = new MockHttpServletResponse();
        final MockHttpServletResponse secondResponse = new MockHttpServletResponse();
        Map<String, String> defaultEntry = buildEntry(JSON_HOME_URI);
        Map<String, String> devEntry = buildEntry(JSON_HOME_DEV_URI);
        // when:
        registryController.register("", defaultEntry, firstResponse);
        registryController.register("test", devEntry, secondResponse);
        // then:
        final String defaultLocation = firstResponse.getHeader("Location");
        final String devLocation = secondResponse.getHeader("Location");
        assertNotEquals(defaultLocation, devLocation);
        assertFalse(defaultLocation.contains("?environment"));
        assertTrue(devLocation.endsWith("?environment=test"));
        assertEquals(firstResponse.getStatus(), 201);
        assertEquals(secondResponse.getStatus(), 201);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindEnvironmentSpecificJsonHome() throws IOException {
        // given:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, String> entry = buildEntry(JSON_HOME_DEV_URI);
        // when:
        registryController.register("test", entry, response);
        // then:
        final String location = response.getHeader("Location");
        final String id = location.substring(BASE_LOCATION.length(), location.indexOf('?'));
        final String environment = location.substring(location.indexOf('=') + 1);
        assertTrue(location.startsWith(BASE_LOCATION));
        assertNotNull(registryController.getEntry(id, environment, new MockHttpServletResponse()));
    }

    @Test
    public void shouldRegisterNewEntry() throws Exception {
        // given
        // when
        final MockHttpServletResponse response = postEntry(registryController, "http://www.example.org/example/foo");
        // then
        assertEquals(response.getStatus(), 201 /* created */);
        final String location = response.getHeader("Location");
        assertTrue(location.startsWith("http://www.example.org/registry/"));
        assertFalse(location.endsWith("/"));
        final Map<String, String> entry = registryController.getEntry(
                location.substring(location.lastIndexOf("/")+1),
                "",
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://www.example.org/example/foo");
    }

    @Test
    public void shouldFailToRegisterEntryWithSameHref() throws Exception {
        // given
        MockHttpServletResponse response = postEntry(registryController, "http://www.example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        response = postEntry(registryController, "http://www.example.org/example/foo");
        // then
        assertEquals(response.getStatus(), SC_CONFLICT);
        final Map<String, String> entry = registryController.getEntry(
                location.substring(location.lastIndexOf("/")+1),
                "",
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://www.example.org/example/foo");
    }

    @Test
    public void shouldUpdateExistingEntry() throws Exception {
        // given
        final MockHttpServletResponse response = postEntry(registryController, "http://www.example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        final MockHttpServletResponse updateResponse = putEntry(registryController, location, "http://www.example.org/example/bar");
        // then
        assertEquals(updateResponse.getStatus(), SC_NO_CONTENT);
        final Map<String, String> entry = registryController.getEntry(
                location.substring(location.lastIndexOf("/")+1),
                "",
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://www.example.org/example/bar");
    }

    @Test
    public void shouldCreateEntryWithGivenId() throws Exception {
        // given
        // when
        final MockHttpServletResponse response = putEntry(registryController, "http://www.example.org/registry/42", "http://www.example.org/example/bar");
        // then
        assertEquals(response.getStatus(), SC_CREATED);
        final Map<String, String> entry = registryController.getEntry(
                "42",
                "",
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://www.example.org/example/bar");
    }

    @Test
    public void shouldDeleteEntry() throws Exception {
        // given
        MockHttpServletResponse response = postEntry(registryController, "http://www.example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        registryController.unregister(
                location.substring(location.lastIndexOf("/")+1),
                "",
                new MockHttpServletResponse());
        // then
        final MockHttpServletResponse secondResponse = new MockHttpServletResponse();
        final Map<String, String> entry = registryController.getEntry(
                location.substring(location.lastIndexOf("/")+1),
                "",
                secondResponse);

        assertNull(entry);
        assertEquals(secondResponse.getStatus(), SC_NOT_FOUND);
    }

}
