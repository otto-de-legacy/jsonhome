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
package de.otto.jsonhome.controller;

import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.fixtures.ControllerFixtures.ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel;
import static de.otto.jsonhome.model.Allow.GET;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 05.10.12
 */
public class JsonHomeControllerTest {

    @Test
    public void testGetHomeDocument() throws Exception {
        // given
        final JsonHomeController controller = jsonHomeController();
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getHomeDocument(response);
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
    }

    private JsonHomeController jsonHomeController() {
        final JsonHomeController controller = new JsonHomeController();
        controller.setControllerTypes(ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class);
        controller.setRootUri("http://example.org");
        return controller;
    }

}
