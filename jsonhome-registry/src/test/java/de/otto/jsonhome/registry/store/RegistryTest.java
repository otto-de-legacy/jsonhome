package de.otto.jsonhome.registry.store;

import org.testng.annotations.Test;

import java.util.Collections;

import static java.net.URI.create;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.testng.Assert.*;

/**
 * @author Guido Steinacker
 * @since 09.02.13
 */
public class RegistryTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void shouldThrowExceptionIfNameIsNull() {
        new Registry(null, "", Collections.<Link>emptyList());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNameIsEmpty() {
        new Registry("", "", Collections.<Link>emptyList());
    }

    @Test
    public void shouldCreateEmptyLinks() {
        final Registry registry = new Registry("foo", "", Collections.<Link>emptyList());
        assertTrue(registry.getAll().isEmpty());
    }

    @Test
    public void shouldCreateLinks() {
        final Link link = new Link(create("http://example.org"), "");
        final Registry registry = new Registry("foo", "", singletonList(
                link
        ));
        assertEquals(registry.getAll(), singletonList(link));
    }

    @Test
    public void shouldFindLinksByHref() {
        final Link foo = new Link(create("http://example.org/foo"), "");
        final Link bar = new Link(create("http://example.org/bar"), "");
        final Registry registry = new Registry("fooBar", "", asList(foo, bar));
        assertEquals(registry.findByHref(create("http://example.org/bar")), bar);
    }

    @Test
    public void shouldReturnNullIfHrefDoesNotExist() {
        final Registry registry = new Registry("foo", "", Collections.<Link>emptyList());
        assertNull(registry.findByHref(create("http://example.org")));
    }

    @Test
    public void shouldReturnLinkForSimpleBaseUri() {
        final Registry registry = new Registry("foo", "Registry Foo", Collections.<Link>emptyList());
        assertEquals(
                registry.asLinkFor(create("http://example.org")),
                new Link(create("http://example.org/registries/foo"), "Registry Foo"));
    }

    @Test
    public void shouldReturnLinkForBaseUriWithContext() {
        final Registry registry = new Registry("foo", "Registry Foo", Collections.<Link>emptyList());
        assertEquals(
                registry.asLinkFor(create("http://example.org/bar")),
                new Link(create("http://example.org/bar/registries/foo"), "Registry Foo"));
    }

    @Test
    public void shouldReturnLinkForSimpleBaseUriWithTrailingSlash() {
        final Registry registry = new Registry("foo", "Registry Foo", Collections.<Link>emptyList());
        assertEquals(
                registry.asLinkFor(create("http://example.org/")),
                new Link(create("http://example.org/registries/foo"), "Registry Foo"));
    }

    @Test
    public void shouldReturnLinkForBaseUriWithContextAndTrailingSlash() {
        final Registry registry = new Registry("foo", "Registry Foo", Collections.<Link>emptyList());
        assertEquals(
                registry.asLinkFor(create("http://example.org/bar/")),
                new Link(create("http://example.org/bar/registries/foo"), "Registry Foo"));
    }
}
