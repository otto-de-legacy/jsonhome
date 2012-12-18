package de.otto.jsonhome.fixtures.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAspectAnnotationAspect {

    @Before("@target(org.springframework.stereotype.Component) && @annotation(foo)")
    public void assertAuthorized(JoinPoint jp, TestAspectAnnotation foo) {
    }

}