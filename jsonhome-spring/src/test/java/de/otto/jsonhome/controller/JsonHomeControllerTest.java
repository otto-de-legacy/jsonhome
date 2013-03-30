/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.otto.jsonhome.controller;

import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.generator.SpringJsonHomeGenerator;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.fixtures.ControllerFixtures.*;
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
                "http://example.org/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJsonHome(response);
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
    public void shouldFindInheritedAnnotations() throws Exception {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithInheritance.class,
                "http://example.org/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJson(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> hints = asMap(resources.get("http://example.org/rel/foo").get("hints"));
        assertEquals(hints.get("description"), asList("controller value"));
    }

    @Test
    public void applicationJsonHomeShouldNotContainAdditionalInformation() throws Exception {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithDocumentation.class,
                "http://example.org/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJsonHome(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> hints = asMap(resources.get("http://example.org/rel/foo").get("hints"));
        assertNull(hints.get("description"));
    }

    @Test
    public void applicationJsonShouldContainAdditionalInformation() throws Exception {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithDocumentation.class,
                "http://example.org/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJson(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");

        final Map<String, ?> hints = asMap(resources.get("http://example.org/rel/foo").get("hints"));
        assertEquals(hints.get("description"), asList("controller value"));
    }

    @Test
    public void shouldUseRootLinkRelationTypeUri() throws Exception {
        // given

        final JsonHomeController controller = jsonHomeController(
                ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class,
                "http://example.org/",
                "http://otto.de/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJsonHome(response);
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
                "http://example.org/",
                "http://otto.de/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJsonHome(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> relFoo = resources.get("http://otto.de/rel/foo");
        assertNotNull(relFoo);
        assertEquals(asMap(relFoo.get("hints")).get("docs"), "http://example.org/doc/foo");
    }

    @Test
    public void shouldContainPreferHint() {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithHints.class,
                "http://example.org/",
                "http://otto.de/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJsonHome(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> relFoo = resources.get("http://otto.de/rel/preferHint");
        assertNotNull(relFoo);
        assertEquals(asMap(relFoo.get("hints")).get("prefer"), asList("return-representation=application/json", "return-asynch"));
    }

    @Test
    public void shouldContainAcceptRangesHint() {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithHints.class,
                "http://example.org/",
                "http://otto.de/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJsonHome(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> relFoo = resources.get("http://otto.de/rel/acceptRangesHint");
        assertNotNull(relFoo);
        assertEquals(asMap(relFoo.get("hints")).get("accept-ranges"), asList("bytes"));
    }

    @Test
    public void shouldContainPreconditionReqEtag() {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithHints.class,
                "http://example.org/",
                "http://otto.de/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJsonHome(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> relFoo = resources.get("http://otto.de/rel/bar");
        assertNotNull(relFoo);
        assertEquals(asMap(relFoo.get("hints")).get("precondition-req"), asList("etag", "last-modified"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldContainAuthReq() {
        // given
        final JsonHomeController controller = jsonHomeController(
                ControllerWithHints.class,
                "http://example.org/",
                "http://otto.de/");
        // when
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String, ?> resourcesMap = controller.getAsApplicationJsonHome(response);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> relFoo = resources.get("http://otto.de/rel/foobar");
        assertNotNull(relFoo);
        final Map<String, Object> basicAuth = new HashMap<String, Object>();
        basicAuth.put("scheme", "Basic");
        basicAuth.put("realms", asList("foo"));
        final Map<String, Object> digestAuth = new HashMap<String, Object>();
        digestAuth.put("scheme", "Digest");
        digestAuth.put("realms", asList("bar"));
        assertEquals(asMap(relFoo.get("hints")).get("auth-req"), asList(
                basicAuth, digestAuth));
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> asMap(final Object obj) {
        return (Map<String, ?>) obj;
    }

    private JsonHomeController jsonHomeController(final Class<?> controllerType,
                                                  final String applicationBaseUri) {
        return jsonHomeController(controllerType, applicationBaseUri, applicationBaseUri);
    }

    private JsonHomeController jsonHomeController(final Class<?> controllerType,
                                                  final String applicationBaseUri,
                                                  final String relationTypeBaseUri) {
        final JsonHomeController controller = new JsonHomeController();
        controller.setJsonHomeSource(getJsonHomeSource(controllerType, applicationBaseUri, relationTypeBaseUri));
        controller.setRelationTypeBaseUri(relationTypeBaseUri);
        return controller;
    }

    private JsonHomeSource getJsonHomeSource(final Class<?> controllerType,
                                             final String baseUri,
                                             final String relationTypeBaseUri) {
        final GeneratorBasedJsonHomeSource source = new GeneratorBasedJsonHomeSource();
        source.setControllerTypes(controllerType);
        source.setJsonHomeGenerator(jsonHomeGenerator(baseUri, relationTypeBaseUri));
        return source;
    }

    private JsonHomeGenerator jsonHomeGenerator(final String baseUri, final String relationTypeBaseUri) {
        final SpringJsonHomeGenerator jsonHomeGenerator = new SpringJsonHomeGenerator();
        jsonHomeGenerator.setApplicationBaseUri(baseUri);
        jsonHomeGenerator.setRelationTypeBaseUri(relationTypeBaseUri != null ? relationTypeBaseUri : baseUri);
        jsonHomeGenerator.postConstruct();
        return jsonHomeGenerator;
    }

}
