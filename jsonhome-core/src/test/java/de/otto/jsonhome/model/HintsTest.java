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
package de.otto.jsonhome.model;

import de.otto.jsonhome.annotation.Status;
import org.testng.annotations.Test;

import java.util.Set;

import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 30.09.12
 */
public class HintsTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void creationShouldFailWithAcceptPostIfPostIsNotAllowed() {
        // given
        final Set<Allow> allows = of(GET, PUT);
        final String acceptPost = "text/plain";
        // when
        hintsBuilder().allowing(allows).representedAs("text/html").acceptingForPost(acceptPost).build();
        // then an exception is thrown
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void creationShouldFailWithAcceptPutIfPutIsNotAllowed() {
        // given
        final Set<Allow> allows = of(GET, POST);
        final String acceptPut = "text/plain";
        // when
        hintsBuilder().allowing(allows).representedAs("text/html").acceptingForPut(acceptPut).build();
        // then an exception is thrown
    }

    @Test
    public void shouldMergeStatusWithHigherOrder() {
        // given
        final Hints first = hintsBuilder().withStatus(Status.OK).build();
        final Hints second = hintsBuilder().withStatus(Status.DEPRECATED).build();
        final Hints third = hintsBuilder().withStatus(Status.GONE).build();
        // when
        final Hints firstWithSecond = first.mergeWith(second);
        final Hints firstWithThird = first.mergeWith(third);
        final Hints secondWithFirst = second.mergeWith(first);
        final Hints secondWithThird = second.mergeWith(third);
        final Hints thirdWithFirst = third.mergeWith(first);
        final Hints thirdWithSecond = third.mergeWith(second);
        final Hints secondWithSecond = second.mergeWith(second);
        // then
        assertEquals(secondWithSecond, second);
        assertEquals(firstWithSecond, secondWithFirst);
        assertEquals(firstWithThird, thirdWithFirst);
        assertEquals(secondWithThird, thirdWithSecond);
        assertEquals(firstWithSecond.getStatus(), Status.DEPRECATED);
        assertEquals(firstWithThird.getStatus(), Status.GONE);
        assertEquals(secondWithThird.getStatus(), Status.GONE);
    }

    @Test
    public void testMergeWith() {
        // given
        final Hints firstHints = hintsBuilder()
                .allowing(of(GET, PUT))
                .representedAs("text/html", "text/plain")
                .acceptingForPut("bar/foo")
                .build();
        final Hints secondHints = hintsBuilder()
                .allowing(of(GET, POST, DELETE))
                .representedAs("text/html", "application/json")
                .acceptingForPost("foo/bar")
                .build();
        // when
        final Hints merged = firstHints.mergeWith(secondHints);
        // then
        assertEquals(merged.getAllows(), of(GET, PUT, POST, DELETE));
        assertEquals(merged.getRepresentations(), asList("text/html", "text/plain", "application/json"));
        assertEquals(merged.getAcceptPut(), asList("bar/foo"));
        assertEquals(merged.getAcceptPost(), asList("foo/bar"));
    }
}
