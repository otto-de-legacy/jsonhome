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

import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.generator.SpringJsonHomeGenerator;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.fixtures.ControllerFixtures.ControllerWithDocumentation;
import static de.otto.jsonhome.fixtures.ControllerFixtures.ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel;
import static de.otto.jsonhome.model.Allow.GET;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 05.10.12
 */
public class JsonHomeControllerTest {

    @Test
    public void testGetHomeDocument() throws Exception {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class,
                "http://example.org",
                null);
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
    }

    @Test
    public void shouldUseRootLinkRelationTypeUri() throws Exception {
        // given

        final JsonHomeController controller = jsonHomeController(
                ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class,
                "http://example.org",
                "http://otto.de");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getJsonHomeDocument(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        assertNotNull(resources.get("http://otto.de/rel/foo"));
    }

    @Test
    public void shouldContainDocsHint() throws Exception {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithDocumentation.class,
                "http://example.org",
                "http://otto.de");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getJsonHomeDocument(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> relFoo = resources.get("http://otto.de/rel/foo");
        assertNotNull(relFoo);
        assertEquals(asMap(relFoo.get("hints")).get("docs"), "http://example.org/doc/foo");
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> asMap(final Object obj) {
        return (Map<String, ?>) obj;
    }

    private JsonHomeController jsonHomeController(final Class<?> controllerType,
                                                  final String applicationBaseUri,
                                                  final String relationTypeBaseUri) {
        final JsonHomeController controller = new JsonHomeController();
        controller.setControllerTypes(controllerType);
        controller.setJsonHomeGenerator(
                jsonHomeFor(applicationBaseUri, relationTypeBaseUri != null ? relationTypeBaseUri : applicationBaseUri)
        );
        controller.setApplicationBaseUri(applicationBaseUri);
        return controller;
    }

    private JsonHomeGenerator jsonHomeFor(final String baseUri, final String relationTypeBaseUri) {
        final SpringJsonHomeGenerator jsonHomeGenerator = new SpringJsonHomeGenerator();
        jsonHomeGenerator.setApplicationBaseUri(baseUri);
        jsonHomeGenerator.setRelationTypeBaseUri(relationTypeBaseUri);
        jsonHomeGenerator.postConstruct();
        return jsonHomeGenerator;
    }



}
