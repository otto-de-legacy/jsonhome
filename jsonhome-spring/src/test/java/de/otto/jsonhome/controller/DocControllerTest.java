package de.otto.jsonhome.controller;

import de.otto.jsonhome.controller.DocController;
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
