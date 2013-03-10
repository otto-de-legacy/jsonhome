package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.ResourceLink;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.lang.reflect.Method;

import static de.otto.jsonhome.fixtures.ResourceFixtures.ResourceWithResourceAndLinkRelationType;
import static java.net.URI.create;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class JerseyResourceLinkGeneratorTest {

    private static  @Path("/test") class ResourceWithTemplateLinkWithExtension {
        @GET @Path("/foo/{bar}.xml") @Rel("http://example.org/rel/foo/bar")
        public void getSomething() {}
    }

    @Test
    public void testPathsWithLeadingAndTrailingSlashes() throws Exception {
        final ResourceLinkGenerator generator = new JerseyResourceLinkGenerator(
                create("http://example.org"),
                create("http://rel.example.org"),
                null, null
        );
        final String resource = generator.resourcePathFor(
                ResourceWithResourceAndLinkRelationType.class.getMethod("bar")
        );
        assertEquals("http://example.org/foo/bar/", resource);
    }

    @Test
    public void shouldBeTemplateLink() throws Exception {
        // given
        final JerseyResourceLinkGenerator generator = new JerseyResourceLinkGenerator(
                create("http://example.org"),
                create("http://rel.example.org"),
                null, null
        );
        final Method method = ResourceWithTemplateLinkWithExtension.class.getMethod("getSomething");
        // when
        final boolean isCandidateForAnalysis = generator.isCandidateForAnalysis(method);
        final ResourceLink resourceLink = generator.resourceLinkFor(method);
        // then
        assertTrue(isCandidateForAnalysis);
        assertFalse(resourceLink.isDirectLink());
    }
}
