package de.steinacker.jsonhome.generator;

import de.steinacker.jsonhome.model.DirectLink;
import de.steinacker.jsonhome.model.JsonHome;
import de.steinacker.jsonhome.model.ResourceLink;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static de.steinacker.jsonhome.fixtures.ControllerFixtures.*;
import static de.steinacker.jsonhome.generator.JsonHomeGenerator.jsonHomeFor;
import static de.steinacker.jsonhome.model.DirectLink.directLink;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author Guido Steinacker
 * @since 17.09.12
 */
public class LinkRelationTypeTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void jsonHomeWithoutURIShouldThrowNullPointerException() throws Exception {
        // when
        jsonHomeFor(null);
    }

    @Test
    public void controllerWithoutResourceMappingShouldNotHaveResourceLinks() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithoutResource.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertTrue(resourceLinks.isEmpty());
    }

    @Test
    public void controllerWithoutMethodsShouldNotHaveResourceLinks() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithoutMethods.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertTrue(resourceLinks.isEmpty());
    }

    @Test
    public void controllerWithResourceWithoutLinkRelationTypeShouldNotHaveResourceLink() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithResourceWithoutLinkRelationType.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 0);
    }

    @Test
    public void shouldCombineRepresentationsFromMultipleMethods() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithDifferentRepresentations.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        final List<String> expected = asList("application/foo", "text/html", "text/plain", "application/json", "application/bar");
        assertTrue(resourceLinks.get(0).getRepresentations().containsAll(expected));
    }

    @Test
    public void separateMethodsShouldBeCombinedInAllowsSpec() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithAllowsSpecAcrossMultipleMethods.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertTrue(resourceLinks.get(0).getAllows().containsAll(asList("GET", "PUT", "POST", "DELETE")));
    }

    @Test
    public void shouldHaveCorrectAllowsSpec() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithDifferentAllowsSpecifications.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertTrue(resourceLinks.get(0).getAllows().containsAll(asList("GET", "PUT", "POST", "DELETE", "HEAD")));
    }

    @Test
    public void shouldHaveCorrectHref() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithDifferentResourceDefinitions.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 3);
        final List<String> uris = asList(
                ((DirectLink) resourceLinks.get(0)).getHref().toString(),
                ((DirectLink) resourceLinks.get(1)).getHref().toString(),
                ((DirectLink) resourceLinks.get(2)).getHref().toString());
        assertTrue(uris.containsAll(asList("http://example.org/foo", "http://example.org/foo/bar", "http://example.org/foo/foobar")));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailIfMultipleHrefsAreSupported() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithDifferentUrisForSameRelationType.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then an exception is thrown
    }

    @Test
    public void controllerWithResourceAndDirectLinkRelationTypeShouldHaveDirectResourceLink() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithResourceAndLinkRelationType.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        final ResourceLink link = resourceLinks.get(0);
        assertTrue(link.isDirectLink());
        assertEquals(link.getLinkRelationType(), create("http://example.org/rel/fooBarType"));
    }

    @Test
    public void shouldInheritAllowsSpecFromTypeLevel() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithRequestMethodsAtTypeLevel.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(resourceLinks.get(0).getAllows(), asList("GET", "PUT"));
    }

    @Test
    public void shouldOverrideInheritedAllowsSpecFromTypeLevel() throws Exception {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithRequestMethodsAtTypeAndMethodLevel.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(resourceLinks.get(0).getAllows(), asList("GET", "POST"));
    }

    @Test
    public void methodShouldDefineLinkRelationTypeIfControllerHasNoAnnotation() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithRequestMappingAndLinkRelationTypeAtMethodLevel.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(resourceLinks.get(0).getLinkRelationType(), create("http://example.org/rel/foo"));
    }

    @Test
    public void methodShouldInheritLinkRelationTypeFromController() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertEquals(resourceLinks.get(0).getLinkRelationType(), create("http://example.org/rel/foo"));
    }

    @Test
    public void twoMethodsWithDifferentLinkRelationTypesShouldBeDifferentResources() {
        // given
        final JsonHome jsonHome = jsonHomeFor(create("http://example.org")).with(ControllerWithMultipleLinkRelationTypes.class);
        // when
        final List<ResourceLink> resourceLinks = jsonHome.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 2);
        assertTrue(resourceLinks.containsAll(asList(
                directLink(create("http://example.org/rel/foo"), create("http://example.org/foo"), singletonList("GET"), singletonList("text/html")),
                directLink(create("http://example.org/rel/bar"), create("http://example.org/foo"), singletonList("GET"), singletonList("text/html"))
        )));
    }

    @Test
    public void mustNotHaveMultipleResourceLinksIfResourceIsImplementedInDifferentControllers() {
        // given
        final JsonHome foo = jsonHomeFor(create("http://example.org")).with(asList(
                ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class,
                AnotherControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class));
        // when
        final List<ResourceLink> resourceLinks = foo.getResourceLinks();
        // then
        assertEquals(resourceLinks.size(), 1);
        assertTrue(resourceLinks.get(0).getAllows().containsAll(asList("GET", "PUT")));
    }

    @Test(enabled = false)
    public void shouldHaveAcceptPostHintIfPostSupportsDifferentMediaTypeThanGet() {
        // TODO: not yet implemented
    }

    @Test(enabled = false)
    public void shouldHaveAcceptPutHintIfPutSupportsDifferentMediaTypeThanGet() {
        // TODO: not yet implemented
    }

    @Test(enabled = false)
    public void statusHintMustBeDeprecatedIfTheControllerIsAnnotatedAsDeprecated() {
        // TODO not yet implemented
    }

    @Test(enabled = false)
    public void statusHintMustBeDeprecatedIfAllMethodsOfTheResourceAreDeprecated() {
        // TODO not yet implemented
    }
}
