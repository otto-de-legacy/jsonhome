package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.registry.store.Registries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Hints.emptyHints;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static de.otto.jsonhome.registry.controller.RegistryClientHelper.buildEntry;
import static java.net.URI.create;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 04.01.13
 */
@ContextConfiguration(locations = "classpath:/testSpringContext.xml")
public class RegistryJsonHomeControllerTest extends AbstractTestNGSpringContextTests {

    public static final String BASE_LOCATION = "http://www.example.org/registry/";
    public static final String JSON_HOME_URI = "http://www.example.org/json-home";
    public static final String TEST_JSON_HOME_URI = "http://www.example.org/test/json-home";

    @Autowired
    private RegistriesController registriesController;

    @Autowired
    private Registries registries;

    @BeforeMethod
    public void beforeMethod() {
        registries.clear();
    }

    @Test
    public void shouldReturnJsonHome() throws IOException {
        // given:
        final RegistryJsonHomeController jsonHomeController = new RegistryJsonHomeController();
        jsonHomeController.setJsonHomeSource(getJsonHomeSource());
        registriesController.register("", buildEntry(JSON_HOME_URI), new MockHttpServletResponse());
        registriesController.register("test", buildEntry(TEST_JSON_HOME_URI), new MockHttpServletResponse());
        // when:
        final Map<String, ?> json = jsonHomeController.getAsApplicationJson("", new MockHttpServletResponse());
        // then:
        assertNotNull(json);
        assertTrue(json.containsKey("resources"));
        final Object resources = getFrom(json, "resources");
        assertEquals(((Map)resources).size(), 1);
        final Object resource = getFrom(resources, "http://example.org/rel/foo");
        assertNotNull(resource);
        final Object href = getFrom(resource, "href");
        assertEquals(href, "http://example.org/fooResource");
    }

    @Test
    public void shouldReturnJsonHomeForTestEnvironment() throws IOException {
        // given:
        final RegistryJsonHomeController jsonHomeController = new RegistryJsonHomeController();
        jsonHomeController.setJsonHomeSource(getJsonHomeSource());
        registriesController.register("", buildEntry(JSON_HOME_URI), new MockHttpServletResponse());
        registriesController.register("test", buildEntry(TEST_JSON_HOME_URI), new MockHttpServletResponse());
        // when:
        final Map<String, ?> json = jsonHomeController.getAsApplicationJson("test", new MockHttpServletResponse());
        // then:
        assertNotNull(json);
        assertTrue(json.containsKey("resources"));
        final Object resources = getFrom(json, "resources");
        assertEquals(((Map) resources).size(), 1);
        final Object resource = getFrom(resources, "http://example.org/rel/foo");
        assertNotNull(resource);
        final Object href = getFrom(resource, "href");
        assertEquals(href, "http://example.org/testfooResource");
    }

    private JsonHomeEnvSource getJsonHomeSource() {
        return new JsonHomeEnvSource() {
            @Override
            public JsonHome getJsonHome(final String environment) {
                return jsonHome(
                        directLink(
                                create("http://example.org/rel/foo"),
                                create("http://example.org/" + environment + "fooResource"),
                                emptyHints()
                        )
                );
            }

            @Override
            public JsonHome getJsonHome() {
                return getJsonHome("");
            }
        };
    }

    private Object getFrom(final Object map, final String key) {
        return ((Map)map).get(key);
    }
}
