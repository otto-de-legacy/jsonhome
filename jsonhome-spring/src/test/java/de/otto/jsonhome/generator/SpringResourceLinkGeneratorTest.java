package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.Href;
import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.Allow;
import de.otto.jsonhome.model.ResourceLink;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.HintsBuilder.hints;
import static java.util.Collections.singletonList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 21.10.12
 */
public class SpringResourceLinkGeneratorTest {

    public static final URI BASE_URI = URI.create("http://example.org");

    private static  @Controller class ControllerWithDirectResourceLink {
        @RequestMapping("/foo") @Rel("http://example.org/rel/bar")
        public void getSomething() {}
    }

    private static  @Controller @Rel("http://example.org/rel/bar")
    class ControllerWithOverriddenFullyQualifiedResourceLink {
        @RequestMapping("/foo1")  @Href("http://example.org/bar")
        public void getSomething() {}
        @RequestMapping("/foo2") @Href("/bar")
        public void getSomethingElse() {}
    }

    @Test
    public void shouldUseHrefUriFromSpringAnnotations() throws Exception {

        // given
        final SpringResourceLinkGenerator generator = new SpringResourceLinkGenerator(BASE_URI, BASE_URI);
        final Method method = ControllerWithDirectResourceLink.class.getMethod("getSomething");
        // when
        final boolean isCandidateForAnalysis = generator.isCandidateForAnalysis(method);
        final List<ResourceLink> resourceLinks = generator.resourceLinksFor(method);
        // then
        assertTrue(isCandidateForAnalysis);
        assertEquals(resourceLinks, singletonList(directLink(
                BASE_URI.resolve("/rel/bar"),
                BASE_URI.resolve("/foo"),
                hints()
                        .representedAs("text/html")
                        .allowing(Allow.GET)
                        .build()))
        );
    }

    @Test
    public void shouldOverrideHrefUriUsingHrefAnnotation() throws Exception {
        // given
        final SpringResourceLinkGenerator generator = new SpringResourceLinkGenerator(BASE_URI, BASE_URI);
        final Method method = ControllerWithOverriddenFullyQualifiedResourceLink.class.getMethod("getSomething");
        // when
        final boolean isCandidateForAnalysis = generator.isCandidateForAnalysis(method);
        final List<ResourceLink> resourceLinks = generator.resourceLinksFor(method);
        // then
        assertTrue(isCandidateForAnalysis);
        assertEquals(resourceLinks, singletonList(directLink(
                BASE_URI.resolve("/rel/bar"),
                BASE_URI.resolve("/bar"),
                hints()
                        .representedAs("text/html")
                        .allowing(Allow.GET)
                        .build()))
        );
    }

    @Test
    public void shouldOverrideHrefUriUsingRelativeHrefAnnotation() throws Exception {
        // given
        final SpringResourceLinkGenerator generator = new SpringResourceLinkGenerator(BASE_URI, BASE_URI);
        final Method method = ControllerWithOverriddenFullyQualifiedResourceLink.class.getMethod("getSomethingElse");
        // when
        final boolean isCandidateForAnalysis = generator.isCandidateForAnalysis(method);
        final List<ResourceLink> resourceLinks = generator.resourceLinksFor(method);
        // then
        assertTrue(isCandidateForAnalysis);
        assertEquals(resourceLinks, singletonList(directLink(
                BASE_URI.resolve("/rel/bar"),
                BASE_URI.resolve("/bar"),
                hints()
                        .representedAs("text/html")
                        .allowing(Allow.GET)
                        .build()))
        );
    }

}
