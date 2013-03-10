/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.Href;
import de.otto.jsonhome.annotation.HrefTemplate;
import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.Allow;
import de.otto.jsonhome.model.ResourceLink;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.URI;

import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;
import static java.util.Arrays.asList;
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

    private static  @Controller @Rel("http://example.org/rel/bar")
    class ControllerWithOverriddenRelativeTemplatedResourceLink {
        @RequestMapping("/foo/**")  @HrefTemplate("http://example.org/bar{/var1*}")
        public void getSomething() {}
    }

    @Test
    public void shouldUseHrefUriFromSpringAnnotations() throws Exception {

        // given
        final SpringResourceLinkGenerator generator = new SpringResourceLinkGenerator(BASE_URI, BASE_URI, null, null);
        final Method method = ControllerWithDirectResourceLink.class.getMethod("getSomething");
        // when
        final boolean isCandidateForAnalysis = generator.isCandidateForAnalysis(method);
        final ResourceLink resourceLink = generator.resourceLinkFor(method);
        // then
        assertTrue(isCandidateForAnalysis);
        assertEquals(resourceLink, directLink(
                BASE_URI.resolve("/rel/bar"),
                BASE_URI.resolve("/foo"),
                hintsBuilder()
                        .representedAs("text/html")
                        .allowing(Allow.GET)
                        .build())
        );
    }

    @Test
    public void shouldOverrideHrefUriUsingHrefAnnotation() throws Exception {
        // given
        final SpringResourceLinkGenerator generator = new SpringResourceLinkGenerator(BASE_URI, BASE_URI, null, null);
        final Method method = ControllerWithOverriddenFullyQualifiedResourceLink.class.getMethod("getSomething");
        // when
        final boolean isCandidateForAnalysis = generator.isCandidateForAnalysis(method);
        final ResourceLink resourceLink = generator.resourceLinkFor(method);
        // then
        assertTrue(isCandidateForAnalysis);
        assertEquals(resourceLink, directLink(
                BASE_URI.resolve("/rel/bar"),
                BASE_URI.resolve("/bar"),
                hintsBuilder()
                        .representedAs("text/html")
                        .allowing(Allow.GET)
                        .build())
        );
    }

    @Test
    public void shouldOverrideHrefUriUsingRelativeHrefAnnotation() throws Exception {
        // given
        final SpringResourceLinkGenerator generator = new SpringResourceLinkGenerator(BASE_URI, BASE_URI.normalize(), null, null);
        final Method method = ControllerWithOverriddenFullyQualifiedResourceLink.class.getMethod("getSomethingElse");
        // when
        final boolean isCandidateForAnalysis = generator.isCandidateForAnalysis(method);
        final ResourceLink resourceLink = generator.resourceLinkFor(method);
        // then
        assertTrue(isCandidateForAnalysis);
        assertEquals(resourceLink, directLink(
                BASE_URI.resolve("/rel/bar"),
                BASE_URI.resolve("/bar"),
                hintsBuilder()
                        .representedAs("text/html")
                        .allowing(Allow.GET)
                        .build())
        );
    }

    @Test
    public void shouldOverrideHrefTemplateUsingHrefTemplateAnnotation() throws Exception {
        // given
        final SpringResourceLinkGenerator generator = new SpringResourceLinkGenerator(BASE_URI, BASE_URI, null, null);
        final Method method = ControllerWithOverriddenRelativeTemplatedResourceLink.class.getMethod("getSomething");
        // when
        final boolean isCandidateForAnalysis = generator.isCandidateForAnalysis(method);
        final ResourceLink resourceLink = generator.resourceLinkFor(method);
        // then
        assertTrue(isCandidateForAnalysis);
        assertEquals(resourceLink, templatedLink(
                BASE_URI.resolve("/rel/bar"),
                "http://example.org/bar{/var1*}",
                asList(hrefVar("var1", BASE_URI.resolve("/rel/bar#var1"))),
                hintsBuilder()
                        .representedAs("text/html")
                        .allowing(Allow.GET)
                        .build())
        );
    }
}
