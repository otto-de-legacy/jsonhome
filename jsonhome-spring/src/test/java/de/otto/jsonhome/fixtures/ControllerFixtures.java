/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.fixtures;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Rel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    public static @Controller @RequestMapping("/foo") @Rel("http://example.org/rel/fooBarType") class ControllerWithResourceAndLinkRelationType {
        public @RequestMapping("/bar") void bar() {}
    }

    public static @Controller @RequestMapping("/foo") class ControllerWithDifferentUrisForSameRelationType {
        public @RequestMapping @Rel("http://example.org/rel/fooType") void foo() {}
        public @RequestMapping("/bar") @Rel("http://example.org/rel/fooType") void bar() {}
    }

    public static @Controller @RequestMapping("/foo") class ControllerWithRelativeLinkRelationType{
        public @RequestMapping @Rel("/rel/fooType") void foo() {}
    }

    public static @Controller @RequestMapping("/foo") class ControllerWithDifferentResourceDefinitions {
        public @RequestMapping @Rel("http://example.org/rel/fooType") void foo() {}
        public @RequestMapping("/bar") @Rel("http://example.org/rel/barType") void bar() {}
        public @RequestMapping("/foobar") @Rel("http://example.org/rel/fooBarType") void fooBar() {}
    }

    public static @Controller @RequestMapping("/foo") @Rel("http://example.org/rel/fooType") class ControllerWithDifferentRepresentations {
        public @RequestMapping void defaultHtml() {}
        public @RequestMapping(produces = "application/foo") void html() {}
        public @RequestMapping(produces = {"text/plain", "application/json"}) void textAndJson() {}
        public @RequestMapping(consumes = "application/bar") void bar() {}
    }

    public static @Controller @Rel("http://example.org/rel/fooType") class ControllerWithDifferentAllowsSpecifications {
        public @RequestMapping("/default") void defaultGet() {}
        public @RequestMapping(value = "/default", method = PUT) void putSomething() {}
        public @RequestMapping(value = "/default", method = {POST, DELETE, HEAD}) void someMoreMethods() {}
    }

    public static @Controller @Rel("http://example.org/rel/fooType") class ControllerWithGetAndPostMethodWithDefaultAllowsSpec {
        public @RequestMapping("/default") void defaultGet() {}
        public @RequestMapping(value = "/default", method = POST) void defaultPost(@PathVariable String foo) {}
    }

    public static @Controller @RequestMapping("foo") @Rel("http://example.org/rel/fooType") class ControllerWithAllowsSpecAcrossMultipleMethods{
        public @RequestMapping() void defaultGet() {}
        public @RequestMapping(method = PUT) void putSomething() {}
        public @RequestMapping(method = {POST, DELETE}) void htmlAndJson() {}
    }

    public static @Controller class ControllerWithRequestMappingAndLinkRelationTypeAtMethodLevel {
        public @RequestMapping(value = "/bar") @Rel("/rel/foo") void bar() {}
    }

    public static @Controller @RequestMapping(value = "/bar") @Rel("/rel/foo") class ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel {
        public @RequestMapping void getAFoo() {}
    }

    public static @Controller @RequestMapping(value = "/bar") @Rel("/rel/foo") class AnotherControllerWithRequestMappingAndLinkRelationTypeAtClassLevel {
        public @RequestMapping(method = PUT) void putAFoo() {}
    }

    public static @Controller class ControllerWithMultipleLinkRelationTypes {
        public @RequestMapping(value = "/foo") @Rel("/rel/foo") void foo() {}
        public @RequestMapping(value = "/foo") @Rel("/rel/bar") void bar() {}
    }

    public static @Controller @Rel("/rel/foo") class ControllerWithAcceptPutAndAcceptPost {
        public @RequestMapping(value = "/foo", method = RequestMethod.GET, produces = "text/plain")  void getIt() {}
        public @RequestMapping(value = "/foo", method = RequestMethod.POST, consumes = "foo/bar")  void postIt() {}
        public @RequestMapping(value = "/foo", method = RequestMethod.PUT, consumes = "bar/foo") void putIt() {}
    }

    public static @Controller class ControllerWithTemplatedResourceLink {
        public @Rel("/rel/foo") @RequestMapping(value = "/{bar}") void find(@PathVariable String bar, @RequestParam String query) {}
        public @Rel("/rel/foobar") @RequestMapping(value = "/{bar}/{foobar}") void find(@PathVariable String bar, @PathVariable String foobar, @RequestParam String query, @RequestParam int page) {}
    }

    @Controller
    @Doc(value = "controller value", link = "http://example.org/doc/foo")
    public static class ControllerWithDocumentation {
        public
        @Rel("/rel/foo")
        @RequestMapping(value = "/{foo}")
        void foo(@PathVariable @Doc("var value 1") String foo,
                 @RequestParam @Doc("var value 2") String requestParam) {}

        public
        @Rel("/rel/bar")
        @Doc("a value")
        @RequestMapping(value = "/{bar}")
        void bar(@PathVariable @Doc("var value 1") String bar,
                 @RequestParam @Doc("var value 2") String requestParam) {}
    }

}
