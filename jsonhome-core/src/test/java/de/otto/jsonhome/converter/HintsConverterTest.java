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

import de.otto.jsonhome.model.Allow;
import de.otto.jsonhome.model.Hints;
import de.otto.jsonhome.model.Status;
import org.testng.annotations.Test;

import java.util.*;

import static de.otto.jsonhome.converter.HintsConverter.toJsonHomeRepresentation;
import static de.otto.jsonhome.converter.HintsConverter.toRepresentation;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSON;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;
import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.Authentication.authReq;
import static de.otto.jsonhome.model.Documentation.documentation;
import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.Precondition.ETAG;
import static de.otto.jsonhome.model.Precondition.LAST_MODIFIED;
import static java.net.URI.create;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author Guido Steinacker
 * @since 30.09.12
 */
public class HintsConverterTest {

    @Test
    public void testToJson() throws Exception {
        // given
        final Set<Allow> allows = of(GET, POST, PUT);
        final List<String> representations = asList("text/html", "text/plain");
        final List<String> acceptPut = asList("foo/bar");
        final List<String> acceptPost = asList("bar/foo");
        final Hints hints = hintsBuilder()
                .allowing(allows)
                .representedAs(representations)
                .acceptingForPut(acceptPut)
                .acceptingForPost(acceptPost)
                .acceptingRanges("bytes")
                .preferring("return-representation=application/json", "return-asynch")
                .with(emptyDocs())
                .requiring(asList(ETAG, LAST_MODIFIED))
                .withStatus(Status.DEPRECATED)
                .build();
        // when
        final Map<String, ?> map = toJsonHomeRepresentation(hints);
        // then
        assertEquals(map.keySet().size(), 8);
        assertEquals(map.get("allow"), allows);
        assertEquals(map.get("representations"), representations);
        assertEquals(map.get("accept-put"), acceptPut);
        assertEquals(map.get("accept-post"), acceptPost);
        assertEquals(map.get("accept-ranges"), asList("bytes"));
        assertEquals(map.get("prefer"), asList("return-representation=application/json", "return-asynch"));
        assertEquals(map.get("precondition-req"), asList("etag", "last-modified"));
        assertEquals(map.get("status"), "deprecated");
    }

    @Test
    public void testAuthReq() {
        // given
        final Hints hints = hintsBuilder().withAuthRequired(asList(
                authReq("Basic", asList("private"))))
                .build();
        // when
        final Map<String, ?> map = toJsonHomeRepresentation(hints);
        // then
        assertEquals(map.keySet().size(), 3);
        List<Map<String, Object>> expectedAuthReq = new ArrayList<Map<String, Object>>();
        Map<String, Object> auth = new HashMap<String, Object>();
        auth.put("scheme", "Basic");
        auth.put("realms", asList("private"));
        expectedAuthReq.add(auth);
        assertEquals(map.get("auth-req"), expectedAuthReq);
    }

    @Test
    public void testStatusDeprecated() throws Exception {
        // given
        final Hints hints = hintsBuilder().withStatus(Status.DEPRECATED).build();
        // when
        final Map<String, ?> map = toJsonHomeRepresentation(hints);
        // then
        assertEquals(map.get("status"), "deprecated");
    }

    @Test
    public void testStatusGone() throws Exception {
        // given
        final Hints hints = hintsBuilder().withStatus(Status.GONE).build();
        // when
        final Map<String, ?> map = toJsonHomeRepresentation(hints);
        // then
        assertEquals(map.get("status"), "gone");
    }

    @Test
    public void testStatusOK() throws Exception {
        // given
        final Hints hints = hintsBuilder().withStatus(Status.OK).build();
        // when
        final Map<String, ?> map = toJsonHomeRepresentation(hints);
        // then
        assertEquals(map.get("status"), null);
    }

    @Test
    public void testAdditionalDescriptionForApplicationJsonHome() {
        // given
        final Hints hints = hintsBuilder()
                .with(documentation(asList("foo", "bar"), "<h1>Hello</h1>", create("http://example.org/docs")))
                .build();
        // when
        final Map<String, ?> jsonHomeMap = toRepresentation(hints, APPLICATION_JSONHOME);
        // then
        assertEquals(jsonHomeMap.get("docs"), "http://example.org/docs");
        assertNull(jsonHomeMap.get("description"));
        assertNull(jsonHomeMap.get("detailedDescription"));
    }

    @Test
    public void testAdditionalDescriptionForApplicationJson() {
        // given
        final Hints hints = hintsBuilder()
                .with(documentation(asList("foo", "bar"), "<h1>Hello</h1>", create("http://example.org/docs")))
                .build();
        // when
        final Map<String, ?> jsonMap = toRepresentation(hints, APPLICATION_JSON);
        // then
        assertEquals(jsonMap.get("docs"), "http://example.org/docs");
        assertEquals(jsonMap.get("description"), asList("foo", "bar"));
        assertEquals(jsonMap.get("detailedDescription"), "<h1>Hello</h1>");
    }
}
