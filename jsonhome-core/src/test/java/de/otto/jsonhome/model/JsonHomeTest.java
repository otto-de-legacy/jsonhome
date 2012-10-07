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

import java.net.URI;
import java.util.List;
import java.util.Map;

import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 16.09.12
 */
public class JsonHomeTest {

    @Test
    public void shouldRenderEmptyJsonHome() {
        // given
        final List<ResourceLink> resourceLinks = asList(LinkFixtures.STOREFRONT_LINK, LinkFixtures.ABOUTPAGE_LINK);
        final JsonHome jsonHome = jsonHome(resourceLinks);
        // when
        final Map<String,?> json = jsonHome.toJson();
        // then
        assertNotNull(json);
    }

    @Test
    public void shouldRenderJsonHomeWithTwoDirectLinks() {
        // given
        final List<ResourceLink> resourceLinks = asList(LinkFixtures.STOREFRONT_LINK, LinkFixtures.ABOUTPAGE_LINK);
        final JsonHome jsonHome = jsonHome(resourceLinks);
        // when
        final Map<String,?> json = jsonHome.toJson();
        // then
        assertTrue(json.containsKey("resources"));
        assertTrue(Map.class.isAssignableFrom(json.get("resources").getClass()));
        @SuppressWarnings("unchecked")
        final Map<String, ?> resources = (Map<String, ?>) json.get("resources");
        final URI relationType = LinkFixtures.STOREFRONT_LINK.getLinkRelationType();
        assertEquals(resources.get(relationType.toString()), LinkFixtures.STOREFRONT_LINK.toJson());
        assertEquals(resources.get(LinkFixtures.ABOUTPAGE_LINK.getLinkRelationType().toString()), LinkFixtures.ABOUTPAGE_LINK.toJson());
    }

}
