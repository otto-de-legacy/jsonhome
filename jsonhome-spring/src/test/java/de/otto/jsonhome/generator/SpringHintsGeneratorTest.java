/*
 * *
 *  Copyright 2012 Guido Steinacker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.Authentication;
import de.otto.jsonhome.model.Hints;
import org.testng.annotations.Test;

import java.net.URI;

import static de.otto.jsonhome.fixtures.ControllerFixtures.ControllerWithDifferentProducesAndConsumes;
import static de.otto.jsonhome.fixtures.ControllerFixtures.ControllerWithHints;
import static de.otto.jsonhome.model.Authentication.authReq;
import static de.otto.jsonhome.model.Precondition.ETAG;
import static de.otto.jsonhome.model.Precondition.LAST_MODIFIED;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * @author Guido Steinacker
 * @since 20.10.12
 */
public class SpringHintsGeneratorTest {

    public static final URI RELATION_TYPE_BASE_URI = create("http://example.org");

    @Test
    public void testMethodWithTwoRepresentations() throws Exception {
        // given
        final SpringHintsGenerator generator = new SpringHintsGenerator(RELATION_TYPE_BASE_URI);
        final Class<?> controller = ControllerWithDifferentProducesAndConsumes.class;
        // when
        final Hints hints = generator.hintsOf(
                create("/rel/product/form"),
                controller.getMethod("postSomething", String.class));
        // then
        assertEquals(hints.getRepresentations(), asList("text/html"));
        assertEquals(hints.getAcceptPost(), asList("application/x-www-form-urlencoded"));
    }

    @Test
    public void shouldFindRequiredPrecondition() throws NoSuchMethodException {
        // given
        final SpringHintsGenerator generator = new SpringHintsGenerator(RELATION_TYPE_BASE_URI);
        final Class<?> controller = ControllerWithHints.class;
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
        final SpringHintsGenerator generator = new SpringHintsGenerator(RELATION_TYPE_BASE_URI);
        final Class<?> controller = ControllerWithHints.class;
        // when
        final Hints hints = generator.hintsOf(
                create("/rel/bar"),
                controller.getMethod("methodWithAuthRequired"));
        // then
        assertEquals(hints.getAuthReq(), asList(authReq("Basic", asList("foo")), authReq("Digest", asList("bar"))));
    }
}
