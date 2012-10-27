package de.otto.jsonhome.client;

/**
 * @author Guido Steinacker
 * @since 27.10.12
 */
public class NotFoundException extends HttpStatusException {

    public NotFoundException(final String message) {
        super(404, message);
    }

}
