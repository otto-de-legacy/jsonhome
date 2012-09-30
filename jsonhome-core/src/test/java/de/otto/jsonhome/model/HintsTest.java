package de.otto.jsonhome.model;

import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 30.09.12
 */
public class HintsTest {

    @Test
    public void testToJson() throws Exception {
        // given
        final List<String> allows = asList("GET, PUT");
        final List<String> representations = asList("text/html", "text/plain");
        final Hints hints = new Hints(allows, representations);
        // when
        final Map<String, ?> map = hints.toJson();
        // then
        assertEquals(map.get("allow"), allows);
        assertEquals(map.get("representations"), representations);
    }

    @Test
    public void testMergeWith() {
        // given
        final Hints firstHints = new Hints(asList("GET", "PUT"), asList("text/html", "text/plain"));
        final Hints secondHints = new Hints(asList("GET", "DELETE"), asList("text/html", "application/json"));
        // when
        final Hints merged = firstHints.mergeWith(secondHints);
        // then
        assertEquals(merged.getAllows(), asList("GET", "PUT", "DELETE"));
        assertEquals(merged.getRepresentations(), asList("text/html", "text/plain", "application/json"));
    }
}
