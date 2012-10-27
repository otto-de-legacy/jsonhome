package de.otto.jsonhome.client;

import de.otto.jsonhome.annotation.Precondition;
import de.otto.jsonhome.annotation.Status;
import de.otto.jsonhome.model.Allow;
import de.otto.jsonhome.model.JsonHome;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.EnumSet;

import static de.otto.jsonhome.model.Allow.GET;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Docs.docLink;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
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
                                    .allowing(EnumSet.of(GET, Allow.HEAD))
                                    .representedAs(asList("text/html", "text/plain"))
                                    .with(docLink(URI.create("http://de.wikipedia.org/wiki/Homepage")))
                                    .requiring(Precondition.ETAG)
                                    .withStatus(Status.DEPRECATED)
                                    .build()
                    )
        ));
    }

}
