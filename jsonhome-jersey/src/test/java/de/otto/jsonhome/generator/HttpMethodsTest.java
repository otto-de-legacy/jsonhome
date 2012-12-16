package de.otto.jsonhome.generator;


import org.testng.annotations.Test;

import javax.ws.rs.GET;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class HttpMethodsTest {

    class GetAnnotation {

        @GET
        public String getMethod() {
            return "";
        }

    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertTrue(HttpMethods.isHttpMethod(GetAnnotation.class.
                getMethod("getMethod").getAnnotation(GET.class)));
    }

    @Test
    public void testNullAnnotation() throws Exception {
        assertFalse(HttpMethods.isHttpMethod(null));
    }

}
