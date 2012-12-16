package de.otto.jsonhome.fixtures.one;

import de.otto.jsonhome.annotation.Rel;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/foo") @Rel("http://example.org/rel/fooType")
public class Foo {

    @POST
    public void foo() {}

}
