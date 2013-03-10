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

    // TODO: implement and use harness server
    @Test(enabled = false)
    public void shouldFindRegisteredResource() {
        // given
        final JsonHomeClient client = new HttpJsonHomeClient();
        // when
        final JsonHome jsonHome = client.get(JSONHOME_URI);
        // then
        assertNotNull(jsonHome);
        assertTrue(jsonHome.hasResourceFor(RELATION_TYPE_URI));
    }

    // TODO: implement and use harness server
    // TODO: caching-behaviour is not testable
    @Test(enabled = false)
    public void shouldInvalidateCacheEntry() {
        // given
        final JsonHomeClient client = new HttpJsonHomeClient();
        // when
        client.get(JSONHOME_URI);
        client.get(JSONHOME_URI);
        final JsonHome jsonHome = client.updateAndGet(JSONHOME_URI);
        // then
        assertNotNull(jsonHome);
        assertTrue(jsonHome.hasResourceFor(RELATION_TYPE_URI));
    }

    // TODO: implement and use harness server
    @Test(enabled = false, expectedExceptions = NotFoundException.class)
    public void shouldThrowNotFoundExceptionForUnknownResource() {
        // given
        final JsonHomeClient client = new HttpJsonHomeClient();
        // when
        final JsonHome jsonHome = client.get(URI.create("http://localhost:8080/foo"));
        // then
        assertNull(jsonHome);
    }

    @Test(enabled = false)
    public void shouldGetApplicationJsonFormatIncludingDescription() {
        fail();
    }
}
