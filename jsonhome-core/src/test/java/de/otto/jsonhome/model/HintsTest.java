package de.otto.jsonhome.model;

import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static de.otto.jsonhome.model.HintsBuilder.hints;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 30.09.12
 */
public class HintsTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void creationShouldFailWithAcceptPostIfPostIsNotAllowed() {
        // given
        final List<String> allows = asList("GET", "PUT");
        final String acceptPost = "text/plain";
        // when
        hints().allowing(allows).representedAs("text/html").acceptingForPost(acceptPost).build();
        // then an exception is thrown
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void creationShouldFailWithAcceptPutIfPutIsNotAllowed() {
        // given
        final List<String> allows = asList("GET", "POST");
        final String acceptPut = "text/plain";
        // when
        hints().allowing(allows).representedAs("text/html").acceptingForPut(acceptPut).build();
        // then an exception is thrown
    }

    @Test
    public void testToJson() throws Exception {
        // given
        final List<String> allows = asList("GET", "POST", "PUT");
        final List<String> representations = asList("text/html", "text/plain");
        final List<String> acceptPut = asList("foo/bar");
        final List<String> acceptPost = asList("bar/foo");
        final Hints hints = hints()
                .allowing(allows)
                .representedAs(representations)
                .acceptingForPut(acceptPut)
                .acceptingForPost(acceptPost)
                .build();
        // when
        final Map<String, ?> map = hints.toJson();
        // then
        assertEquals(map.get("allow"), allows);
        assertEquals(map.get("representations"), representations);
        assertEquals(map.get("accept-put"), acceptPut);
        assertEquals(map.get("accept-post"), acceptPost);
    }

    @Test
    public void testMergeWith() {
        // given
        final Hints firstHints = hints()
                .allowing("GET", "PUT")
                .representedAs("text/html", "text/plain")
                .acceptingForPut("bar/foo")
                .build();
        final Hints secondHints = hints()
                .allowing("GET", "POST", "DELETE")
                .representedAs("text/html", "application/json")
                .acceptingForPost("foo/bar")
                .build();
        // when
        final Hints merged = firstHints.mergeWith(secondHints);
        // then
        assertEquals(merged.getAllows(), asList("GET", "PUT", "POST", "DELETE"));
        assertEquals(merged.getRepresentations(), asList("text/html", "text/plain", "application/json"));
        assertEquals(merged.getAcceptPut(), asList("bar/foo"));
        assertEquals(merged.getAcceptPost(), asList("foo/bar"));
    }
}
