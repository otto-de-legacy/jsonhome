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

import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.Hints;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.testng.annotations.Test;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 20.10.12
 */
public class SpringHintsGeneratorTest {

    class ControllerWithDifferentProducesAndConsumes {

        @RequestMapping(
                method = RequestMethod.POST,
                consumes = "application/x-www-form-urlencoded",
                produces = "text/html"
        )
        @Rel("/rel/product/form")
        public void postSomething(final String foo) {}
    }

    @Test
    public void testMethodWithTwoRepresentations() throws Exception {
        // given
        final SpringHintsGenerator generator = new SpringHintsGenerator();
        final Class<?> controller = ControllerWithDifferentProducesAndConsumes.class;
        // when
        final Hints hints = generator.hintsOf(controller.getMethod("postSomething", String.class));
        // then
        assertEquals(hints.getRepresentations(), asList("text/html"));
        assertEquals(hints.getAcceptPost(), asList("application/x-www-form-urlencoded"));
    }
}
