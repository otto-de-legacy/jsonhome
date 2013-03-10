package de.otto.jsonhome.parser;

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.Status;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.EnumSet;

import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.Authentication.authReq;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Documentation.docLink;
import static de.otto.jsonhome.model.Documentation.documentation;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static de.otto.jsonhome.model.Precondition.ETAG;
import static de.otto.jsonhome.model.Precondition.LAST_MODIFIED;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;
import static java.net.URI.create;
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
                .parse(new ByteArrayInputStream(jsonHomeDocument.getBytes()));
        // then
        assertEquals(jsonHome, jsonHome(
                directLink(
                        create("http://example.org/jsonhome-example/rel/storefront"),
                        create("http://example.org/jsonhome-example/storefront"),
                        hintsBuilder()
                                .allowing(EnumSet.of(GET, HEAD))
                                .representedAs(asList("text/html", "text/plain"))
                                .with(docLink(URI.create("http://de.wikipedia.org/wiki/Homepage")))
                                .requiring(ETAG)
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
                .parse(new ByteArrayInputStream(jsonHomeDocument.getBytes()));
        // then
        assertEquals(jsonHome, jsonHome(
                templatedLink(
                        create("http://example.org/jsonhome-example/rel/product"),
                        "http://example.org/jsonhome-example/products/{productId}",
                        asList(hrefVar("productId", create("http://example.org/jsonhome-example/rel/product#productId"))),
                        hintsBuilder()
                                .allowing(EnumSet.of(PUT, POST))
                                .representedAs(asList("application/json"))
                                .acceptingForPut(asList("application/example-product", "application/json"))
                                .acceptingForPost("application/json")
                                .build()
                )
        ));
    }

    @Test
    public void shouldParseMultiplePreconditions() throws Exception {
        // given
        final String jsonHomeDocument = "{\n" +
                "  \"resources\" : {" +
                "\"http://example.org/jsonhome-example/rel/storefront\" : {\n" +
                "      \"href\" : \"http://example.org/jsonhome-example/storefront\",\n" +
                "      \"hints\" : {\n" +
                "        \"precondition-req\" : [\"etag\", \"last-modified\"]\n" +
                "      }\n" +
                "    }}}";
        // when
        final JsonHome jsonHome = new JacksonJsonHomeParser()
                .parse(new ByteArrayInputStream(jsonHomeDocument.getBytes()));
        // then
        assertEquals(jsonHome, jsonHome(
                directLink(
                        create("http://example.org/jsonhome-example/rel/storefront"),
                        create("http://example.org/jsonhome-example/storefront"),
                        hintsBuilder()
                                .requiring(ETAG, LAST_MODIFIED)
                                .build()
                )
        ));
    }

    @Test
    public void shouldParseMultiplePreferences() throws Exception {
        // given
        final String jsonHomeDocument = "{\n" +
                "  \"resources\" : {" +
                "\"http://example.org/jsonhome-example/rel/storefront\" : {\n" +
                "      \"href\" : \"http://example.org/jsonhome-example/storefront\",\n" +
                "      \"hints\" : {\n" +
                "        \"prefer\" : [\"return-representation=application/json\", \"return-asynch\"]\n" +
                "      }\n" +
                "    }}}";
        // when
        final JsonHome jsonHome = new JacksonJsonHomeParser()
                .parse(new ByteArrayInputStream(jsonHomeDocument.getBytes()));
        // then
        assertEquals(jsonHome, jsonHome(
                directLink(
                        create("http://example.org/jsonhome-example/rel/storefront"),
                        create("http://example.org/jsonhome-example/storefront"),
                        hintsBuilder()
                                .preferring("return-representation=application/json", "return-asynch")
                                .build()
                )
        ));
    }

    @Test
    public void shouldParseAcceptedRanges() throws Exception {
        // given
        final String jsonHomeDocument = "{\n" +
                "  \"resources\" : {" +
                "\"http://example.org/jsonhome-example/rel/storefront\" : {\n" +
                "      \"href\" : \"http://example.org/jsonhome-example/storefront\",\n" +
                "      \"hints\" : {\n" +
                "        \"accept-ranges\" : [\"bytes\"]\n" +
                "      }\n" +
                "    }}}";
        // when
        final JsonHome jsonHome = new JacksonJsonHomeParser()
                .parse(new ByteArrayInputStream(jsonHomeDocument.getBytes()));
        // then
        assertEquals(jsonHome, jsonHome(
                directLink(
                        create("http://example.org/jsonhome-example/rel/storefront"),
                        create("http://example.org/jsonhome-example/storefront"),
                        hintsBuilder()
                                .acceptingRanges("bytes")
                                .build()
                )
        ));
    }

    @Test
    public void shouldParseMultipleRequiredAuth() throws Exception {
        // given
        final String jsonHomeDocument = "{\n" +
                "  \"resources\" : {" +
                "\"http://example.org/jsonhome-example/rel/storefront\" : {\n" +
                "      \"href\" : \"http://example.org/jsonhome-example/storefront\",\n" +
                "      \"hints\" : {\n" +
                "        \"auth-req\" : [{\"scheme\" : \"Basic\", \"realms\" : [\"public\", \"private\"]}, {\"scheme\" : \"Digest\"}]\n" +
                "      }\n" +
                "    }}}";
        // when
        final JsonHome jsonHome = new JacksonJsonHomeParser()
                .parse(new ByteArrayInputStream(jsonHomeDocument.getBytes()));
        // then
        assertEquals(jsonHome, jsonHome(
                directLink(
                        create("http://example.org/jsonhome-example/rel/storefront"),
                        create("http://example.org/jsonhome-example/storefront"),
                        hintsBuilder()
                                .withAuthRequired(asList(
                                        authReq("Basic", asList("public", "private")),
                                        authReq("Digest")))
                                .build()
                )
        ));
    }

    @Test
    public void shouldParseApplicationJsonWithDescription() throws Exception {
        // given
        final String jsonDocument = "{\n" +
                "  \"resources\" : {" +
                "\"http://example.org/jsonhome-example/rel/storefront\" : {\n" +
                "      \"href\" : \"http://example.org/jsonhome-example/storefront\",\n" +
                "      \"hints\" : {\n" +
                "        \"docs\" : \"http://de.wikipedia.org/wiki/Homepage\",\n" +
                "        \"description\" : [\"a short description\"],\n" +
                "        \"detailedDescription\" : \"<p>A detailed description.</p>\"\n" +
                "      }\n" +
                "    }}}";
        // when
        final JsonHome jsonHome = new JacksonJsonHomeParser()
                .parse(new ByteArrayInputStream(jsonDocument.getBytes()));
        // then
        assertEquals(jsonHome, jsonHome(
                directLink(
                        create("http://example.org/jsonhome-example/rel/storefront"),
                        create("http://example.org/jsonhome-example/storefront"),
                        hintsBuilder()
                                .with(documentation(
                                        asList("a short description"),
                                        "<p>A detailed description.</p>",
                                        create("http://de.wikipedia.org/wiki/Homepage")))
                                .build()
                )
        ));
    }

}
