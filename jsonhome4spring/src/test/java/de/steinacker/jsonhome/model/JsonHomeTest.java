package de.steinacker.jsonhome.model;

import de.steinacker.jsonhome.fixtures.LinkFixtures;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static de.steinacker.jsonhome.model.JsonHome.jsonHome;
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
