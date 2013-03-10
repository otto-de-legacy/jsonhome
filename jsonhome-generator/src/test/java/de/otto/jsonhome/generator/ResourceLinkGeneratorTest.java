package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.Href;
import de.otto.jsonhome.annotation.HrefTemplate;
import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.Allow;
import de.otto.jsonhome.model.ResourceLink;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Set;

import static de.otto.jsonhome.model.Allow.GET;
import static java.util.Collections.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Guido Steinacker
 * @since 13.01.13
 */
public class ResourceLinkGeneratorTest {

    public static final URI APPLICATION_BASE_URI = URI.create("http://app.example.org");
    public static final URI RELATION_TYPE_BASE_URI = URI.create("http://spec.example.org");
    public static final URI VAR_TYPE_BASE_URI = URI.create("http://spec.example.org/vartypes");

    static class TestResourceLinkGenerator extends ResourceLinkGenerator {

        TestResourceLinkGenerator(final URI baseVarTypeUri) {
            super(APPLICATION_BASE_URI,
                    RELATION_TYPE_BASE_URI,
                    baseVarTypeUri,
                    new HintsGenerator(RELATION_TYPE_BASE_URI, null) {
                        @Override
                        protected Set<Allow> allowedHttpMethodsOf(final Method method) {
                            return singleton(GET);
                        }

                        @Override
                        protected List<String> producedRepresentationsOf(final Method method) {
                            return singletonList("text/plain");
                        }

                        @Override
                        protected List<String> consumedRepresentationsOf(final Method method) {
                            return emptyList();
                        }
                    },
                    new HrefVarsGenerator(RELATION_TYPE_BASE_URI, null) {
                        @Override
                        protected boolean hasRequestParam(final ParameterInfo parameterInfo) {
                            return true;
                        }

                        @Override
                        protected boolean hasPathVariable(final ParameterInfo parameterInfo) {
                            return true;
                        }
                    });
        }

        public TestResourceLinkGenerator() {
            this(null);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected boolean isCandidateForAnalysis(Method method) {
            return true;
        }

        @Override
        protected String resourcePathFor(Method method) {
            if (method.getName().startsWith("direct")) {
                return APPLICATION_BASE_URI + "/foo";
            } else {
                return APPLICATION_BASE_URI + "/foo/{bar}";
            }
        }
    }

    @Rel("/rel/test")
    static class TestController {
        public void directLink() {}
        public void templatedLink(final String bar) {}
        public void templatedLink(final String bar, final String foobar) {}
        @Href("http://test.example.org/foo/bar")
        public void annotatedDirectLink() {}
        @HrefTemplate("/bar/{foo}")
        public void templatedLinkWithHrefTemplate(final String bar) {}
    }

    @Test
    public void resourceLinkForDirectLink() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final ResourceLink resourceLink = generator.resourceLinkFor(TestController.class.getMethod("directLink"));
        // then:
        assertEquals(resourceLink.asDirectLink().getHref().toString(), APPLICATION_BASE_URI + "/foo");
    }

    @Test
    public void resourceLinkForAnnotatedDirectLink() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final ResourceLink resourceLink = generator.resourceLinkFor(TestController.class.getMethod("annotatedDirectLink"));
        // then:
        assertEquals(resourceLink.asDirectLink().getHref().toString(), "http://test.example.org/foo/bar");
    }

    @Test
    public void resourceLinkForTemplatedLink() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final ResourceLink resourceLink = generator.resourceLinkFor(TestController.class.getMethod("templatedLink", String.class));
        // then:
        final String expectedTemplate = APPLICATION_BASE_URI + "/foo/{bar}";
        assertEquals(resourceLink.asTemplatedLink().getHrefTemplate(), expectedTemplate);
    }

    @Test
    public void varTypeForTemplatedLinkWithDefaultVarTypeUri() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final ResourceLink resourceLink = generator.resourceLinkFor(TestController.class.getMethod("templatedLink", String.class));
        // then:
        final URI expectedVarType = URI.create(RELATION_TYPE_BASE_URI + "/rel/test#bar");
        assertNotNull(resourceLink.asTemplatedLink().getHrefVar(expectedVarType));
    }

    @Test
    public void varTypeForTemplatedLinkWithBaseVarTypeUri() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator(VAR_TYPE_BASE_URI);
        // when:
        final ResourceLink resourceLink = generator.resourceLinkFor(TestController.class.getMethod("templatedLink", String.class));
        // then:
        final URI expectedVarType = URI.create(VAR_TYPE_BASE_URI.toString() + "/bar");
        assertNotNull(resourceLink.asTemplatedLink().getHrefVar(expectedVarType));
    }

    @Test
    public void resourceLinkForAnnotatedTemplatedLink() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final ResourceLink resourceLink = generator.resourceLinkFor(TestController.class.getMethod("templatedLinkWithHrefTemplate", String.class));
        // then:
        final String expectedTemplate = APPLICATION_BASE_URI + "/bar/{foo}";
        assertEquals(resourceLink.asTemplatedLink().getHrefTemplate(), expectedTemplate);
    }

    @Test
    public void queryTemplateForNoParam() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final String query = generator.queryTemplateFrom(TestController.class.getMethod("directLink"));
        // then:
        assertEquals(query, "");

    }

    @Test
    public void queryTemplateForSingleParam() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final String query = generator.queryTemplateFrom(TestController.class.getMethod("templatedLink", String.class));
        // then:
        assertEquals(query, "{?bar}");
    }

    @Test
    public void queryTemplateForDoubleParam() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final String query = generator.queryTemplateFrom(TestController.class.getMethod("templatedLink", String.class, String.class));
        // then:
        assertEquals(query, "{?bar,foobar}");

    }

    @Test
    public void testRelationTypeFrom() throws Exception {
        // given:
        ResourceLinkGenerator generator = new TestResourceLinkGenerator();
        // when:
        final URI rel = generator.relationTypeFrom(TestController.class.getMethod("directLink"));
        // then:
        assertEquals(rel, URI.create(RELATION_TYPE_BASE_URI + "/rel/test"));
    }

}
