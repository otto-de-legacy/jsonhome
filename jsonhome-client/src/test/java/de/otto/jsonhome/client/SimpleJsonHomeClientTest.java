package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;
import org.testng.annotations.Test;

import java.net.URI;

import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class SimpleJsonHomeClientTest {

    public static final URI RELATION_TYPE_URI = create("http://example.org/rel/widgets");
    public static final URI JSONHOME_URI = create("http://example.org/json-home");
    public static final JsonHome JSON_HOME = jsonHome(asList(
            directLink(RELATION_TYPE_URI, create("http://example.org/widgets"), null))
    );

    @Test
    public void shouldFindRegisteredResource() {
        // given
        final SimpleJsonHomeClient simpleJsonHomeClient = new SimpleJsonHomeClient();
        simpleJsonHomeClient.register(JSONHOME_URI, JSON_HOME);
        // when
        final JsonHome jsonHome = simpleJsonHomeClient.get(JSONHOME_URI);
        // then
        assertNotNull(jsonHome);
        assertTrue(jsonHome.hasResourceFor(RELATION_TYPE_URI));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void shouldReturnNullForUnregisteredResource() {
        // given
        final SimpleJsonHomeClient simpleJsonHomeClient = new SimpleJsonHomeClient();
        // when
        simpleJsonHomeClient.get(JSONHOME_URI);
        // then an exception is thrown
    }
}
