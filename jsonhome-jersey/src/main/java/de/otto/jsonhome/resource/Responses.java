package de.otto.jsonhome.resource;

import javax.ws.rs.core.Response;

/**
 * @author Sebastian Schroeder
 * @since 28.12.2012
 */
public final class Responses {

    private Responses() {}

    public static Response addCacheControlHeaders(Response.ResponseBuilder builder, int maxAge) {
        return builder.
                header("Vary", "Accept").
                header("Cache-Control", "max-age=" + maxAge).build();
    }

}
