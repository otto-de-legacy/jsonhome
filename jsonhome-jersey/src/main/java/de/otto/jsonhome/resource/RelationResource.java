package de.otto.jsonhome.resource;

import com.sun.jersey.api.view.Viewable;
import de.otto.jsonhome.generator.JerseyJsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import de.otto.jsonhome.resource.scanner.AnnotationScanner;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.resource.Responses.addCacheControlHeaders;

/**
 * @author Sebastian Schroeder
 * @since 28.12.2012
 */
@Path("/rel/{all:.*}")
public final class RelationResource {

    private final JsonHomeSource jsonHomeSource;
    private int maxAge = 3600;

    public RelationResource(JsonHomeSource jsonHomeSource) {
        this.jsonHomeSource = jsonHomeSource;
    }

    public RelationResource() {
        this.jsonHomeSource = new JerseyJsonHomeSource(new JerseyJsonHomeGenerator(), new AnnotationScanner());
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getRelationType(@Context UriInfo uriInfo) {
        final Map<String,Object> model = new HashMap<String, Object>();
        final JsonHome jsonHome = jsonHomeSource.getJsonHome();
        if (jsonHome.hasResourceFor(uriInfo.getRequestUri())) {
            final ResourceLink resourceLink = jsonHome.getResourceFor(uriInfo.getRequestUri());
            model.put("resource", resourceLink);
            final Viewable viewable;
            if (resourceLink.isDirectLink()) {
                viewable = new Viewable("/jsonhome/directresource", model);
            } else {
                viewable = new Viewable("/jsonhome/templatedresource", model);
            }
            return addCacheControlHeaders(Response.ok(viewable), maxAge);
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Unknown relation type " + uriInfo.getRequestUri()).build();
        }
    }

}
