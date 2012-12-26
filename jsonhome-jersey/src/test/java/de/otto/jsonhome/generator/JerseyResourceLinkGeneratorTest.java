package de.otto.jsonhome.generator;

import org.testng.annotations.Test;

import static de.otto.jsonhome.fixtures.ResourceFixtures.ResourceWithResourceAndLinkRelationType;
import static java.net.URI.create;
import static org.testng.AssertJUnit.assertEquals;

public class JerseyResourceLinkGeneratorTest {

    @Test
    public void testPathsWithLeadingAndTrailingSlashes() throws Exception {
        final ResourceLinkGenerator generator = new JerseyResourceLinkGenerator(
                create("http://example.org"),
                create("http://rel.example.org")
        );
        final String resource = generator.resourcePathFor(
                ResourceWithResourceAndLinkRelationType.class.getMethod("bar")
        );
        assertEquals("http://example.org/foo/bar/", resource);
    }
}
