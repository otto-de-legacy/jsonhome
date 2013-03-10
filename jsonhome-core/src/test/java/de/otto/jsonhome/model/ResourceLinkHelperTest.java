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

package de.otto.jsonhome.model;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static de.otto.jsonhome.fixtures.LinkFixtures.*;
import static de.otto.jsonhome.model.ResourceLinkHelper.mergeResources;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 27.12.12
 */
public class ResourceLinkHelperTest {

    @Test
    public void mergingIdenticalLinksShouldResultInSingleLink() throws Exception {
        // when
        final List<? extends ResourceLink> resourceLinks = mergeResources(
                asList(STOREFRONT_LINK),
                STOREFRONT_LINK
        );
        // then
        assertEquals(resourceLinks, asList(STOREFRONT_LINK));
    }

    @Test
    public void mergingDifferentLinksShouldResultInMergedLinks() throws Exception {
        // when
        final List<? extends ResourceLink> resourceLinks = mergeResources(
                asList(STOREFRONT_LINK),
                ABOUTPAGE_LINK
        );
        // then
        assertEquals(resourceLinks, asList(STOREFRONT_LINK, ABOUTPAGE_LINK));
    }

    @Test
    public void mergingListWithIdenticalLinksShouldResultInSingleLink() throws Exception {
        // when
        final List<? extends ResourceLink> resourceLinks = mergeResources(
                asList(STOREFRONT_LINK),
                asList(STOREFRONT_LINK)
        );
        // then
        assertEquals(resourceLinks, asList(STOREFRONT_LINK));
    }

    @Test
    public void mergingListsWithDifferentLinksShouldResultInMergedLinks() throws Exception {
        // when
        final List<ResourceLink> first = asList(STOREFRONT_LINK, ABOUTPAGE_LINK);
        final List<DirectLink> second = asList(SHOPPAGES_LINK, FOO_LINK);
        final List<? extends ResourceLink> resourceLinks = mergeResources(
                first,
                second
        );
        // then
        assertEquals(resourceLinks, asList(STOREFRONT_LINK, ABOUTPAGE_LINK, SHOPPAGES_LINK, FOO_LINK));
        assertEquals(first.size(), 2);
        assertEquals(second.size(), 2);
    }

    @Test
    public void mergingEmptyListWithLinksShouldResultInLinks() throws Exception {
        // when
        final List<? extends ResourceLink> resourceLinks = mergeResources(
                asList(STOREFRONT_LINK),
                Collections.<ResourceLink>emptyList()
        );
        // then
        assertEquals(resourceLinks, asList(STOREFRONT_LINK));
    }
}
