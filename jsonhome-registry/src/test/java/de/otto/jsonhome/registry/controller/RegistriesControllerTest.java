package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.registry.store.RegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static de.otto.jsonhome.registry.fixture.RegistriesFixture.*;
import static de.otto.jsonhome.registry.fixture.RegistryFixture.registryLiveWithSingleLinkTo;
import static javax.servlet.http.HttpServletResponse.*;
import static org.testng.Assert.assertEquals;

/**
 * Unit tests of the RegistriesController.
 *
 * @author Guido Steinacker
 * @since 04.01.13
 */
@ContextConfiguration(locations = "classpath:/testSpringContext.xml")
public class RegistriesControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private RegistriesController registriesController;

    @Autowired
    private RegistryRepository repository;

    @BeforeMethod
    public void beforeMethod() {
        repository.clear();
    }

    /* GET /registries
        {
            "self" : "http://example.org/registries",
            "registries" : [
                "http://example.org/registries/live",
                "http://example.org/registries/test"
            ]
        }

        HTTP status codes returned by this method:
        200 OK: if the resource was successfully returned.
    */

    @Test
    public void shouldReturnEmtpyRegistriesResource() {
        // given: an empty registry
        final MockHttpServletResponse response = new MockHttpServletResponse();
        // when:
        final Map<String, ?> registries = registriesController.getRegistries(response);
        // then:
        assertEquals(registries, emptyRegistries());
        assertEquals(response.getStatus(), SC_OK);
    }

    @Test
    public void shouldReturnRegistriesWithSingleEntry() {
        // given: an empty registry
        registriesController.putRegistry("live", registryLiveWithSingleLinkTo("foo"), new MockHttpServletResponse());
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> registries = registriesController.getRegistries(response);
        // then:
        assertEquals(registries, oneRegistryNamed("live"));
        assertEquals(response.getStatus(), SC_OK);
    }

    @Test
    public void shouldReturnRegistriesWithTwoEntries() {
        // given:
        registriesController.putRegistry("live", registryLiveWithSingleLinkTo("foo"), new MockHttpServletResponse());
        registriesController.putRegistry("test", registryLiveWithSingleLinkTo("foo"), new MockHttpServletResponse());
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> registries = registriesController.getRegistries(response);
        // then:
        assertEquals(response.getStatus(), SC_OK);
        assertEquals(registries.size(), 2);
        assertEquals(registries.get("live"), twoRegistriesTestAndLive().get("live"));
        assertEquals(registries.get("test"), twoRegistriesTestAndLive().get("test"));
    }

    /* GET /registries/live */

    @Test
    public void shouldReturnNotFoundWhenAccessingNonExistentRegistry() throws IOException {
        // given:
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.getRegistry("foo", response);
        // then:
        assertEquals(response.getStatus(), SC_NOT_FOUND);
    }

    @Test
    public void shouldCreateAndGetExistingRegistry() throws IOException {
        // given:
        registriesController.putRegistry(
                "live",
                registryLiveWithSingleLinkTo("foo"),
                new MockHttpServletResponse());
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> live = registriesController.getRegistry("live", response);
        // then:
        assertEquals(response.getStatus(), SC_OK);
        assertEquals(live, registryLiveWithSingleLinkTo("foo"));
    }

    /* PUT /registries/live */

    @Test
    public void shouldCreateANewRegistryAndAddMissingAttributes() {
        // given:
        final Map<String, Object> registry = registryLiveWithSingleLinkTo("foo");
        registry.remove("self");
        registry.remove("container");
        registry.remove("name");
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.putRegistry("live", registry, response);
        // then:
        assertEquals(response.getStatus(), SC_CREATED);
        assertEquals(
                registriesController.getRegistry("live", new MockHttpServletResponse()),
                registryLiveWithSingleLinkTo("foo"));
    }

    @Test
    public void shouldOverwriteMissingAttributes() {
        // given:
        final Map<String, Object> registry = registryLiveWithSingleLinkTo("foo");
        registry.put("self", "---");
        registry.put("container", "---");
        registry.put("name", "---");
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.putRegistry("live", registry, response);
        // then:
        assertEquals(response.getStatus(), SC_CREATED);
        assertEquals(
                registriesController.getRegistry("live", new MockHttpServletResponse()),
                registryLiveWithSingleLinkTo("foo"));
    }

    @Test
    public void shouldUpdateExistingRegistry() {
        // given:
        final Map<String, Object> registry = registryLiveWithSingleLinkTo("bar");
        registriesController.putRegistry("live", registry, new MockHttpServletResponse());
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.putRegistry("live", registryLiveWithSingleLinkTo("foo"), response);
        // then:
        assertEquals(response.getStatus(), SC_NO_CONTENT);
        assertEquals(
                registriesController.getRegistry("live", new MockHttpServletResponse()),
                registryLiveWithSingleLinkTo("foo"));
    }

    /* DELETE /registries/live */

    @Test
    public void shouldDeleteExistingRegistry() {
        // given:
        registriesController.putRegistry("live", registryLiveWithSingleLinkTo("foo"), new MockHttpServletResponse());
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.deleteRegistry("live", response);
        // then:
        assertEquals(response.getStatus(), SC_NO_CONTENT);
        assertEquals(registriesController.getRegistries(new MockHttpServletResponse()), emptyRegistries());
    }

    @Test
    public void shouldDeleteNonExistentRegistry() {
        // given:
        // when:
        final MockHttpServletResponse response = new MockHttpServletResponse();
        registriesController.deleteRegistry("live", response);
        // then:
        assertEquals(response.getStatus(), SC_NO_CONTENT);
        assertEquals(registriesController.getRegistries(new MockHttpServletResponse()), emptyRegistries());
    }

}
