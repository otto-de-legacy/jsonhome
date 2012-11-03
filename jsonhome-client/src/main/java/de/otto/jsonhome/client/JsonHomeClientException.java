package de.otto.jsonhome.client;

/**
 * @author Guido Steinacker
 * @since 27.10.12
 */
public class JsonHomeClientException extends RuntimeException {

    public JsonHomeClientException(String message) {
        super(message);
    }

    public JsonHomeClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonHomeClientException(Throwable cause) {
        super(cause);
    }

}
