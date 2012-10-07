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

import de.otto.jsonhome.fixtures.LinkFixtures;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import static de.otto.jsonhome.fixtures.LinkFixtures.*;
import static de.otto.jsonhome.model.Allow.GET;
import static de.otto.jsonhome.model.Allow.PUT;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.of;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

/**
 * @author Guido Steinacker
 * @since 16.09.12
 */
public class DirectLinkTest {

    @Test
    public void shouldReturnValidJson() throws Exception {
        // given
        final DirectLink storefrontLink = STOREFRONT_LINK;
        // when
        final Map<String,?> json = storefrontLink.toJson();
        // then
        assertNotNull(json);
        assertEquals(json.get("href"), ABS_STOREFRONT_HREF.toString());
        final Map hints = (Map) json.get("hints");
        assertNotNull(hints);
        assertEquals(hints.get("allow"), of(GET));
        assertEquals(hints.get("representations"), Arrays.asList("text/html", "application/json"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithShouldFailWithExceptionIfOtherDoesNotHaveSameRelationType() {
        // given
        final DirectLink storeFrontLink = STOREFRONT_LINK;
        final DirectLink aboutPageLink = SHOPPAGES_LINK;
        // when
        storeFrontLink.mergeWith(aboutPageLink);
        // then an exception is thrown
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithShouldFailIfOtherHasDifferentHref() {
        // given
        final DirectLink storeFrontLink = STOREFRONT_LINK;
        final DirectLink linkWithDifferentHref = directLink(LinkFixtures.RESOURCELINK_SHOP_STOREFRONT, LinkFixtures.ABS_SHOPPAGES_HREF, new Hints(of(GET), asList("text/html", "application/json")));
        // when
        storeFrontLink.mergeWith(linkWithDifferentHref);
        // then an exception is thrown
    }

    @Test
    public void mergeWithShouldMergeAllowsSpec() {
        // given
        final DirectLink storeFrontLink = STOREFRONT_LINK;
        final DirectLink linkWithDifferentHref = directLink(LinkFixtures.RESOURCELINK_SHOP_STOREFRONT, LinkFixtures.ABS_STOREFRONT_HREF, new Hints(of(PUT), singletonList("text/html")));
        // when
        final ResourceLink resourceLink = storeFrontLink.mergeWith(linkWithDifferentHref);
        // then
        assertEquals(resourceLink.getHints().getAllows(), of(GET, PUT));
    }

    @Test
    public void mergeWithShouldMergeRepresentations() {
        // given
        final DirectLink storeFrontLink = STOREFRONT_LINK;
        final DirectLink linkWithDifferentHref = directLink(LinkFixtures.RESOURCELINK_SHOP_STOREFRONT, LinkFixtures.ABS_STOREFRONT_HREF, new Hints(of(GET), singletonList("application/json")));
        // when
        final ResourceLink resourceLink = storeFrontLink.mergeWith(linkWithDifferentHref);
        // then
        assertEquals(resourceLink.getHints().getRepresentations(), asList("text/html", "application/json"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mergeWithTemplatedLinkShouldFailWithIllegalStateException() {
        // given
        final DirectLink storeFrontLink = STOREFRONT_LINK;
        final TemplatedLink templatedLink = ABOUTPAGE_LINK;
        // when
        storeFrontLink.mergeWith(templatedLink);
        // then an exception is thrown.
    }

    @Test
    public void mergeWithSelfShouldReturnEqualDirectLink() {
        // given
        final DirectLink storeFrontLink = STOREFRONT_LINK;
        // when
        final ResourceLink mergedLink = storeFrontLink.mergeWith(storeFrontLink);
        // then
        assertEquals(storeFrontLink, mergedLink);
    }


}
