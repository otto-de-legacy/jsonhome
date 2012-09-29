package de.steinacker.jsonhome.annotation;

import java.lang.annotation.*;

/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VarTypes {

    VarType[] value();

}
