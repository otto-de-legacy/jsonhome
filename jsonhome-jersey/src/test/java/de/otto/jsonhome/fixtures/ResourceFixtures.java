package de.otto.jsonhome.fixtures;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Docs;
import de.otto.jsonhome.annotation.Hints;
import de.otto.jsonhome.annotation.Rel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;

import static de.otto.jsonhome.model.Precondition.ETAG;
import static de.otto.jsonhome.model.Status.DEPRECATED;

public class ResourceFixtures {

    public static @Path("") class ResourceWithoutResource {}

    public static @Path("/foo") class ResourceWithoutMethods {}

    public static @Path("/foo") class ResourceWithResourceWithoutLinkRelationType {
        public @GET @Path("/bar") void bar() {}
    }

    public static @Path("/foo") @Rel("http://example.org/rel/fooBarType") class ResourceWithResourceAndLinkRelationType {
        public @GET @Path("/bar") void bar() {}
    }

    public static @Path("/foo") class ResourceWithDifferentUrisForSameRelationType {
        public @GET @Rel("http://example.org/rel/fooType") void foo() {}
        public @GET @Path("/bar") @Rel("http://example.org/rel/fooType") void bar() {}
    }

    public static @Path("/foo") class ResourceWithRelativeLinkRelationType{
        public @GET @Rel("/rel/fooType") void foo() {}
    }

    public static @Path("/foo") class ResourceWithDifferentResourceDefinitions {
        public @GET @Rel("http://example.org/rel/fooType") void foo() {}
        public @GET @Path("/bar") @Rel("http://example.org/rel/barType") void bar() {}
        public @GET @Path("/foobar") @Rel("http://example.org/rel/fooBarType") void fooBar() {}
    }

    public static @Path("/foo") @Rel("http://example.org/rel/fooType") class ResourceWithDifferentRepresentations {
        public @GET void defaultHtml() {}
        public @GET @Produces("application/foo") void html() {}
        public @GET @Produces({"text/plain", "application/json"}) void textAndJson() {}
        public @GET @Consumes("application/bar") void bar() {}
    }

    public static @Path("") @Rel("http://example.org/rel/fooType") class ResourceWithDifferentAllowsSpecifications {
        public @GET @Path("/default") void defaultGet() {}
        public @PUT @Path("/default") void putSomething() {}
        public @POST @DELETE @HEAD @Path("/default") void someMoreMethods() {}
    }

    public static @Path("") @Rel("http://example.org/rel/fooType") class ResourceWithGetAndPostMethodWithDefaultAllowsSpec {
        public @GET @Path("/default") void defaultGet() {}
        public @POST @Path("/default") void defaultPost() {}
    }

    public static @Path("foo") @Rel("http://example.org/rel/fooType") class ResourceWithAllowsSpecAcrossMultipleMethods{
        public @GET void defaultGet() {}
        public @PUT void putSomething() {}
        public @POST @DELETE void htmlAndJson() {}
    }

    public static @Path("") class ResourceWithRequestMappingAndLinkRelationTypeAtMethodLevel {
        public @GET @Path("/bar") @Rel("/rel/foo") void bar() {}
    }

    public static @Path("/bar") @Rel("/rel/foo") class ResourceWithRequestMappingAndLinkRelationTypeAtClassLevel {
        public @GET void getAFoo() {}
    }

    public static @Path("/foo") @Rel("/rel/foo") class ResourceWithRequiredPrecondition {
        public @GET @Consumes("text/plain") @Hints(preconditionReq = ETAG) void putFoo(@Context String body) {}
    }

    public static @Path("/bar") @Rel("/rel/foo") class AnotherResourceWithRequestMappingAndLinkRelationTypeAtClassLevel {
        public @PUT void putAFoo() {}
    }

    public static @Path("") class ResourceWithMultipleLinkRelationTypes {
        public @GET @Path("/foo") @Rel("/rel/foo") void foo() {}
        public @GET @Path("/foo") @Rel("/rel/bar") void bar() {}
    }

    public static @Path("") @Rel("/rel/foo") class ResourceWithAcceptPutAndAcceptPost {
        public @GET @Path("/foo") @Produces("text/plain")  void getIt() {}
        public @POST @Path("/foo") @Consumes("foo/bar")  void postIt() {}
        public @PUT @Path("/foo") @Consumes("bar/foo") void putIt() {}
    }

    public static @Path("") class ResourceWithTemplatedResourceLink {
        public @GET @Rel("/rel/foo") @Path("/{bar}") void find(@PathParam("bar") String bar, @QueryParam("query") String query) {}
        public @GET @Rel("/rel/foobar") @Path("/{bar}/{foobar}") void find(@PathParam("bar") String bar, @PathParam("foobar") String foobar, @QueryParam("query") String query, @QueryParam("page") int page) {}
    }

    @Path("")
    @Docs({
            @Doc(value = "controller value", link = "http://example.org/doc/foo", rel = "/rel/foo"),
            @Doc(value = "a value", link = "http://example.org/doc/foo", rel = "/rel/bar")
    })
    public static class ResourceWithDocumentation {
        public
        @GET
        @Rel("/rel/foo")
        @Path("/{foo}")
        void foo(@PathParam("foo") @Doc("var value 1") String foo,
                 @QueryParam("requestParam") @Doc("var value 2") String requestParam) {}

        public
        @GET
        @Rel("/rel/bar")
        @Path("/{bar}")
        void bar(@PathParam("bar") @Doc("var value 1") String bar,
                 @QueryParam("requestParam") @Doc("var value 2") String requestParam) {}
    }

    @Path("/foo")
    public static class ResourceWithDeprecatedResource {
        public
        @GET
        @Rel("/rel/foo")
        @Hints(status = DEPRECATED)
        void foo() {}

        public
        @GET
        @Rel("/rel/bar")
        @Path("/bar")
        void bar() {}
    }

}
