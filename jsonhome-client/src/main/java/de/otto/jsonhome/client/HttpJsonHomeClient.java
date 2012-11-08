package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.parser.JacksonJsonHomeParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.BasicHttpCacheStorage;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.protocol.BasicHttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * A JsonHomeClient used to get json-home documents from an URI via HTTP.
 * <p/>
 * This implementation is relying on Apache's CachingHttpClient.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class HttpJsonHomeClient implements JsonHomeClient {

    private final HttpClient httpClient;
    private final HttpCacheStorage cacheStorage;

    /**
     * Constructs a default HttpJsonHomeClient build on top of a CachingHttpClient with in-memory storage.
     */
    public HttpJsonHomeClient() {
        final CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setMaxCacheEntries(100);
        cacheConfig.setMaxObjectSize(50000);
        this.cacheStorage = new BasicHttpCacheStorage(cacheConfig);
        this.httpClient = new CachingHttpClient(new DefaultHttpClient(), cacheStorage, cacheConfig);
    }

    /**
     * Constructs a HttpJsonHomeClient using a HttpClient and a CacheConfig.
     * <p/>
     * Internally, these two are used to build a CachingHttpClient using a BasicHttpCacheStorage.
     *
     * @param httpClient non-caching HttpClient used to get resources.
     * @param cacheConfig configuration of the HttpCacheStorage
     */
    public HttpJsonHomeClient(final HttpClient httpClient, final CacheConfig cacheConfig) {
        this.cacheStorage = new BasicHttpCacheStorage(cacheConfig);
        this.httpClient = new CachingHttpClient(httpClient, cacheStorage, cacheConfig);
    }

    /**
     * Constructs a caching HttpJsonHomeClient using a HttpClient, HttpCacheStorage and a CacheConfig.
     *
     * @param httpClient non-caching HttpClient used to get resources.
     * @param storage the HttpCacheStorage used to cache HTTP responses.
     * @param cacheConfig configuration of the HttpCacheStorage
     */
    public HttpJsonHomeClient(final HttpClient httpClient,
                              final HttpCacheStorage storage,
                              final CacheConfig cacheConfig) {
        this.cacheStorage = storage;
        this.httpClient = new CachingHttpClient(httpClient, cacheStorage, cacheConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonHome updateAndGet(final URI uri) {
        try {
            cacheStorage.removeEntry(uri.toString());
        } catch (IOException e) {
            throw new JsonHomeClientException("IOException caught while removing cache-entry: " + e.getMessage(), e);
        }
        return get(uri);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonHome get(final URI uri) {
        final HttpGet httpget = new HttpGet(uri);
        final BasicHttpContext context = new BasicHttpContext();
        final HttpResponse response;
        try {
            response = httpClient.execute(httpget, context);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 404) {
                throw new NotFoundException("Resource " + uri + " not found");
            } else if (statusCode >= 400) {
                throw new HttpStatusException(statusCode,
                        "Failed to load json-home from " + uri +
                                ": Received HTTP status code " + response.getStatusLine().toString());
            }
        } catch (final IOException e) {
            // in case of an IOException, the connection will be released automatically.
            throw new JsonHomeClientException("Exception caught while getting json-home from " + uri, e);
        }
        final HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream stream = null;
            try {
                /*
                CacheResponseStatus responseStatus = (CacheResponseStatus) context.getAttribute(
                        CachingHttpClient.CACHE_RESPONSE_STATUS);
                switch (responseStatus) {
                    case CACHE_HIT:
                        System.out.println("A response was generated from the cache with no requests " +
                                "sent upstream");
                        break;
                    case CACHE_MODULE_RESPONSE:
                        System.out.println("The response was generated directly by the caching module");
                        break;
                    case CACHE_MISS:
                        System.out.println("The response came from an upstream server");
                        break;
                    case VALIDATED:
                        System.out.println("The response was generated from the cache after validating " +
                                "the entry with the origin server");
                        break;
                }
                */
                stream = entity.getContent();
                return new JacksonJsonHomeParser().parse(stream);
            } catch (final IOException e) {
                // in case of an IOException, the connection will be released automatically.
                throw new JsonHomeClientException("Exception caught while getting json-home from " + uri, e);
            } catch (final RuntimeException e) {
                httpget.abort();
                throw new JsonHomeClientException("Exception caught while getting json-home from " + uri, e);
            } finally {
                if (stream != null) try { stream.close(); } catch (IOException e) { /* ignore */ }
            }
        }
        throw new JsonHomeClientException("No content returned when getting json-home resource from " + uri);
    }

    @Override
    public void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }

}
