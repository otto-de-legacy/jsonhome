package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;
import org.testng.annotations.Test;

import java.net.URI;

import static java.net.URI.create;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class HttpJsonHomeClientTest {

    public static final URI RELATION_TYPE_URI = create("http://localhost:8080/jsonhome-example/rel/products");
    public static final URI JSONHOME_URI = create("http://localhost:8080/jsonhome-example/json-home");

    // TODO: harness server
    @Test(enabled = false)
    public void shouldFindRegisteredResource() {
        // given
        final HttpJsonHomeClient client = new HttpJsonHomeClient();
        // when
        final JsonHome jsonHome = client.get(JSONHOME_URI);
        // then
        assertNotNull(jsonHome);
        assertTrue(jsonHome.hasResourceFor(RELATION_TYPE_URI));
    }

    // TODO: harness server
    @Test(enabled = false, expectedExceptions = NotFoundException.class)
    public void shouldThrowNotFoundExceptionForUnknownResource() {
        // given
        final HttpJsonHomeClient client = new HttpJsonHomeClient();
        // when
        final JsonHome jsonHome = client.get(URI.create("http://localhost:8080/foo"));
        // then
        assertNull(jsonHome);
    }
}
