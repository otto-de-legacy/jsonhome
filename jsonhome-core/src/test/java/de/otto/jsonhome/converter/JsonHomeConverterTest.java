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

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeConverter.toJsonHomeRepresentation;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;
import static de.otto.jsonhome.converter.ResourceLinkConverter.toRepresentation;
import static de.otto.jsonhome.fixtures.LinkFixtures.ABOUTPAGE_LINK;
import static de.otto.jsonhome.fixtures.LinkFixtures.STOREFRONT_LINK;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static java.util.Arrays.asList;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 14.10.12
 */
public class JsonHomeConverterTest {
    @Test
    public void shouldRenderEmptyJsonHome() {
        // given
        final List<ResourceLink> resourceLinks = asList(STOREFRONT_LINK, ABOUTPAGE_LINK);
        final JsonHome jsonHome = jsonHome(resourceLinks);
        // when
        final Map<String,?> json = toJsonHomeRepresentation(jsonHome);
        // then
        assertNotNull(json);
    }

    @Test
    public void shouldRenderJsonHomeWithTwoDirectLinks() {
        // given
        final List<ResourceLink> resourceLinks = asList(STOREFRONT_LINK, ABOUTPAGE_LINK);
        final JsonHome jsonHome = jsonHome(resourceLinks);
        // when
        final Map<String,?> json = toJsonHomeRepresentation(jsonHome);
        // then
        assertTrue(json.containsKey("resources"));
        assertTrue(Map.class.isAssignableFrom(json.get("resources").getClass()));
        @SuppressWarnings("unchecked")
        final Map<String, ?> resources = (Map<String, ?>) json.get("resources");
        String relationType = STOREFRONT_LINK.getLinkRelationType().toString();
        assertEquals(resources.get(relationType), toRepresentation(STOREFRONT_LINK, APPLICATION_JSONHOME).get(relationType));
        relationType = ABOUTPAGE_LINK.getLinkRelationType().toString();
        assertEquals(resources.get(relationType), toRepresentation(ABOUTPAGE_LINK, APPLICATION_JSONHOME).get(relationType));
    }


}
