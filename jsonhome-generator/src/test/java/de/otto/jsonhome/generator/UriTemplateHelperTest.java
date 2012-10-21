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
