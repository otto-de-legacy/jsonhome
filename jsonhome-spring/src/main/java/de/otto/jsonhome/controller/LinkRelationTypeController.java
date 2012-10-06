package de.otto.jsonhome.controller;

import de.otto.jsonhome.model.ResourceLink;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;


/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/rel")
public class LinkRelationTypeController extends JsonHomeControllerBase {

    @RequestMapping(
            value = "/{relationType}",
            method = RequestMethod.GET,
            produces = "text/html"
    )
    public ModelAndView getRelationshipType(final @PathVariable String relationType,
                                            final HttpServletRequest request) {
        final URI relationTypeURI = URI.create(rootUri().toString() + "/rel/" + relationType);
        final Map<String,Object> model = new HashMap<String, Object>();
        model.put("contextpath", request.getContextPath());
        if (jsonHome().hasResourceFor(relationTypeURI)) {
            final ResourceLink resourceLink = jsonHome().getResourceFor(relationTypeURI);
            model.put("resource", resourceLink);
            if (resourceLink.isDirectLink()) {
                return new ModelAndView("doc/directresource", model);
            } else {
                return new ModelAndView("doc/templatedresource", model);
            }
        } else {
            throw new IllegalArgumentException("Unknown relation type " + relationType);
        }
    }

    @ResponseStatus(value = NOT_FOUND)
    @ExceptionHandler({IllegalArgumentException.class})
    public void handleNotFound(final HttpServletResponse response) throws IOException {
        response.sendError(404, "Unknown link-relation type.");
    }
}
