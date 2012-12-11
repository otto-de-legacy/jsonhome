package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.*;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.*;

import static de.otto.jsonhome.fixtures.ResourceFixtures.*;
import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static de.otto.jsonhome.model.Precondition.ETAG;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.of;
import static org.testng.Assert.*;

public class JerseyJsonHomeGeneratorTest {

    public static final URI ROOT_URI = create("http://example.org");

    @Test
    public void controllerWithoutResourceMappingShouldNotHaveResourceLinks() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ResourceWithoutResource.class).generate();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertTrue(resourceLinks.isEmpty());
    }

    @Test
    public void controllerWithoutMethodsShouldNotHaveResourceLinks() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ResourceWithoutMethods.class).generate();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertTrue(resourceLinks.isEmpty());
    }

    @Test
    public void controllerWithResourceWithoutLinkRelationTypeShouldNotHaveResourceLink() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ResourceWithResourceWithoutLinkRelationType.class)
                .generate();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 0);
    }

    @Test
    public void shouldCombineRepresentationsFromMultipleMethods() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ResourceWithDifferentRepresentations.class)
                .generate();
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
                .with(ResourceWithAllowsSpecAcrossMultipleMethods.class)
                .generate();
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
                .with(ResourceWithGetAndPostMethodWithDefaultAllowsSpec.class)
                .generate();
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
                .with(ResourceWithDifferentAllowsSpecifications.class)
                .generate();
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
                .with(ResourceWithDifferentResourceDefinitions.class)
                .generate();
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
    public void shouldKeepUseBaseUriAsRelationTypePrefix() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ResourceWithRelativeLinkRelationType.class)
                .generate();
        // when
        final Set<URI> resourceLinks = jsonHome.getResources().keySet();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(resourceLinks.iterator().next().toString(), "http://example.org/rel/fooType");
    }

    @Test
    public void shouldKeepUseRootRelUriAsRelationTypePrefix() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"), create("http://otto.de"))
                .with(ResourceWithRelativeLinkRelationType.class)
                .generate();
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
                .with(ResourceWithDifferentUrisForSameRelationType.class)
                .generate();
        // when
        jsonHome.getResources();
        // then an exception is thrown
    }

    @Test
    public void controllerWithResourceAndDirectLinkRelationTypeShouldHaveDirectResourceLink() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ResourceWithResourceAndLinkRelationType.class)
                .generate();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        final ResourceLink link = firstFrom(resourceLinks);
        assertTrue(link.isDirectLink());
        assertEquals(link.getLinkRelationType(), create("http://example.org/rel/fooBarType"));
    }

    @Test
    public void methodShouldDefineLinkRelationTypeIfResourceHasNoAnnotation() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ResourceWithRequestMappingAndLinkRelationTypeAtMethodLevel.class)
                .generate();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(firstFrom(resourceLinks).getLinkRelationType(), create("http://example.org/rel/foo"));
    }

    @Test
    public void methodShouldInheritLinkRelationTypeFromResource() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org"))
                .with(ResourceWithRequestMappingAndLinkRelationTypeAtClassLevel.class)
                .generate();
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
                .with(ResourceWithMultipleLinkRelationTypes.class)
                .generate();
        // when
        final Collection<ResourceLink> resourceLinks = jsonHome.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 2);
        assertTrue(resourceLinks.containsAll(asList(
                directLink(create("http://example.org/rel/foo"), create("http://example.org/foo"), Hints.hints(of(GET), singletonList("text/html"))),
                directLink(create("http://example.org/rel/bar"), create("http://example.org/foo"), Hints.hints(of(GET), singletonList("text/html")))
        )));
    }

    @Test
    public void mustNotHaveMultipleResourceLinksIfResourceIsImplementedInDifferentResources() {
        // given
        @SuppressWarnings("unchecked")
        final List<Class<?>> controller = Arrays.asList(
                ResourceWithRequestMappingAndLinkRelationTypeAtClassLevel.class,
                AnotherResourceWithRequestMappingAndLinkRelationTypeAtClassLevel.class
        );
        final JsonHome foo = jsonHomeFor(create("http://example.org")).with(controller).generate();
        // when
        final Collection<ResourceLink> resourceLinks = foo.getResources().values();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertTrue(firstFrom(resourceLinks).getHints().getAllows().containsAll(of(GET, PUT)));
    }

    @Test
    public void shouldHaveAcceptPostHintIfPostSupportsDifferentMediaTypeThangenerate() {
        // given
        final JsonHome foo = jsonHomeFor(ROOT_URI).with(ResourceWithAcceptPutAndAcceptPost.class).generate();
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
    public void resourceWithRequiredPreconditionShouldHaveHintWithPreconditionReq() {
        //
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ResourceWithRequiredPrecondition.class).generate();
        // when
        final Hints hints = jsonHome.getResourceFor(create("http://example.org/rel/foo")).getHints();
        // then
        assertFalse(hints.getPreconditionReq().isEmpty());
        assertEquals(hints.getPreconditionReq(), asList(ETAG));
    }

    @Test
    public void templatedResourceLinkShouldHavePathAndQueryVar() {
        // given
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ResourceWithTemplatedResourceLink.class).generate();
        // when
        final TemplatedLink templatedLink = jsonHome.getResourceFor(create(ROOT_URI + "/rel/foo")).asTemplatedLink();
        // then
        assertEquals(templatedLink.getHrefTemplate(), ROOT_URI + "/{bar}{?query}");
        assertEquals(templatedLink.getHrefVars().size(), 2);
        assertEquals(templatedLink.getHrefVars().get(0), hrefVar(
                "bar", create(ROOT_URI + "/rel/foo#bar"))
        );
        assertEquals(templatedLink.getHrefVars().get(1),
                hrefVar("query", create(ROOT_URI + "/rel/foo#query"))
        );
    }

    @Test
    public void templatedResourceLinkShouldHavePathAndQueryVars() {
        // given
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ResourceWithTemplatedResourceLink.class).generate();
        // when
        final TemplatedLink templatedLink = jsonHome.getResourceFor(create(ROOT_URI + "/rel/foobar")).asTemplatedLink();
        // then
        assertEquals(templatedLink.getHrefTemplate(), ROOT_URI + "/{bar}/{foobar}{?query,page}");
        assertEquals(templatedLink.getHrefVars().size(), 4);
        assertEquals(templatedLink.getHrefVars().get(0),
                hrefVar("bar", create(ROOT_URI + "/rel/foobar#bar"))
        );
        assertEquals(templatedLink.getHrefVars().get(1),
                hrefVar("foobar", create(ROOT_URI + "/rel/foobar#foobar"))
        );
        assertEquals(templatedLink.getHrefVars().get(2),
                hrefVar("query", create(ROOT_URI + "/rel/foobar#query"))
        );
        assertEquals(templatedLink.getHrefVars().get(3),
                hrefVar("page", create(ROOT_URI + "/rel/foobar#page"))
        );
    }

    @Test
    public void shouldFindDocumentationAndOverrideDocFromResource() {
        // given
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ResourceWithDocumentation.class).generate();
        final URI relationTypeURI = create(ROOT_URI.toString() + "/rel/bar");
        // when
        final ResourceLink resourceLink = jsonHome.getResourceFor(relationTypeURI);
        // then
        final Documentation docs = resourceLink.getHints().getDocs();
        assertEquals(docs.getDescription().get(0), "a value");
        final Documentation var1Doc = resourceLink.asTemplatedLink().getHrefVars().get(0).getDocs();
        assertEquals(var1Doc.getDescription().get(0), "var value 1");
        final Documentation var2Doc = resourceLink.asTemplatedLink().getHrefVars().get(1).getDocs();
        assertEquals(var2Doc.getDescription().get(0), "var value 2");
    }

    @Test
    public void shouldFindDocumentationAndInheritDocFromResource() {
        // given
        final JsonHome jsonHome = jsonHomeFor(ROOT_URI).with(ResourceWithDocumentation.class).generate();
        final URI relationTypeURI = create(ROOT_URI.toString() + "/rel/foo");
        // when
        final ResourceLink resourceLink = jsonHome.getResourceFor(relationTypeURI);
        // then
        final Documentation docs = resourceLink.getHints().getDocs();
        assertEquals(docs.getDescription().get(0), "controller value");
        final Documentation var1Doc = resourceLink.asTemplatedLink().getHrefVars().get(0).getDocs();
        assertEquals(var1Doc.getDescription().get(0), "var value 1");
        final Documentation var2Doc = resourceLink.asTemplatedLink().getHrefVars().get(1).getDocs();
        assertEquals(var2Doc.getDescription().get(0), "var value 2");
    }

    private JsonHomeGenerator jsonHomeFor(final URI applicationBaseUri) {
        return new JerseyJsonHomeGenerator(applicationBaseUri, applicationBaseUri);
    }

    private JsonHomeGenerator jsonHomeFor(final URI applicationBaseUri, final URI relationTypeBaseUri) {
        return new JerseyJsonHomeGenerator(applicationBaseUri, relationTypeBaseUri);
    }

    private ResourceLink firstFrom(Collection<ResourceLink> resourceLinks) {
        return resourceLinks.iterator().next();
    }

}
