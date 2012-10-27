package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.Precondition;
import de.otto.jsonhome.model.Status;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.EnumSet;

import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Docs.docLink;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class JacksonJsonHomeParserTest {

    @Test
    public void shouldParseDirectLink() throws Exception {
        // given
        final String jsonHomeDocument = "{\n" +
                "  \"resources\" : {" +
                "\"http://example.org/jsonhome-example/rel/storefront\" : {\n" +
                "      \"href\" : \"http://example.org/jsonhome-example/storefront\",\n" +
                "      \"hints\" : {\n" +
                "        \"allow\" : [\n" +
                "          \"GET\"\n," +
                "          \"HEAD\"\n" +
                "        ],\n" +
                "        \"docs\" : \"http://de.wikipedia.org/wiki/Homepage\",\n" +
                "        \"precondition-req\" : [\"etag\"],\n" +
                "        \"status\" : \"deprecated\",\n" +
                "        \"docs\" : \"http://de.wikipedia.org/wiki/Homepage\",\n" +
                "        \"representations\" : [\n" +
                "          \"text/html\",\n" +
                "          \"text/plain\"\n" +
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
                                    .allowing(EnumSet.of(GET, HEAD))
                                    .representedAs(asList("text/html", "text/plain"))
                                    .with(docLink(URI.create("http://de.wikipedia.org/wiki/Homepage")))
                                    .requiring(Precondition.ETAG)
                                    .withStatus(Status.DEPRECATED)
                                    .build()
                    )
        ));
    }

    @Test
    public void shouldParseTemplatedLink() throws Exception {
        // given
        final String jsonHomeDocument = "{\n" +
                "  \"resources\" : {" +
                "\"http://example.org/jsonhome-example/rel/product\" : {\n" +
                "      \"href-template\" : \"http://example.org/jsonhome-example/products/{productId}\",\n" +
                "      \"href-vars\" : {\n" +
                "        \"productId\" : \"http://example.org/jsonhome-example/rel/product#productId\"\n" +
                "      },\n" +
                "      \"hints\" : {\n" +
                "        \"allow\" : [\n" +
                "          \"POST\",\n" +
                "          \"PUT\"\n" +
                "        ],\n" +
                "        \"accept-put\" : [\n" +
                "          \"application/example-product\",\n" +
                "          \"application/json\"\n" +
                "        ],\n" +
                "        \"accept-post\" : [\n" +
                "          \"application/json\"\n" +
                "        ],\n" +
                "        \"representations\" : [\n" +
                "          \"application/json\"\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "    }}}";
        // when
        final JsonHome jsonHome = new JacksonJsonHomeParser()
                .fromStream(new ByteArrayInputStream(jsonHomeDocument.getBytes()))
                .parse();
        // then
        assertEquals(jsonHome, jsonHome(
                templatedLink(
                        URI.create("http://example.org/jsonhome-example/rel/product"),
                        "http://example.org/jsonhome-example/products/{productId}",
                        asList(hrefVar("productId", URI.create("http://example.org/jsonhome-example/rel/product#productId"))),
                        hintsBuilder()
                                .allowing(EnumSet.of(PUT, POST))
                                .representedAs(asList("application/json"))
                                .acceptingForPut(asList("application/example-product", "application/json"))
                                .acceptingForPost("application/json")
                                .build()
                )
        ));
    }
}
