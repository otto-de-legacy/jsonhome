package de.otto.jsonhome.example.hellojersey;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Rel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Sebastian Schroeder
 * @since 11.12.2012
 */
@Path("/")
@Rel("/rel/hello-world-example")
@Doc(rel = "/rel/hello-world-example",
        value = "The hello-world example resource"
)
public class HelloWorldResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response helloWorld() {
        return Response.ok("Hello World!").build();
    }

}
