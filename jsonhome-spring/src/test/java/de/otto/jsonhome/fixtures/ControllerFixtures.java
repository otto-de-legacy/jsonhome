/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.otto.jsonhome.fixtures;

import de.otto.jsonhome.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.otto.jsonhome.model.Precondition.ETAG;
import static de.otto.jsonhome.model.Precondition.LAST_MODIFIED;
import static de.otto.jsonhome.model.Status.DEPRECATED;
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

    public static @Controller @RequestMapping(value = "/foo") @Rel("/rel/foo") class ControllerWithRequiredPrecondition {
        public @RequestMapping(consumes = "text/plain") @Hints(preconditionReq = ETAG) void putFoo(@RequestBody String body) {}
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
    @Docs({
        @Doc(value = "controller value", link = "http://example.org/doc/foo", rel = "/rel/foo"),
        @Doc(value = "a value", link = "http://example.org/doc/foo", rel = "/rel/bar")
    })
    public static class ControllerWithDocumentation {
        public
        @Rel("/rel/foo")
        @RequestMapping(value = "/{foo}")
        void foo(@PathVariable @Doc("var value 1") String foo,
                 @RequestParam @Doc("var value 2") String requestParam) {}

        public
        @Rel("/rel/bar")
        @RequestMapping(value = "/{bar}")
        void bar(@PathVariable @Doc("var value 1") String bar,
                 @RequestParam @Doc("var value 2") String requestParam) {}
    }

    public static class ControllerWithInheritance extends ControllerWithDocumentation {
        public
        void foo(String foo, String requestParam) {}

        public
        void bar(String bar, String requestParam) {}
    }
static class TestAuth {
    private final String scheme;
    private final List<String> realms;

    TestAuth(String scheme, List<String> realms) {
        this.scheme = scheme;
        this.realms = realms;
    }
}
    @Controller
    @RequestMapping(value = "/foo")
    public static class ControllerWithHints {
        @Rel("/rel/foo")
        @RequestMapping("/dep")
        @Hints(status = DEPRECATED)
        public void deprecatedMethod() {}

        @Rel("/rel/bar")
        @RequestMapping("/pre")
        @Hints(preconditionReq = {ETAG, LAST_MODIFIED})
        public void methodWithPreconditionsRequired() {}

        @Rel("/rel/preferHint")
        @RequestMapping("/prefer")
        @Hints(prefer = {"return-representation=application/json", "return-asynch"})
        public void methodWithPreferHint() {}

        @Rel("/rel/acceptRangesHint")
        @RequestMapping("/acceptRanges")
        @Hints(acceptRanges = {"bytes"})
        public void methodWithAcceptRangesHint() {}

        @Rel("/rel/foobar")
        @RequestMapping("/auth")
        @Hints(authReq = {
                @Auth(scheme="Basic", realms={"foo"}),
                @Auth(scheme="Digest", realms={"bar"})
        })
        public void methodWithAuthRequired() {}
    }

    @Controller
    public static class ControllerWithDifferentProducesAndConsumes {
        @RequestMapping(
                method = RequestMethod.POST,
                consumes = "application/x-www-form-urlencoded",
                produces = "text/html"
        )
        @Rel("/rel/product/form")
        public void postSomething(final String foo) {}
    }

}
