package de.otto.jsonhome.registry.controller;

import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class JsonHomeRegistryControllerTest {


    /*
    @Test
    public void testGetHomeDocument() throws Exception {
        // given
        final JsonHomeRegistryController controller = jsonHomeRegistryController("http://example.org");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getJsonHomeDocument(response);
        // then
        assertEquals(response.getHeader("Cache-Control"), "max-age=3600");
        assertEquals(resourcesMap.size(), 1);
        assertTrue(resourcesMap.containsKey("resources"));
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("representations", asList("text/html"));
        expected.put("allow", of(GET));
        final Object hints = resources.get("http://example.org/rel/foo").get("hints");
        assertEquals(hints, expected);
    }*/

    /**
     * Factory method used to create a JsonHomeRegistryController instance.
     * @param applicationBaseUri base uri of the application
     * @return JsonHomeRegistryController
     */
    private JsonHomeRegistryController jsonHomeRegistryController(final String applicationBaseUri) {
        final JsonHomeRegistryController controller = new JsonHomeRegistryController();
        controller.setApplicationBaseUri(applicationBaseUri);
        return controller;
    }

}
