package de.otto.jsonhome.controller;

import de.otto.jsonhome.model.JsonHome;
import org.testng.annotations.Test;

import static de.otto.jsonhome.fixtures.ControllerFixtures.ControllerWithoutResource;
import static org.testng.Assert.assertNotNull;

/**
 * @author Guido Steinacker
 * @since 06.10.12
 */
public class JsonHomeControllerBaseTest {

    @Test
    public void shouldReturnJsonHomeInstance() {
        // given
        final JsonHomeControllerBase jsonHomeConfiguration = new JsonHomeControllerBase();
        jsonHomeConfiguration.setControllerTypes(ControllerWithoutResource.class);
        jsonHomeConfiguration.setRootUri("http://example.org");
        // when
        final JsonHome jsonHome = jsonHomeConfiguration.jsonHome();
        // then
        assertNotNull(jsonHome);
    }
}
