/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.otto.jsonhome.generator;

import org.testng.annotations.Test;

import static de.otto.jsonhome.generator.UriTemplateHelper.variableNamesFrom;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 21.10.12
 */
public class UriTemplateHelperTest {

    @Test
    public void testVariableNamesFrom() {
        assertEquals(variableNamesFrom("http://example.org/foo{count}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{+count}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{#count}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{.count}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{count,foo}"), asList("count", "foo"));
        assertEquals(variableNamesFrom("http://example.org/foo{count:3}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{count*}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{/count}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{/count*}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{;count}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{;count*}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{?count}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{?count*}"), asList("count"));
        assertEquals(variableNamesFrom("http://example.org/foo{&count*}"), asList("count"));
    }
}
