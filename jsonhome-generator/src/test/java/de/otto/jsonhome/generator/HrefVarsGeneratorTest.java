package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.HrefVar;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static java.net.URI.create;
import static org.testng.Assert.assertEquals;

/**
 * @author Guido Steinacker
 * @since 14.01.13
 */
public class HrefVarsGeneratorTest {

    public static final URI RELATIONTYPE_BASE_URI = create("http://specs.example.org");
    public static final URI VARTYPE_BASE_URI = create("http://specs.example.org/vartypes");
    public static final URI VARTYPE_BASE_URI_WITH_TRAILING_SLASH = create("http://specs.example.org/vartypes/");

    static class TestHrefVarsGenerator extends HrefVarsGenerator {

        protected TestHrefVarsGenerator() {
            super(RELATIONTYPE_BASE_URI);
        }

        @Override
        protected boolean hasRequestParam(ParameterInfo parameterInfo) {
            return true;
        }

        @Override
        protected boolean hasPathVariable(ParameterInfo parameterInfo) {
            return true;
        }
    }

    static class MethodFixture {
        public void foo(String bar) {}
    }

    @Test
    public void testHrefVarsWithFragmentVarTypeUri() throws Exception {
        // given:
        final HrefVarsGenerator generator = new TestHrefVarsGenerator();
        // when:
        final Method method = MethodFixture.class.getMethod("foo", String.class);
        final List<HrefVar> hrefVars = generator.hrefVarsFor(VARTYPE_BASE_URI, true, method);
        // then:
        assertEquals(hrefVars.size(), 1);
        assertEquals(hrefVars.get(0), hrefVar("bar", create("http://specs.example.org/vartypes#bar"), emptyDocs()));
    }

    @Test
    public void testHrefVarsWithFragmentVarTypeUriWithTrailingSlash() throws Exception {
        // given:
        final HrefVarsGenerator generator = new TestHrefVarsGenerator();
        // when:
        final Method method = MethodFixture.class.getMethod("foo", String.class);
        final List<HrefVar> hrefVars = generator.hrefVarsFor(VARTYPE_BASE_URI_WITH_TRAILING_SLASH, true, method);
        // then:
        assertEquals(hrefVars.size(), 1);
        assertEquals(hrefVars.get(0), hrefVar("bar", create("http://specs.example.org/vartypes#bar"), emptyDocs()));
    }

    @Test
    public void testHrefVarsWithSubResourceVarTypeUri() throws Exception {
        // given:
        final HrefVarsGenerator generator = new TestHrefVarsGenerator();
        // when:
        final Method method = MethodFixture.class.getMethod("foo", String.class);
        final List<HrefVar> hrefVars = generator.hrefVarsFor(VARTYPE_BASE_URI, false, method);
        // then:
        assertEquals(hrefVars.size(), 1);
        assertEquals(hrefVars.get(0), hrefVar("bar", create("http://specs.example.org/vartypes/bar"), emptyDocs()));
    }

    @Test
    public void testHrefVarsWithSubResourceVarTypeUriWithTrailingSlash() throws Exception {
        // given:
        final HrefVarsGenerator generator = new TestHrefVarsGenerator();
        // when:
        final Method method = MethodFixture.class.getMethod("foo", String.class);
        final List<HrefVar> hrefVars = generator.hrefVarsFor(VARTYPE_BASE_URI_WITH_TRAILING_SLASH, false, method);
        // then:
        assertEquals(hrefVars.size(), 1);
        assertEquals(hrefVars.get(0), hrefVar("bar", create("http://specs.example.org/vartypes/bar"), emptyDocs()));
    }
}
