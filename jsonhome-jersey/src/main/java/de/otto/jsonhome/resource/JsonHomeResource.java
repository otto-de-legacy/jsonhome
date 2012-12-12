package de.otto.jsonhome.resource;

import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static de.otto.jsonhome.converter.JsonHomeConverter.toRepresentation;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSON;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;

/**
 * @author Sebastian Schroeder
 * @since 11.12.2012
 */
@Path("json-home")
public final class JsonHomeResource {

    private final JsonHomeSource jsonHomeSource;
    private int maxAge = 0;

    public JsonHomeResource(JsonHomeSource jsonHomeSource) {
        this.jsonHomeSource = jsonHomeSource;
    }
    /*
    public JsonHomeResource() {
        this.jsonHomeSource = new JerseyJsonHomeSource("http://www.example.org", "http://rel.example.org", Arrays.asList("de.otto"));
    }
    */

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    /*
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtmlHomeDocument(@Context final UriInfo uriInfo) {
        final JsonHome jsonHome = jsonHomeSource.getJsonHome();
        final Map<String,Object> resources = new HashMap<String, Object>();
        resources.put("resources", jsonHome.getResources().values());
        resources.put("contextpath", uriInfo.getPath());
        return addHeaders(Response.ok(resources));
    }
    */

    @GET
    @Produces("application/json-home")
    public Response getAsApplicationJsonHome() {
        final JsonHome jsonHome = jsonHomeSource.getJsonHome();
        return addHeaders(Response.ok(toRepresentation(jsonHome, APPLICATION_JSONHOME)));
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAsApplicationJson() {
        final JsonHome jsonHome = jsonHomeSource.getJsonHome();
        return addHeaders(Response.ok(toRepresentation(jsonHome, APPLICATION_JSON)));
    }

    private Response addHeaders(Response.ResponseBuilder builder) {
        return builder.
                header("Vary", "Accept").
                header("Cache-Control", "max-age=" + maxAge).build();
    }

}
