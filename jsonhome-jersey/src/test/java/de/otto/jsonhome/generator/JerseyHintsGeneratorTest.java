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

package de.otto.jsonhome.generator;


import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.Allow;
import de.otto.jsonhome.model.Hints;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import static de.otto.jsonhome.fixtures.ResourceFixtures.ResourceWithHints;
import static de.otto.jsonhome.model.Authentication.authReq;
import static de.otto.jsonhome.model.Precondition.ETAG;
import static de.otto.jsonhome.model.Precondition.LAST_MODIFIED;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

public class JerseyHintsGeneratorTest {

    public static final URI RELATION_TYPE_BASE_URI = create("http://example.org");

    class ResourceWithDifferentProducesAndConsumes {

        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Produces({MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
        @Rel("/rel/product/form")
        public void postSomething() {}
    }

    @Test
    public void testMethodWithProducesAndConsumes() throws Exception {
        // given
        final JerseyHintsGenerator generator = new JerseyHintsGenerator(RELATION_TYPE_BASE_URI, null);
        final Class<?> resource = ResourceWithDifferentProducesAndConsumes.class;
        // when
        final Hints hints = generator.hintsOf(
                create("/rel/product/form"),
                resource.getMethod("postSomething"));
        // then
        assertEquals(hints.getAllows(), EnumSet.of(Allow.POST));
        assertEquals(hints.getRepresentations(), Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN));
        assertEquals(hints.getAcceptPost(), Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED));
    }

    class ResourceWithOnlyPut {

        @PUT
        @Rel("/rel/product/add")
        public void putSomething() {}
    }

    @Test
    public void testMethodWithHttpMethodOnly() throws Exception {
        // given
        final JerseyHintsGenerator generator = new JerseyHintsGenerator(RELATION_TYPE_BASE_URI, null);
        final Class<?> resource = ResourceWithOnlyPut.class;
        // when
        final Hints hints = generator.hintsOf(
                create("/rel/product/add"),
                resource.getMethod("putSomething"));
        // then
        assertEquals(hints.getAllows(), EnumSet.of(Allow.PUT));
        assertEquals(hints.getRepresentations(), Collections.<String>emptyList());
        assertEquals(hints.getAcceptPut(), Collections.<String>emptyList());
    }

    @Test
    public void shouldFindRequiredPrecondition() throws NoSuchMethodException {
        // given
        final JerseyHintsGenerator generator = new JerseyHintsGenerator(RELATION_TYPE_BASE_URI, null);
        final Class<?> controller = ResourceWithHints.class;
        // when
        final Hints hints = generator.hintsOf(
                create("/rel/bar"),
                controller.getMethod("methodWithPreconditionsRequired"));
        // then
        assertEquals(hints.getPreconditionReq(), asList(ETAG, LAST_MODIFIED));
    }

    @Test
    public void shouldFindRequiredAuthentication() throws NoSuchMethodException {
        // given
        final JerseyHintsGenerator generator = new JerseyHintsGenerator(RELATION_TYPE_BASE_URI, null);
        final Class<?> controller = ResourceWithHints.class;
        // when
        final Hints hints = generator.hintsOf(
                create("/rel/bar"),
                controller.getMethod("methodWithAuthRequired"));
        // then
        assertEquals(hints.getAuthReq(), asList(authReq("Basic", asList("foo")), authReq("Digest", asList("bar"))));
    }

}
