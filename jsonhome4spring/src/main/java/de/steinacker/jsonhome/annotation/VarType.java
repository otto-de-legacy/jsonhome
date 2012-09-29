package de.steinacker.jsonhome.annotation;

import java.lang.annotation.*;

/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VarType {

    /**
     * A short name of the var type, uniquely identifying the concept behind the var type.
     *
     * This is used to construct the URI of the var-type document if no external
     * reference is provided.
     *
     */
    String value();

    /**
     * A description of the var type.
     *
     */
    String description();

    /**
     * An optional reference to an external definition of the var type.
     *
     * Must be a fully qualified URI.
     *
     */
    String reference() default "";
}
