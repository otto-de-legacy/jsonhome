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
package de.otto.jsonhome.registry;

import org.testng.annotations.Test;

import java.net.URI;

/**
 * @author Guido Steinacker
 * @since 18.11.12
 */
public class RegistryEntryTest {

    public static final String TITLE = "foo";
    public static final URI EXAMPLE_DOT_ORG = URI.create("http://example.org");

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfSelfIsNull() {
        new RegistryEntry(null, TITLE, EXAMPLE_DOT_ORG);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfSelfIsNotAbsolute() {
        new RegistryEntry(URI.create("/foo"), TITLE, EXAMPLE_DOT_ORG);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfTitleIsNull() {
        new RegistryEntry(EXAMPLE_DOT_ORG, null, EXAMPLE_DOT_ORG);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfTitleIsEmpty() {
        new RegistryEntry(EXAMPLE_DOT_ORG, "", EXAMPLE_DOT_ORG);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfHrefIsNull() {
        new RegistryEntry(EXAMPLE_DOT_ORG, TITLE, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfHrefIsNotAbsolute() {
        new RegistryEntry(EXAMPLE_DOT_ORG, TITLE, URI.create("/foo"));
    }
}
