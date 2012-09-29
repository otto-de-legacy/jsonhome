package de.steinacker.jsonhome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;


/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/rel")
public class LinkRelationTypeController {

    public LinkRelationTypeController() {
    }

    @RequestMapping(
            value = "/{relationType}",
            method = RequestMethod.GET,
            produces = "text/html"
    )
    public String getRelationshipType(final @PathVariable String relationType) {
        if (relationType.equals("index")) {
            return "redirect:http://www.w3.org/TR/1999/REC-html401-19991224/";
        }
        throw new IllegalArgumentException("Unknown relation type " + relationType);
    }

    @ResponseStatus(value = NOT_FOUND)
    @ExceptionHandler({IllegalArgumentException.class})
    public void handleNotFound() {
    }
}
