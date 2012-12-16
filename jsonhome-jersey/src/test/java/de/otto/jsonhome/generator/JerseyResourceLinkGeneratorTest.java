package de.otto.jsonhome.generator;

import de.otto.jsonhome.fixtures.ResourceFixtures;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class JerseyResourceLinkGeneratorTest {

    @Test
    public void testPathsWithLeadingAndTrailingSlashes() throws Exception {
        ResourceLinkGenerator generator = new JerseyResourceLinkGenerator(
            URI.create("http://example.org"), URI.create("http://rel.example.org"));
        List<String> resources = generator.
                resourcePathsFor(ResourceFixtures.ResourceWithResourceAndLinkRelationType.class.getMethod("bar"));
        assertEquals(1, resources.size());
        assertEquals("http://example.org/foo/bar/", resources.get(0));
    }
}
