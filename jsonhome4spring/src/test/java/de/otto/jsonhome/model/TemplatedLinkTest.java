package de.otto.jsonhome.model;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static de.otto.jsonhome.fixtures.LinkFixtures.*;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;
import static java.util.Arrays.asList;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 16.09.12
 */
public class TemplatedLinkTest {

    @Test
    public void shouldReturnValidJson() throws Exception {
        // given
        final TemplatedLink storefrontLink = ABOUTPAGE_LINK;
        // when
        final Map<String,?> json = storefrontLink.toJson();
        // then
        final Map linkAttributes = (Map) json.get(RESOURCELINK_SHOP_PAGE.toString());
        assertNotNull(linkAttributes);
        assertNull(linkAttributes.get("href"));
        assertEquals(linkAttributes.get("href-template"), REL_PAGE_HREF);
        @SuppressWarnings("unchecked")
        final List<Map<String,?>> hrefVars = (List<Map<String,?>>) linkAttributes.get("href-vars");
        assertNotNull(hrefVars);
        assertEquals(hrefVars.get(0).get("pageId"), VAR_TYPE_PAGEID.toString());
        final Map hints = (Map) linkAttributes.get("hints");
        assertNotNull(hints);
        assertEquals(hints.get("allow"), Arrays.asList("GET"));
        assertEquals(hints.get("representations"), Arrays.asList("text/html", "application/json"));

    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithShouldFailWithExceptionIfOtherDoesNotHaveSameRelationType() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        final TemplatedLink otherTemplatedLink = TemplatedLink.templatedLink(
                RESOURCELINK_SHOP_STOREFRONT,
                REL_PAGE_HREF,
                asList(new HrefVar("pageId", VAR_TYPE_PAGEID, "")),
                asList("GET"),
                asList("text/html"));
        // when
        aboutPageLink.mergeWith(otherTemplatedLink);
        // then an exception is thrown
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithShouldFailWithExceptionIfOtherLinksToDifferentResource() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        final TemplatedLink otherTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                "/foo/{fooId}",
                asList(new HrefVar("fooId", VAR_TYPE_PAGEID, "")),
                asList("GET"),
                asList("text/html"));
        // when
        aboutPageLink.mergeWith(otherTemplatedLink);
        // then an exception is thrown
    }

    @Test
    public void mergeWithShouldMergeAllowsSpec() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        final TemplatedLink otherTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                REL_PAGE_HREF,
                asList(new HrefVar("pageId", VAR_TYPE_PAGEID, "")),
                asList("PUT"),
                asList("text/html", "application/json")
        );
        // when
        final ResourceLink resourceLink = aboutPageLink.mergeWith(otherTemplatedLink);
        // then
        assertEquals(resourceLink.getAllows(), asList("GET", "PUT"));
    }

    @Test
    public void mergeWithShouldMergeRepresentations() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        final TemplatedLink otherTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                REL_PAGE_HREF,
                asList(new HrefVar("pageId", VAR_TYPE_PAGEID, "")),
                asList("GET"),
                asList("text/html", "application/foo")
        );
        // when
        final ResourceLink resourceLink = aboutPageLink.mergeWith(otherTemplatedLink);
        // then
        assertEquals(resourceLink.getRepresentations(), asList("text/html", "application/json", "application/foo"));
    }

    /**
     * The json-home Draft is relying on uri templates as defined in
     * http://tools.ietf.org/html/rfc6570. Spring is using a different approach, where it is possible to
     * have different methods supporting multiple uri templates per resource. This is currently not
     * supported by the json-home generator.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithShouldFailForDifferentHrefTemplates() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        final TemplatedLink otherTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                "pages/foo/{pageId}",
                asList(new HrefVar("pageId", VAR_TYPE_PAGEID, "")),
                asList("GET"),
                asList("text/html", "application/json")
        );
        // when
        aboutPageLink.mergeWith(otherTemplatedLink);
        // then an exception is thrown
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithShouldFailForDifferentHrefVars() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        final TemplatedLink otherTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                "pages/{pageId}",
                asList(new HrefVar("foo", VAR_TYPE_PAGEID, "")),
                asList("GET"),
                asList("text/html", "application/json")
        );
        // when
        aboutPageLink.mergeWith(otherTemplatedLink);
        // then an exception is thrown
    }

    @Test(enabled = false)
    public void mergeWithShouldMergeAcceptPut() {
        // TODO not yet implemented
    }

    @Test(enabled = false)
    public void mergeWithShouldMergeAcceptPost() {
        // TODO not yet implemented
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithDirectLinkShouldFailWithIllegalStateException() {
        // given
        final TemplatedLink templatedLink = ABOUTPAGE_LINK;
        final DirectLink directLink = STOREFRONT_LINK;
        // when
        templatedLink.mergeWith(directLink);
        // then an exception is thrown.
    }

    @Test
    public void mergeWithSelfShouldReturnEqualTemplatedLink() {
        // given
        final TemplatedLink aboutLink = ABOUTPAGE_LINK;
        // when
        final ResourceLink mergedLink = aboutLink.mergeWith(aboutLink);
        // then
        assertEquals(aboutLink, mergedLink);
    }}
