package de.otto.jsonhome.generator;

import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Guido Steinacker
 * @since 03.10.12
 */
public class MethodHelperTest {

    static class Fixture {
        public void methodWithoutParameter() {}
        public void methodWithSinglePrimitiveParam(int foo) {}
        public void methodWithNonPrimitivePlusGenericParam(Double foo, List<String> bar) {}
        public void methodWithAnnotatedParam(@Value("test") int foo) {}
    }

    @Test
    public void methodWithoutParameter() throws Exception{
        // given
        final Method method = Fixture.class.getMethod("methodWithoutParameter");
        // when
        final List<ParameterInfo> parameterInfos = MethodHelper.getParameterInfos(method);
        // then
        assertTrue(parameterInfos.isEmpty());

    }

    @Test
    public void methodWithSinglePrimitiveParameter() throws Exception{
        // given
        final Method method = Fixture.class.getMethod("methodWithSinglePrimitiveParam", int.class);
        // when
        final List<ParameterInfo> parameterInfos = MethodHelper.getParameterInfos(method);
        // then
        assertEquals(parameterInfos.size(), 1);
        assertEquals(parameterInfos.get(0).getName(), "foo");
        assertEquals(parameterInfos.get(0).getType(), int.class);
        assertTrue(parameterInfos.get(0).getAnnotations().isEmpty());
    }

    @Test
    public void methodWithNonPrimitivePlusGenericParam() throws Exception{
        // given
        final Method method = Fixture.class.getMethod("methodWithNonPrimitivePlusGenericParam", Double.class, List.class);
        // when
        final List<ParameterInfo> parameterInfos = MethodHelper.getParameterInfos(method);
        // then
        assertEquals(parameterInfos.size(), 2);
        assertEquals(parameterInfos.get(0).getName(), "foo");
        assertEquals(parameterInfos.get(0).getType(), Double.class);
        assertTrue(parameterInfos.get(0).getAnnotations().isEmpty());
        assertEquals(parameterInfos.get(1).getName(), "bar");
        assertEquals(parameterInfos.get(1).getType(), List.class);
        assertTrue(parameterInfos.get(1).getAnnotations().isEmpty());
    }

    @Test
    public void methodWithAnnotatedParam() throws Exception{
        // given
        final Method method = Fixture.class.getMethod("methodWithAnnotatedParam", int.class);
        // when
        final List<ParameterInfo> parameterInfos = MethodHelper.getParameterInfos(method);
        // then
        assertEquals(parameterInfos.size(), 1);
        assertEquals(parameterInfos.get(0).getName(), "foo");
        assertEquals(parameterInfos.get(0).getType(), int.class);
        assertEquals(parameterInfos.get(0).getAnnotations().size(), 1);
        assertEquals(parameterInfos.get(0).getAnnotations().get(0).annotationType(), Value.class);
    }
}
