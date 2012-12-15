package de.otto.jsonhome.fixtures.one;

import de.otto.jsonhome.annotation.Rel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/bar") @Rel("http://example.org/rel/barType")
public class Bar {

    @GET
    public void bar() {};
}
