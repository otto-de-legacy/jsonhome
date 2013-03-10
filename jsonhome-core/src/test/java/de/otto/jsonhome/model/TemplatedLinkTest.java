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
package de.otto.jsonhome.model;

import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collections;

import static de.otto.jsonhome.fixtures.LinkFixtures.*;
import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static de.otto.jsonhome.model.Hints.hints;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 16.09.12
 */
public class TemplatedLinkTest {

    public static final URI JSONHOME_URI = create("http://example.org/json-home");
    public static final URI HREF = create("http://example.org/foo/bar/foobar");

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithShouldFailWithExceptionIfOtherDoesNotHaveSameRelationType() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        final TemplatedLink otherTemplatedLink = TemplatedLink.templatedLink(
                RESOURCELINK_SHOP_STOREFRONT,
                REL_PAGE_HREF,
                asList(hrefVar("pageId", VAR_TYPE_PAGEID, emptyDocs())),
                hints(
                        of(GET),
                        asList("text/html")));
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
                asList(hrefVar("fooId", VAR_TYPE_PAGEID, emptyDocs())),
                hints(
                        of(GET),
                        asList("text/html")
                )
        );
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
                asList(hrefVar("pageId", VAR_TYPE_PAGEID, emptyDocs())),
                hints(
                        of(PUT),
                        asList("text/html", "application/json")
                )
        );
        // when
        final ResourceLink resourceLink = aboutPageLink.mergeWith(otherTemplatedLink);
        // then
        assertEquals(resourceLink.getHints().getAllows(), of(GET, PUT));
    }

    @Test
    public void mergeWithShouldMergeRepresentations() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        final TemplatedLink otherTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                REL_PAGE_HREF,
                asList(hrefVar("pageId", VAR_TYPE_PAGEID, emptyDocs())),
                hints(
                        of(GET),
                        asList("text/html", "application/foo")
                )
        );
        // when
        final ResourceLink resourceLink = aboutPageLink.mergeWith(otherTemplatedLink);
        // then
        assertEquals(resourceLink.getHints().getRepresentations(), asList("text/html", "application/json", "application/foo"));
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
                asList(hrefVar("pageId", VAR_TYPE_PAGEID)),
                hints(
                        of(GET),
                        asList("text/html", "application/json")
                )
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
                asList(hrefVar("foo", VAR_TYPE_PAGEID)),
                hints(
                        of(GET),
                        asList("text/html", "application/json")
                )
        );
        // when
        aboutPageLink.mergeWith(otherTemplatedLink);
        // then an exception is thrown
    }

    @Test(enabled = false)
    public void mergeWithShouldMergeAcceptPut() {
        // given
        final TemplatedLink thisTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                REL_PAGE_HREF,
                asList(hrefVar("pageId", VAR_TYPE_PAGEID)),
                hints(
                        of(GET, PUT),
                        asList("application/foo"),
                        asList("application/json"),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<Precondition>emptyList(),
                        Collections.<Authentication>emptyList(),
                        Status.OK,
                        emptyDocs()
                )
        );
        final TemplatedLink thatTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                REL_PAGE_HREF,
                asList(hrefVar("pageId", VAR_TYPE_PAGEID)),
                hints(
                        of(GET, PUT),
                        asList("application/foo"),
                        asList("application/foo"),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<Precondition>emptyList(),
                        Collections.<Authentication>emptyList(),
                        Status.OK,
                        emptyDocs()
                )
        );
        // when
        final ResourceLink resourceLink = thisTemplatedLink.mergeWith(thatTemplatedLink);
        // then
        final Hints hints = resourceLink.getHints();
        assertEquals(hints.getRepresentations(), asList("application/foo"));
        assertEquals(hints.getAcceptPut(), asList("application/json", "application/foo"));
    }

    @Test(enabled = false)
    public void mergeWithShouldMergeAcceptPost() {
        // given
        final TemplatedLink thisTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                REL_PAGE_HREF,
                asList(hrefVar("pageId", VAR_TYPE_PAGEID)),
                hints(
                        of(GET, POST),
                        asList("application/foo"),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        asList("application/json"),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<Precondition>emptyList(),
                        Collections.<Authentication>emptyList(),
                        Status.OK,
                        emptyDocs()
                )
        );
        final TemplatedLink thatTemplatedLink = templatedLink(
                RESOURCELINK_SHOP_PAGE,
                REL_PAGE_HREF,
                asList(hrefVar("pageId", VAR_TYPE_PAGEID)),
                hints(
                        of(GET, POST),
                        asList("application/foo"),
                        Collections.<String>emptyList(),
                        asList("application/foo"),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList(),
                        Collections.<Precondition>emptyList(),
                        Collections.<Authentication>emptyList(),
                        Status.OK,
                        emptyDocs()
                )
        );
        // when
        final ResourceLink resourceLink = thisTemplatedLink.mergeWith(thatTemplatedLink);
        // then
        final Hints hints = resourceLink.getHints();
        assertEquals(hints.getRepresentations(), asList("application/foo"));
        assertEquals(hints.getAcceptPost(), asList("application/json", "application/foo"));
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
    }

    @Test
    public void shouldExpandUriForExistingVarType() {
        // given
        final URI varType = create("http://example.org/vars/bar");
        final TemplatedLink templatedLink = templatedLink(
                create("http://example.org/rel/foo"),
                "http://example.org/foo/{bar}",
                asList(hrefVar("bar", varType)),
                null
        );
        // when
        final URI uri = templatedLink.expandToUri(varType, "42");
        // then
        assertEquals(uri, create("http://example.org/foo/42"));
    }

    @Test
    public void shouldExpandUriWithRequestParams() {
        // given
        final URI fooVarType = create("http://example.org/vars/foo");
        final URI barVarType = create("http://example.org/vars/bar");
        final TemplatedLink templatedLink = templatedLink(
                create("http://example.org/rel/foo"),
                "http://example.org{?foo,bar}",
                asList(hrefVar("foo", fooVarType), hrefVar("bar", barVarType)),
                null
        );
        // when
        final URI uri = templatedLink.expandToUri(fooVarType, "42", barVarType, 4711);
        // then
        assertEquals(uri, create("http://example.org?foo=42&bar=4711"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void expansionShouldFailWithIllegalArgumentExceptionForUnknownVarType() {
        // given
        final URI varType = create("http://example.org/vars/bar");
        final TemplatedLink templatedLink = templatedLink(
                create("http://example.org/rel/foo"),
                "http://example.org/foo/{bar}",
                asList(hrefVar("bar", varType)),
                null
        );
        // when
        templatedLink.expandToUri(create("http://example.org/vars/foo"), "42");
        // then an exception is thrown.
    }

    @Test
    public void expansionShouldTreatMissingVarTypeAsEmptyValue() {
        // given
        final URI fooVarType = create("http://example.org/vars/foo");
        final URI barVarType = create("http://example.org/vars/bar");
        final TemplatedLink templatedLink = templatedLink(
                create("http://example.org/rel/foo"),
                "http://example.org/{foo}/{bar}",
                asList(hrefVar("foo", fooVarType), hrefVar("bar", barVarType)),
                null
        );
        // when
        final URI uri = templatedLink.expandToUri(create("http://example.org/vars/foo"), "42");
        // then
        assertEquals(uri, create("http://example.org/42/"));
    }
}
