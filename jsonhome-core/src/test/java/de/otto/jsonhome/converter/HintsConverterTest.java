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
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.otto.jsonhome.converter.HintsConverter.hintsToJsonHome;
import static de.otto.jsonhome.model.Allow.*;
import static de.otto.jsonhome.model.Docs.emptyDocumentation;
import static de.otto.jsonhome.model.HintsBuilder.hints;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
import static org.testng.Assert.assertEquals;

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
        final List<String> preconditions = asList("etag");
        final Hints hints = hints()
                .allowing(allows)
                .representedAs(representations)
                .acceptingForPut(acceptPut)
                .acceptingForPost(acceptPost)
                .with(emptyDocumentation())
                .requiring(preconditions)
                .build();
        // when
        final Map<String, ?> map = hintsToJsonHome(hints);
        // then
        assertEquals(map.keySet().size(), 5);
        assertEquals(map.get("allow"), allows);
        assertEquals(map.get("representations"), representations);
        assertEquals(map.get("accept-put"), acceptPut);
        assertEquals(map.get("accept-post"), acceptPost);
        assertEquals(map.get("precondition-req"), preconditions);
    }

}
