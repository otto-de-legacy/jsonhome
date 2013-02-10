package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.registry.store.Registry;
import org.testng.annotations.Test;

import java.util.Map;

import static de.otto.jsonhome.registry.controller.RegistryConverter.jsonToLinks;
import static de.otto.jsonhome.registry.controller.RegistryConverter.linksToJson;
import static de.otto.jsonhome.registry.fixture.RegistryFixture.doubleEntryRegistry;
import static de.otto.jsonhome.registry.fixture.RegistryFixture.registryLiveWithSingleLinkTo;
import static java.net.URI.create;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 09.02.13
 */
public class RegistryConverterTest {

    @Test
    public void shouldConvertRegistryWithSingleEntry() {
        // given:
        final Map<String, Object> linksMap = registryLiveWithSingleLinkTo("foo");
        // when:
        final Registry registry = jsonToLinks(linksMap);
        // then:
        assertEquals(linksToJson(create("http://example.org"), registry), linksMap);
    }

    @Test
    public void shouldConvertRegistryWithMultipleEntries() {
        // given:
        final Map<String, Object> linksMap = doubleEntryRegistry();
        // when:
        final Registry registry = jsonToLinks(linksMap);
        // then:
        assertEquals(linksToJson(create("http://example.org"), registry), linksMap);
    }
}
