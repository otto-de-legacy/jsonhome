package de.otto.jsonhome.resource;

import de.otto.jsonhome.fixtures.ResourceFixtures;
import de.otto.jsonhome.generator.JerseyJsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

public class JsonHomeResourceTest {

    @Test
    public void testGetHomeDocument() throws Exception {
        // given
        final JsonHomeResource controller = new JsonHomeResource(
                jsonHomeSource(ResourceFixtures.ResourceWithRequestMappingAndLinkRelationTypeAtClassLevel.class));
        // when
        final Response response = controller.getAsApplicationJsonHome();
        final Map<String, ?> resourcesMap = new ObjectMapper().readValue((String) response.getEntity(), Map.class);
        // then
        assertEquals(response.getMetadata().getFirst("Cache-Control"), "max-age=3600");
        assertEquals(resourcesMap.size(), 1);
        assertTrue(resourcesMap.containsKey("resources"));
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("representations", asList("text/html"));
        expected.put("allow", asList("GET"));
        final Object hints = resources.get("http://rel.example.org/rel/foo").get("hints");
        assertEquals(hints, expected);
    }

    @Test
    public void testGetHomeDocumentWithDefaultHomeResource() throws Exception {
        // given
        final JsonHomeResource controller = new JsonHomeResource();
        // when
        final Response response = controller.getAsApplicationJsonHome();
        final Map<String, ?> resourcesMap = new ObjectMapper().readValue((String) response.getEntity(), Map.class);
        // then
        assertEquals(response.getMetadata().getFirst("Cache-Control"), "max-age=3600");
        assertEquals(resourcesMap.size(), 1);
        assertTrue(resourcesMap.containsKey("resources"));
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");

        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("representations", asList(MediaType.TEXT_HTML));
        expected.put("allow", asList("GET"));
        final Object hintsBar = resources.get("http://example.org/rel/barType").get("hints");
        assertEquals(hintsBar, expected);

        expected = new HashMap<String, Object>();
        expected.put("representations", Collections.emptyList());
        expected.put("accept-post", asList(MediaType.APPLICATION_FORM_URLENCODED));
        expected.put("allow", asList("POST"));
        final Object hintsFoo = resources.get("http://example.org/rel/fooType").get("hints");
        assertEquals(hintsFoo, expected);
    }

    @Test
    public void applicationJsonHomeShouldNotContainAdditionalInformation() throws Exception {
        // given
        final JsonHomeResource controller = new JsonHomeResource(
                jsonHomeSource(ResourceFixtures.ResourceWithDocumentation.class));
        // when
        final Response response = controller.getAsApplicationJsonHome();
        final Map<String, ?> resourcesMap = new ObjectMapper().readValue((String) response.getEntity(), Map.class);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> hints = asMap(resources.get("http://rel.example.org/rel/foo").get("hints"));
        assertNull(hints.get("description"));
    }

    @Test
    public void applicationJsonShouldContainAdditionalInformation() throws Exception {
        // given
        final JsonHomeResource controller = new JsonHomeResource(
                jsonHomeSource(ResourceFixtures.ResourceWithDocumentation.class));
        // when
        final Response response = controller.getAsApplicationJson();
        final Map<String, ?> resourcesMap = new ObjectMapper().readValue((String) response.getEntity(), Map.class);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");

        final Map<String, ?> hints = asMap(resources.get("http://rel.example.org/rel/foo").get("hints"));
        assertEquals(hints.get("description"), asList("controller value"));
    }

    @Test
    public void shouldUseRootLinkRelationTypeUri() throws Exception {
        // given
        final JsonHomeResource controller = new JsonHomeResource(
                jsonHomeSource(ResourceFixtures.ResourceWithRequestMappingAndLinkRelationTypeAtClassLevel.class));
        // when
        final Response response = controller.getAsApplicationJsonHome();
        final Map<String, ?> resourcesMap = new ObjectMapper().readValue((String) response.getEntity(), Map.class);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        assertNotNull(resources.get("http://rel.example.org/rel/foo"));
    }

    @Test
    public void shouldContainDocsHint() throws Exception {
        // given
        final JsonHomeResource controller = new JsonHomeResource(
                jsonHomeSource(ResourceFixtures.ResourceWithDocumentation.class));
        // when
        final Response response = controller.getAsApplicationJsonHome();
        final Map<String, ?> resourcesMap = new ObjectMapper().readValue((String) response.getEntity(), Map.class);
        // then
        @SuppressWarnings("unchecked")
        final Map<String, Map<String, ?>> resources = (Map<String, Map<String, ?>>) resourcesMap.get("resources");
        final Map<String, ?> relFoo = resources.get("http://rel.example.org/rel/foo");
        assertNotNull(relFoo);
        assertEquals(asMap(relFoo.get("hints")).get("docs"), "http://example.org/doc/foo");
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> asMap(final Object obj) {
        return (Map<String, ?>) obj;
    }

    private JsonHomeSource jsonHomeSource(Class<?>... classes) {
        return new JerseyJsonHomeSource(
                new JerseyJsonHomeGenerator("http://example.org", "http://rel.example.org"),
                Arrays.<Class<?>>asList(classes));
    }

}
