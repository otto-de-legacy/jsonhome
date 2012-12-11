package de.otto.jsonhome.generator;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.Annotation;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
final class HttpMethods {

    private HttpMethods() {}

    public static boolean isHttpMethod(final Annotation annotation) {
        return annotation != null &&
                annotation.annotationType().getAnnotation(HttpMethod.class) != null;
    }

}
