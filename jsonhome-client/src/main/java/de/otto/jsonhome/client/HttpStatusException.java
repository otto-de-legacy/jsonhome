package de.otto.jsonhome.client;

/**
 * @author Guido Steinacker
 * @since 27.10.12
 */
public class HttpStatusException extends RuntimeException {

    private final int httpStatusCode;

    public HttpStatusException(final int httpStatusCode, final String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
