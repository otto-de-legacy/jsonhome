/*
 * *
 *  Copyright 2012 Guido Steinacker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.otto.jsonhome.converter;

import de.otto.jsonhome.model.DirectLink;
import de.otto.jsonhome.model.TemplatedLink;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

import static de.otto.jsonhome.converter.ResourceLinkConverter.resourceLinkToJsonHome;
import static de.otto.jsonhome.fixtures.LinkFixtures.*;
import static de.otto.jsonhome.model.Allow.GET;
import static java.util.Collections.singletonMap;
import static java.util.EnumSet.of;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 14.10.12
 */
public class ResourceLinkConverterTest {

    @Test
    public void shouldConvertDirectLink() {
        // given
        final DirectLink storefrontLink = STOREFRONT_LINK;
        // when
        final Map<String,?> json = resourceLinkToJsonHome(storefrontLink);
        // then
        assertNotNull(json);
        assertEquals(json.keySet().size(), 1);
        @SuppressWarnings("unchecked")
        final Map<String, ?> resource = (Map<String, ?>) json.get("http://example.org/json-home/rel/shop/storefront");
        assertEquals(resource.keySet().size(), 2);
        assertEquals(resource.get("href"), ABS_STOREFRONT_HREF.toString());
        final Map hints = (Map) resource.get("hints");
        assertNotNull(hints);
        assertEquals(hints.get("allow"), of(GET));
        assertEquals(hints.get("representations"), Arrays.asList("text/html", "application/json"));
    }

    @Test
    public void shouldConvertTemplatedLink() {
        // given
        final TemplatedLink aboutPageLink = ABOUTPAGE_LINK;
        // when
        Map<String, ?> json = resourceLinkToJsonHome(aboutPageLink);
        // then
        assertNotNull(json);
        assertEquals(json.keySet().size(), 1);
        @SuppressWarnings("unchecked")
        final Map<String, ?> resource = (Map<String, ?>) json.get("http://example.org/json-home/rel/shop/page");
        assertEquals(resource.keySet().size(), 3);
        assertNull(resource.get("href"));
        assertEquals(resource.get("href-template"), REL_PAGE_HREF);
        assertEquals(resource.get("href-vars"), singletonMap("pageId", "http://example.org/json-home/vartype/shop/page/pageId"));
        final Map hints = (Map) resource.get("hints");
        assertNotNull(hints);
        assertEquals(hints.get("allow"), of(GET));
        assertEquals(hints.get("representations"), Arrays.asList("text/html", "application/json"));
    }
}
