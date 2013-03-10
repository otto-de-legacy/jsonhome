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

package de.otto.jsonhome.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 17.02.13
 */
public class DocControllerTest {

    @Test
    public void shouldReturnMarkdown() throws IOException {
        // given
        final DocController controller = new DocController();
        controller.setRootDir(new ClassPathResource("/test/**"));
        // when
        final String markdown = controller.getMarkdown(
                new MockHttpServletRequest("GET", "/test/doc/test.md"));
        // then
        assertTrue(markdown.startsWith("Test"));
    }

    @Test
    public void shouldReturnMarkdownAsHtml() throws IOException {
        // given
        final DocController controller = new DocController();
        controller.setRootDir(new ClassPathResource("/test/**"));
        // when
        final String markdown = controller.getMarkdownAsHtml(
                new MockHttpServletRequest("GET", "/test/doc/test.md"));
        // then
        assertTrue(markdown.startsWith("<h1>Test</h1>"));
    }
}
