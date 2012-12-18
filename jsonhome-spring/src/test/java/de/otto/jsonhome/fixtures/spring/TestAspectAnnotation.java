package de.otto.jsonhome.fixtures.spring;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestAspectAnnotation {

    String value() default "";

}