/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.otto.jsonhome.client;

import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.parser.JacksonJsonHomeParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.*;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * A JsonHomeClient used to get json-home documents from an URI via HTTP.
 *
 * This implementation is relying on Apache's CachingHttpClient.
 *
 * @author Guido Steinacker
 * @since 26.10.12
 */
public class HttpJsonHomeClient implements JsonHomeClient {

    private static Logger LOG = LoggerFactory.getLogger(HttpJsonHomeClient.class);

    private final HttpClient httpClient;
    private final HttpCacheStorage cacheStorage;

    /**
     * Constructs a default HttpJsonHomeClient build on top of a CachingHttpClient with in-memory storage.
     */
    public HttpJsonHomeClient() {
        final CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(100)
                .setMaxObjectSize(50000)
                .build();
        this.cacheStorage = new BasicHttpCacheStorage(cacheConfig);
        this.httpClient = CachingHttpClientBuilder.create()
                .setHttpCacheStorage(cacheStorage)
                .setCacheConfig(cacheConfig)
                .build();
    }

    /**
     * Constructs a HttpJsonHomeClient using a HttpClient and a CacheConfig.
     *
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
            LOG.info("Expiring cached json-home document {}", uri);
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
        httpget.setHeader("Accept", "application/json");
        final BasicHttpContext context = new BasicHttpContext();
        final HttpResponse response;
        try {
            LOG.info("Getting json-home document {}", uri);
            response = httpClient.execute(httpget, context);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 404) {
                LOG.warn("Json-home document {} not found. HTTP status is 404", uri);
                throw new NotFoundException("Resource " + uri + " not found");
            } else if (statusCode >= 400) {
                final String status = response.getStatusLine().toString();
                LOG.warn("Json-home document {} not found: {}", uri, status);
                throw new HttpStatusException(statusCode,
                        "Failed to load json-home from " + uri +
                                ": Received HTTP status code " + status);
            }
        } catch (final IOException e) {
            LOG.warn("Error getting json-home document {}: {}", uri, e.getMessage());
            // in case of an IOException, the connection will be released automatically.
            throw new JsonHomeClientException("Error getting json-home document " + uri, e);
        } finally {
            httpget.reset();
        }
        final HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream stream = null;
            try {
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
