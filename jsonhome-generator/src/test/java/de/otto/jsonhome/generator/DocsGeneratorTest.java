package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.model.Documentation;
import org.testng.annotations.Test;

import java.util.Collections;

import static java.net.URI.create;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Unit tests of the DocsGenerator.
 *
 * @author Guido Steinacker
 * @since 10.03.13
 */
public class DocsGeneratorTest {

    private static @Doc(
            value = {"foo", "bar"},
            link = "http://example.org/foo/bar.html",
            rel = "/rel/foo"
    )
    class ControllerWithAbsoluteLink { }
    private static @Doc(
            value = {"foo", "bar"},
            link = "/foo/bar.html",
            rel = "/rel/foo"
    )
    class ControllerWithRelativeLink { }
    private static @Doc(
            value = {"foo", "bar"},
            include = "/test.md",
            rel = "/rel/foo"
    )
    class ControllerWithMarkdownInclude { }
    private static @Doc(
            include = "test.md",
            rel = "/rel/foo"
    )
    class ControllerWithMinimalMarkdownInclude { }
    private static @Doc(
            include = "/doesnotexist.md",
            rel = "/rel/foo"
    )
    class ControllerWithUnresolvableMarkdown { }

    @Test
    public void docsWithAbsoluteLinkShouldKeepLink() {
        final Documentation documentation = docsGenerator().documentationFrom(
                create("http://example.org/rel/foo"),
                ControllerWithAbsoluteLink.class
        );
        assertEquals(documentation.getLink(), create("http://example.org/foo/bar.html"));
    }

    @Test
    public void docsWithRelativeLinkShouldUseRelationTypeBaseUriAsPrefix() {
        final Documentation documentation = docsGenerator().documentationFrom(
                create("http://example.org/rel/foo"),
                ControllerWithRelativeLink.class
        );
        assertEquals(documentation.getLink(), create("http://example.org/foo/bar.html"));
    }

    @Test
    public void markdownIncludeWithValuesShouldContainBothDescriptions() {
        final Documentation documentation = docsGenerator().documentationFrom(
                create("http://example.org/rel/foo"),
                ControllerWithMarkdownInclude.class
        );
        assertEquals(documentation.getDescription(), asList("foo", "bar"));
        assertEquals(documentation.getDetailedDescription(), "<h1>Hello World!</h1>");
    }

    @Test
    public void markdownIncludeShouldContainRenderedMarkdown() {
        final Documentation documentation = docsGenerator().documentationFrom(
                create("http://example.org/rel/foo"),
                ControllerWithMinimalMarkdownInclude.class
        );
        assertEquals(documentation.getDescription(), emptyList());
        assertEquals(documentation.getDetailedDescription(), "<h1>Hello World!</h1>");
    }

    @Test
    public void docsGeneratorWithRelativeRootShouldWorkToo() {
        final DocsGenerator docsGenerator = new DocsGenerator(create("http://example.org"), "docs/*");
        final Documentation documentation = docsGenerator.documentationFrom(
                create("http://example.org/rel/foo"),
                ControllerWithMinimalMarkdownInclude.class
        );
        assertEquals(documentation.getDescription(), emptyList());
        assertEquals(documentation.getDetailedDescription(), "<h1>Hello World!</h1>");
    }

    @Test
    public void shouldNotFailToParseDocWithUnresolvableMarkdown() {
        final Documentation documentation = docsGenerator().documentationFrom(
                create("http://example.org/rel/foo"),
                ControllerWithUnresolvableMarkdown.class
        );
        assertEquals(documentation.getDescription(), emptyList());
    }

    @Test
    public void valuesShouldBeParsedAsDescription() {
        final Documentation documentation = docsGenerator().documentationFrom(
                create("http://example.org/rel/foo"),
                ControllerWithAbsoluteLink.class
        );
        assertTrue(documentation.hasDescription());
        assertEquals(documentation.getDescription(), asList("foo", "bar"));
    }

    private DocsGenerator docsGenerator() {
        return new DocsGenerator(create("http://example.org"), "/docs/*");
    }
}
