package de.otto.jsonhome.spring;

import de.otto.jsonhome.controller.JsonHomeController;
import de.otto.jsonhome.fixtures.spring.TestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Map;

import static java.util.EnumSet.of;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 18.12.12
 */
@ContextConfiguration(locations = "classpath:/testSpringContext.xml")
public class SpringControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TestController controller;

    @Autowired
    private JsonHomeController jsonHomeController;

    @Test
    public void controllerUsedForTestShouldExistAndWork() throws Exception {
        assertEquals(controller.getAFoo("42"), "42");
    }

    @Test(enabled = false)
    @SuppressWarnings("unchecked")
    public void shouldFindJsonHomeWithAspects() {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String,?> json = jsonHomeController.getAsApplicationJson(response);
        // then
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) json.get("resources");
        assertNotNull(resources.get("http://otto.de/rel/foo"));
        final Map<String, String> hrefVars = (Map<String, String>) resources.get("http://otto.de/rel/foo").get("href-vars");
        assertEquals(hrefVars.get("fooId"), "http://localhost/rel/foo#fooId");
    }


}
