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
package de.otto.jsonhome.registry;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.*;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public class RegistryControllerTest {

    @Test
    public void shouldRegisterNewEntry() throws Exception {
        // given
        final RegistryController controller = registryController();
        // when
        final MockHttpServletResponse response = postEntry(controller, "http://example.org/example/foo");
        // then
        assertEquals(response.getStatus(), 201 /* created */);
        final String location = response.getHeader("Location");
        assertTrue(location.startsWith("http://example.org/registry/"));
        assertFalse(location.endsWith("/"));
        final Map<String, String> entry = controller.getEntry(
                location.substring(location.lastIndexOf("/")+1),
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://example.org/example/foo");
    }

    @Test
    public void shouldFailToRegisterEntryWithSameHref() throws Exception {
        // given
        final RegistryController controller = registryController();
        MockHttpServletResponse response = postEntry(controller, "http://example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        response = postEntry(controller, "http://example.org/example/foo");
        // then
        assertEquals(response.getStatus(), SC_CONFLICT);
        final Map<String, String> entry = controller.getEntry(
                location.substring(location.lastIndexOf("/")+1),
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://example.org/example/foo");
    }

    @Test
    public void shouldUpdateExistingEntry() throws Exception {
        // given
        final RegistryController controller = registryController();
        final MockHttpServletResponse response = postEntry(controller, "http://example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        final MockHttpServletResponse updateResponse = putEntry(controller, location, "http://example.org/example/bar");
        // then
        assertEquals(updateResponse.getStatus(), SC_NO_CONTENT);
        final Map<String, String> entry = controller.getEntry(
                location.substring(location.lastIndexOf("/")+1),
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://example.org/example/bar");
    }

    @Test
    public void shouldCreateEntryWithGivenId() throws Exception {
        // given
        final RegistryController controller = registryController();
        // when
        final MockHttpServletResponse response = putEntry(controller, "http://example.org/registry/42", "http://example.org/example/bar");
        // then
        assertEquals(response.getStatus(), SC_CREATED);
        final Map<String, String> entry = controller.getEntry(
                "42",
                new MockHttpServletResponse());
        assertEquals(entry.get("href"), "http://example.org/example/bar");
    }

    @Test
    public void shouldDeleteEntry() throws Exception {
        // given
        final RegistryController controller = registryController();
        MockHttpServletResponse response = postEntry(controller, "http://example.org/example/foo");
        final String location = response.getHeader("Location");
        // when
        controller.unregister(
                location.substring(location.lastIndexOf("/")+1),
                new MockHttpServletResponse());
        // then
        final MockHttpServletResponse secondResponse = new MockHttpServletResponse();
        final Map<String, String> entry = controller.getEntry(
                location.substring(location.lastIndexOf("/")+1),
                secondResponse);

        assertNull(entry);
        assertEquals(secondResponse.getStatus(), SC_NOT_FOUND);
    }

    private MockHttpServletResponse postEntry(final RegistryController controller,
                                              final String href) throws IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("http://example.org/registry");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        controller.create(buildEntry(href), response);
        return response;
    }

    private MockHttpServletResponse putEntry(final RegistryController controller,
                                             final String uri,
                                             final String href) throws IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uri);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        controller.createOrUpdate(
                uri.substring(uri.lastIndexOf("/")+1),
                buildEntry(href),
                response);
        return response;
    }

    private Map<String, String> buildEntry(final String href) {
        final Map<String, String> entry = new HashMap<String, String>();
        entry.put("href", href);
        entry.put("title", "A Foo");
        return entry;
    }

    /**
     * Factory method used to create a JsonHomeRegistryController instance.
     * @return JsonHomeRegistryController
     */
    private RegistryController registryController() {
        try {
            File file = new File(System.getProperty("user.home") + "/jsonhome/jsonhome" + UUID.randomUUID() + ".registry");
            file.deleteOnExit();
            final RegistryController controller = new RegistryController();
            controller.setApplicationBaseUri("http://example.org/registry");
            controller.setRegistry(new FileSystemRegistry(file));
            return controller;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
