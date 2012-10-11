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
package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.*;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.*;

import static de.otto.jsonhome.fixtures.ControllerFixtures.*;
import static de.otto.jsonhome.generator.JsonHomeGenerator.jsonHomeFor;
import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Documentation.emptyDocumentation;
import static de.otto.jsonhome.model.HrefVarFlags.REQUIRED;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.of;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 17.09.12
 */
public class JsonHomeGeneratorTest {

    public static final URI ROOT_URI = create("http://example.org");

    @Test(expectedExceptions = NullPointerException.class)
    public void jsonHomeWithoutURIShouldThrowNullPointerException() throws Exception {
        // when
        jsonHomeFor(null);
    }

    @Test
    public void controllerWithoutResourceMappingShouldNotHaveResourceLinks() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithoutResource.class).get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertTrue(resourceLinks.isEmpty());
    }

    @Test
    public void controllerWithoutMethodsShouldNotHaveResourceLinks() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithoutMethods.class).get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertTrue(resourceLinks.isEmpty());
    }

    @Test
    public void controllerWithResourceWithoutLinkRelationTypeShouldNotHaveResourceLink() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithResourceWithoutLinkRelationType.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 0);
    }

    @Test
    public void shouldCombineRepresentationsFromMultipleMethods() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithDifferentRepresentations.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        final List<String> expected = asList("application/foo", "text/html", "text/plain", "application/json", "application/bar");
        assertTrue(firstFrom(resourceLinks).getHints().getRepresentations().containsAll(expected));
    }

    @Test
    public void separateMethodsShouldBeCombinedInAllowsSpec() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithAllowsSpecAcrossMultipleMethods.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertTrue(firstFrom(resourceLinks).getHints().getAllows().containsAll(of(GET, PUT, POST, DELETE)));
    }

    @Test
    public void defaultAcceptPostShouldBeFormUrlEncoded() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithGetAndPostMethodWithDefaultAllowsSpec.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        final Hints hints = firstFrom(resourceLinks).getHints();
        assertEquals(hints.getAllows().size(), 2);
        assertTrue(hints.getAllows().containsAll(asList(GET, POST)));
        assertEquals(hints.getRepresentations(), asList("text/html"));
        assertEquals(hints.getAcceptPost().size(), 1);
        assertEquals(hints.getAcceptPost().get(0), "application/x-www-form-urlencoded");
    }

    @Test
    public void shouldHaveCorrectAllowsSpec() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithDifferentAllowsSpecifications.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertTrue(firstFrom(resourceLinks).getHints().getAllows().containsAll(of(GET, PUT, POST, DELETE, HEAD)));
    }

    @Test
    public void shouldHaveCorrectHref() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithDifferentResourceDefinitions.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 3);
        final Iterator<ResourceLink> iterator = resourceLinks.iterator();
        final List<String> uris = asList(
                ((DirectLink) iterator.next()).getHref().toString(),
                ((DirectLink) iterator.next()).getHref().toString(),
                ((DirectLink) iterator.next()).getHref().toString());
        assertTrue(uris.containsAll(asList("http://example.org/foo", "http://example.org/foo/bar", "http://example.org/foo/foobar")));
    }

    @Test
    public void shouldKeepUseRootUriAsRelationTypePrefix() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithRelativeLinkRelationType.class)
                .get();
        // when
        final Set<URI> resourceLinks = jsonHome.getResources().keySet();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(resourceLinks.iterator().next().toString(), "http://example.org/rel/fooType");
    }

    @Test
    public void shouldKeepUseRootRelUriAsRelationTypePrefix() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .withRelationTypeRoot(create("http://otto.de"))
                .with(ControllerWithRelativeLinkRelationType.class)
                .get();
        // when
        final Set<URI> resourceLinks = jsonHome.getResources().keySet();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(resourceLinks.iterator().next().toString(), "http://otto.de/rel/fooType");
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailIfMultipleHrefsAreSupported() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithDifferentUrisForSameRelationType.class)
                .get();
        // when
        jsonHome.getResources();
        // then an exception is thrown
    }

    @Test
    public void controllerWithResourceAndDirectLinkRelationTypeShouldHaveDirectResourceLink() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithResourceAndLinkRelationType.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        final ResourceLink link = firstFrom(resourceLinks);
        assertTrue(link.isDirectLink());
        assertEquals(link.getLinkRelationType(), create("http://example.org/rel/fooBarType"));
    }

    @Test
    public void methodShouldDefineLinkRelationTypeIfControllerHasNoAnnotation() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithRequestMappingAndLinkRelationTypeAtMethodLevel.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(firstFrom(resourceLinks).getLinkRelationType(), create("http://example.org/rel/foo"));
    }

    @Test
    public void methodShouldInheritLinkRelationTypeFromController() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(firstFrom(resourceLinks).getLinkRelationType(), create("http://example.org/rel/foo"));
    }

    @Test
    public void twoMethodsWithDifferentLinkRelationTypesShouldBeDifferentResources() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ControllerWithMultipleLinkRelationTypes.class)
                .get();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 2);
        assertTrue(resourceLinks.containsAll(asList(
                directLink(create("http://example.org/rel/foo"), create("http://example.org/foo"), new Hints(of(GET), singletonList("text/html"))),
                directLink(create("http://example.org/rel/bar"), create("http://example.org/foo"), new Hints(of(GET), singletonList("text/html")))
        )));
    }

    @Test
    public void mustNotHaveMultipleResourceLinksIfResourceIsImplementedInDifferentControllers() {
        // given
        @SuppressWarnings("unchecked")
        final List<Class<?>> controller = Arrays.asList(
                ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class,
                AnotherControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class
        );
        final JsonHome foo = jsonHomeFor(create("http://example.org")).with(controller).get();
        // when
        final Collection<ResourceLink> resourceLinks = foo.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertTrue(firstFrom(resourceLinks).getHints().getAllows().containsAll(of(GET, PUT)));
    }

    @Test
    public void shouldHaveAcceptPostHintIfPostSupportsDifferentMediaTypeThanGet() {
        // given
        final JsonHome foo = jsonHomeFor(ROOT_URI).with(ControllerWithAcceptPutAndAcceptPost.class).get();
        // when
        final Collection<ResourceLink> resourceLinks = foo.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        final ResourceLink resourceLink = firstFrom(resourceLinks);
        assertTrue(resourceLink.getHints().getAllows().containsAll(of(GET, PUT, POST)));
        assertEquals(resourceLink.getHints().getRepresentations(), asList("text/plain"));
        assertEquals(resourceLink.getHints().getAcceptPost(), asList("foo/bar"));
        assertEquals(resourceLink.getHints().getAcceptPut(), asList("bar/foo"));
    }

    @Test
    public void templatedResourceLinkShouldHavePathAndQueryVar() {
        // given
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ControllerWithTemplatedResourceLink.class).get();
        // when
        final TemplatedLink templatedLink = jsonHome.getResourceFor(create(ROOT_URI + "/rel/foo")).asTemplatedLink();
        // then
        assertEquals(templatedLink.getHrefTemplate(), ROOT_URI + "/{bar}{?query}");
        assertEquals(templatedLink.getHrefVars().size(), 2);
        assertEquals(templatedLink.getHrefVars().get(0), new HrefVar(
                "bar", create(ROOT_URI + "/rel/foo#bar"), emptyDocumentation(), of(REQUIRED)
        ));
        assertEquals(templatedLink.getHrefVars().get(1), new HrefVar(
                "query", create(ROOT_URI + "/rel/foo#query"), emptyDocumentation(), of(REQUIRED)
        ));
    }

    @Test
    public void templatedResourceLinkShouldHavePathAndQueryVars() {
        // given
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ControllerWithTemplatedResourceLink.class).get();
        // when
        final TemplatedLink templatedLink = jsonHome.getResourceFor(create(ROOT_URI + "/rel/foobar")).asTemplatedLink();
        // then
        assertEquals(templatedLink.getHrefTemplate(), ROOT_URI + "/{bar}/{foobar}{?query,page}");
        assertEquals(templatedLink.getHrefVars().size(), 4);
        assertEquals(templatedLink.getHrefVars().get(0), new HrefVar(
                "bar", create(ROOT_URI + "/rel/foobar#bar"), emptyDocumentation(), of(REQUIRED)
        ));
        assertEquals(templatedLink.getHrefVars().get(1), new HrefVar(
                "foobar", create(ROOT_URI + "/rel/foobar#foobar"), emptyDocumentation(), of(REQUIRED)
        ));
        assertEquals(templatedLink.getHrefVars().get(2), new HrefVar(
                "query", create(ROOT_URI + "/rel/foobar#query"), emptyDocumentation(), of(REQUIRED)
        ));
        assertEquals(templatedLink.getHrefVars().get(3), new HrefVar(
                "page", create(ROOT_URI + "/rel/foobar#page"), emptyDocumentation(), of(REQUIRED)
        ));
    }

    @Test(enabled = false)
    public void statusHintMustBeDeprecatedIfTheControllerIsAnnotatedAsDeprecated() {
        // TODO not yet implemented
    }

    @Test(enabled = false)
    public void statusHintMustBeDeprecatedIfAllMethodsOfTheResourceAreDeprecated() {
        // TODO not yet implemented
    }

    @Test
    public void shouldFindDocumentationAndOverrideDocFromController() {
        // given
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ControllerWithDocumentation.class).get();
        final URI relationTypeURI = create(ROOT_URI.toString() + "/rel/bar");
        // when
        final ResourceLink resourceLink = jsonHome.getResourceFor(relationTypeURI);
        // then
        final Documentation documentation = resourceLink.getDocumentation();
        assertEquals(documentation.getDescription().get(0), "a value");
        final Documentation var1Doc = resourceLink.asTemplatedLink().getHrefVars().get(0).getDocumentation();
        assertEquals(var1Doc.getDescription().get(0), "var value 1");
        final Documentation var2Doc = resourceLink.asTemplatedLink().getHrefVars().get(1).getDocumentation();
        assertEquals(var2Doc.getDescription().get(0), "var value 2");
    }

    @Test
    public void shouldFindDocumentationAndInheritDocFromController() {
        // given
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ControllerWithDocumentation.class).get();
        final URI relationTypeURI = create(ROOT_URI.toString() + "/rel/foo");
        // when
        final ResourceLink resourceLink = jsonHome.getResourceFor(relationTypeURI);
        // then
        final Documentation documentation = resourceLink.getDocumentation();
        assertEquals(documentation.getDescription().get(0), "controller value");
        final Documentation var1Doc = resourceLink.asTemplatedLink().getHrefVars().get(0).getDocumentation();
        assertEquals(var1Doc.getDescription().get(0), "var value 1");
        final Documentation var2Doc = resourceLink.asTemplatedLink().getHrefVars().get(1).getDocumentation();
        assertEquals(var2Doc.getDescription().get(0), "var value 2");
    }

    private ResourceLink firstFrom(Collection<ResourceLink> resourceLinks) {
        return resourceLinks.iterator().next();
    }
}
