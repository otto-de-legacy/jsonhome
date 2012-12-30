package de.otto.jsonhome.resource;

import com.sun.jersey.api.view.Viewable;
import de.otto.jsonhome.converter.JsonHomeMediaType;
import de.otto.jsonhome.generator.JerseyJsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.resource.scanner.AnnotationScanner;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.converter.JsonHomeConverter.toRepresentation;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSON;
import static de.otto.jsonhome.converter.JsonHomeMediaType.APPLICATION_JSONHOME;
import static de.otto.jsonhome.resource.Responses.addCacheControlHeaders;

/**
 * @author Sebastian Schroeder
 * @since 11.12.2012
 */
@Path("/json-home")
public final class JsonHomeResource {

    private final JsonHomeSource jsonHomeSource;
    private int maxAge = 3600;

    public JsonHomeResource(JsonHomeSource jsonHomeSource) {
        this.jsonHomeSource = jsonHomeSource;
    }

    public JsonHomeResource() {
        this.jsonHomeSource = new JerseyJsonHomeSource(new JerseyJsonHomeGenerator(), new AnnotationScanner());
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getAsTextHtmlHome() {
        final Map<String,Object> resources = new HashMap<String, Object>();
        resources.put("resources", jsonHomeSource.getJsonHome().getResources().values());

        final Viewable viewable = new Viewable("/resources", resources);
        return addCacheControlHeaders(Response.ok(viewable), maxAge);
    }

    @GET
    @Produces("application/json-home")
    public Response getAsApplicationJsonHome() {
        final JsonHome jsonHome = jsonHomeSource.getJsonHome();
        try {
            return addCacheControlHeaders(Response.ok(toJsonString(jsonHome, APPLICATION_JSONHOME)), maxAge);
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAsApplicationJson() {
        final JsonHome jsonHome = jsonHomeSource.getJsonHome();
        try {
            return addCacheControlHeaders(Response.ok(toJsonString(jsonHome, APPLICATION_JSON)), maxAge);
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String toJsonString(JsonHome jsonHome, JsonHomeMediaType mediaType) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(toRepresentation(jsonHome, mediaType));
    }

}
