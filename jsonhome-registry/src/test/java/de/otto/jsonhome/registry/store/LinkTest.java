/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.registry.store;

import org.testng.annotations.Test;

import java.net.URI;

import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 18.11.12
 */
public class LinkTest {

    public static final String TITLE = "foo";
    public static final URI EXAMPLE_DOT_ORG = URI.create("http://example.org");

    @Test()
    public void shouldSetTitleToEmptyStringIfTitleIsNull() {
        final Link link = new Link(null, EXAMPLE_DOT_ORG);
        assertEquals(link.getTitle(), "");
    }

    @Test
    public void shouldAcceptEmptyTitle() {
        final Link link = new Link("", EXAMPLE_DOT_ORG);
        assertEquals(link.getTitle(), "");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shouldThrowExceptionIfHrefIsNull() {
        new Link(TITLE, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfHrefIsNotAbsolute() {
        new Link(TITLE, URI.create("/foo"));
    }
}
