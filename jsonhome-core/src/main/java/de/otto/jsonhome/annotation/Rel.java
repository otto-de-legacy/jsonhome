package de.otto.jsonhome.annotation;

import java.lang.annotation.*;

/**
 * A link relation type according to http://tools.ietf.org/html/rfc5988#section-4.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rel {

    /**
     * The URI uniquely identifying the link relation type in a given context.
     *
     *
     * @return URI of the link relation type
     */
    String value();

    String description() default "";

}
