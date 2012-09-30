package de.otto.jsonhome.fixtures;

import de.otto.jsonhome.annotation.LinkRelationType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Guido Steinacker
 * @since 17.09.12
 */
public class ControllerFixtures {


    public static @Controller @RequestMapping class ControllerWithoutResource {}

    public static @Controller @RequestMapping("/foo") class ControllerWithoutMethods {}

    public static @Controller @RequestMapping("/foo") class ControllerWithResourceWithoutLinkRelationType {
        public @RequestMapping("/bar") void bar() {}
    }

    public static @Controller @RequestMapping("/foo") @LinkRelationType("http://example.org/rel/fooBarType") class ControllerWithResourceAndLinkRelationType {
        public @RequestMapping("/bar") void bar() {}
    }

    public static @Controller @RequestMapping("/foo") class ControllerWithDifferentUrisForSameRelationType {
        public @RequestMapping @LinkRelationType("http://example.org/rel/fooType") void foo() {}
        public @RequestMapping("/bar") @LinkRelationType("http://example.org/rel/fooType") void bar() {}
    }

    public static @Controller @RequestMapping("/foo") class ControllerWithDifferentResourceDefinitions {
        public @RequestMapping @LinkRelationType("http://example.org/rel/fooType") void foo() {}
        public @RequestMapping("/bar") @LinkRelationType("http://example.org/rel/barType") void bar() {}
        public @RequestMapping("/foobar") @LinkRelationType("http://example.org/rel/fooBarType") void fooBar() {}
    }

    public static @Controller @RequestMapping("/foo") @LinkRelationType("http://example.org/rel/fooType") class ControllerWithDifferentRepresentations {
        public @RequestMapping void defaultHtml() {}
        public @RequestMapping(produces = "application/foo") void html() {}
        public @RequestMapping(produces = {"text/plain", "application/json"}) void textAndJson() {}
        public @RequestMapping(consumes = "application/bar") void bar() {}
    }

    public static @Controller @LinkRelationType("http://example.org/rel/fooType") class ControllerWithDifferentAllowsSpecifications {
        public @RequestMapping("/default") void defaultGet() {}
        public @RequestMapping(value = "/default", method = PUT) void putSomething() {}
        public @RequestMapping(value = "/default", method = {POST, DELETE, HEAD}) void someMoreMethods() {}
    }

    public static @Controller @RequestMapping("foo") @LinkRelationType("http://example.org/rel/fooType") class ControllerWithAllowsSpecAcrossMultipleMethods{
        public @RequestMapping() void defaultGet() {}
        public @RequestMapping(method = PUT) void putSomething() {}
        public @RequestMapping(method = {POST, DELETE}) void htmlAndJson() {}
    }

    public static @Controller class ControllerWithRequestMappingAndLinkRelationTypeAtMethodLevel {
        public @RequestMapping(value = "/bar") @LinkRelationType("/rel/foo") void bar() {}
    }

    public static @Controller @RequestMapping(value = "/bar") @LinkRelationType("/rel/foo") class ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel {
        public @RequestMapping void getAFoo() {}
    }

    public static @Controller @RequestMapping(value = "/bar") @LinkRelationType("/rel/foo") class AnotherControllerWithRequestMappingAndLinkRelationTypeAtClassLevel {
        public @RequestMapping(method = PUT) void putAFoo() {}
    }

    public static @Controller class ControllerWithMultipleLinkRelationTypes {
        public @RequestMapping(value = "/foo") @LinkRelationType("/rel/foo") void foo() {}
        public @RequestMapping(value = "/foo") @LinkRelationType("/rel/bar") void bar() {}
    }

}
