package de.otto.jsonhome.registry.controller;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Guido Steinacker
 * @since 10.01.13
 */
class RegistryClientHelper {

    public static MockHttpServletResponse postEntry(final RegistriesController controller,
                                              final String href) throws IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("http://www.example.org/registries/foo");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        controller.register("foo", buildEntry(href), response);
        return response;
    }

    public static MockHttpServletResponse putEntry(final RegistriesController controller,
                                             final String uri,
                                             final String href) throws IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uri);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        controller.registerOrUpdate(
                "foo",
                uri.substring(uri.lastIndexOf("/") + 1),
                buildEntry(href),
                response);
        return response;
    }

    public static Map<String, String> buildEntry(final String href) {
        final Map<String, String> entry = new HashMap<String, String>();
        entry.put("href", href);
        entry.put("title", "A Foo");
        return entry;
    }
}
