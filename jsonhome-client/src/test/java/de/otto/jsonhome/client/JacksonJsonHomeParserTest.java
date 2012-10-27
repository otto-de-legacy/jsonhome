package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.net.URI;

import static de.otto.jsonhome.model.Allow.GET;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Docs.docLink;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static java.util.Collections.singleton;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class JacksonJsonHomeParserTest {
    @Test
    public void shouldFindSimpleDirectLink() throws Exception {
        // given
        final String jsonHomeDocument = "{\n" +
                "  \"resources\" : {" +
                "\"http://example.org/jsonhome-example/rel/storefront\" : {\n" +
                "      \"href\" : \"http://example.org/jsonhome-example/storefront\",\n" +
                "      \"hints\" : {\n" +
                "        \"allow\" : [\n" +
                "          \"GET\"\n" +
                "        ],\n" +
                "        \"docs\" : \"http://de.wikipedia.org/wiki/Homepage\",\n" +
                "        \"representations\" : [\n" +
                "          \"text/html\"\n" +
                "        ]\n" +
                "      }\n" +
                "    }}}";
        // when
        final JsonHome jsonHome = new JacksonJsonHomeParser()
                .fromStream(new ByteArrayInputStream(jsonHomeDocument.getBytes()))
                .parse();
        // then
        assertEquals(jsonHome, jsonHome(
                    directLink(
                            URI.create("http://example.org/jsonhome-example/rel/storefront"),
                            URI.create("http://example.org/jsonhome-example/storefront"),
                            hintsBuilder()
                                    .allowing(singleton(GET))
                                    .representedAs("text/html")
                                    .with(docLink(URI.create("http://de.wikipedia.org/wiki/Homepage")))
                                    .build()
                    )
        ));
    }
}
