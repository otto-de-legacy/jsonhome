package de.otto.jsonhome.client;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import de.otto.jsonhome.model.JsonHome;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutionException;

/**
 * A JsonHomeClient used to get json-home documents from an URI via HTTP.
 * <p/>
 * This implementation is relying on sonatype async-http-client.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class HttpJsonHomeClient implements JsonHomeClient {

    /**
     * Gets a JsonHome document identified by the specified URI.
     * @param uri the URI of the JsonHome document.
     * @return JsonHome.
     * @throws NotFoundException if the requested resource does not exist.
     * @throws HttpStatusException if an HTTP client or server error is returned.
     */
    @Override
    public JsonHome get(final URI uri) {
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        InputStream stream = null;
        try {
            final Response response = httpClient.prepareGet(uri.toString())
                    .addHeader("Accept", "application/json-home")
                    .setFollowRedirects(true)
                    .execute()
                    .get();
            if (response.getStatusCode() == 404) {
                throw new NotFoundException("Resource " + uri + " not found");
            } else if (response.getStatusCode() >= 400) {
                throw new HttpStatusException( response.getStatusCode(),
                        "Failed to load json-home from " + uri +
                                ": Received HTTP status code " + response.getStatusCode() +
                                " (" + response.getStatusText() + ")");
            }
            stream = response.getResponseBodyAsStream();
            return new JacksonJsonHomeParser().fromStream(stream).parse();
        } catch (IOException e) {
            throw new RuntimeException("Exception caught while getting json-home from " + uri, e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception caught while getting json-home from " + uri, e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Exception caught while getting json-home from " + uri, e);
        }finally {
            httpClient.close();
            try {
                if (stream != null) stream.close();
            } catch (IOException e) { /* ignore*/ }
        }
    }

}
