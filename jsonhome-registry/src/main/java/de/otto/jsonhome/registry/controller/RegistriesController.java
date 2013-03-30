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

package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Docs;
import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.registry.store.Registry;
import de.otto.jsonhome.registry.store.RegistryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;

import static de.otto.jsonhome.generator.UriBuilder.normalized;
import static de.otto.jsonhome.registry.controller.RegistriesConverter.registriesToJson;
import static de.otto.jsonhome.registry.controller.RegistryConverter.jsonToRegistry;
import static de.otto.jsonhome.registry.controller.RegistryConverter.registryToJson;
import static java.net.URI.create;
import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Controller responsible for requests to the <code>/registry</code> resource.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@Docs({
        @Doc(rel = "/rel/jsonhome/registries",
             value = {"The collection of known registries:",
                     "<pre><code>{\n" +
                     "      \"self\" : \"http://example.org/registries\",\n" +
                     "      \"registries\" : [\n" +
                     "          {\n" +
                     "              \"href\" : \"http://example.org/registries/live\"\n" +
                     "              \"title\" : \"Home documents of the live environment\",\n" +
                     "          },\n" +
                     "          {\n" +
                     "              \"href\" : \"http://example.org/registries/test\n" +
                     "              \"title\" : \"Home documents of the testing environment\",\n" +
                     "          }\n" +
                     "      ]\n" +
                     "}\n" +
                     "</pre></code>"
             }
        ),
        @Doc(rel = "/rel/jsonhome/registry",
             value = {
                     "A registry of json-home documents:",
                     "<pre><code>{\n" +
                     "      \"name\" : \"live\",\n" +
                     "      \"title\" : \"Home documents of the live environment\",\n" +
                     "      \"self\" : \"http://example.org/registries/live\",\n" +
                     "      \"container\" : \"http://example.org/registries\",\n" +
                     "      \"service\" : [\n" +
                     "          {\n" +
                     "              \"href\" : \"http://example.org/foo/json-home\"\n" +
                     "              \"title\" : \"Home document of application foo\",\n" +
                     "          },\n" +
                     "          {\n" +
                     "              \"href\" : \"http://example.org/bar/json-home\n" +
                     "              \"title\" : \"Home document of application bar\",\n" +
                     "          }\n" +
                     "      ]\n" +
                     "}\n" +
                     "</pre></code>"
             })
})
public class RegistriesController {

    private static final Logger LOG = LoggerFactory.getLogger(RegistriesController.class);

    private RegistryRepository registryRepository;
    private URI applicationBaseUri;

    @Value("${jsonhome.applicationBaseUri}")
    public void setApplicationBaseUri(final String baseUri) {
        this.applicationBaseUri = normalized(baseUri).toUri();
        LOG.info("ApplicationbaseUri is {}", applicationBaseUri.toString());
    }

    /**
     * Injects the registry implementation used to store registry entries.
     *
     * @param registryRepository the Registry used by the controller.
     */
    @Autowired
    public void setRegistryRepository(final RegistryRepository registryRepository) {
        this.registryRepository = registryRepository;
    }

    /**
     * Returns the registries as a list of URLs.
     *
     * <pre><code>
     *     GET /registries
     *
     *     {
     *          "self" : "http://example.org/registries",
     *          "registries" : [
     *              { "href" : "http://example.org/registries/live", "title" : "Live environment" },
     *              { "href" : "http://example.org/registries/test", "title" : "Testing environment" }
     *          ]
     *     }
     * </code></pre>
     * <p/>
     * HTTP status codes returned by this method:
     * <ul>
     *     <li>200 OK: if the resource was successfully returned.</li>
     * </ul>
     * @param response the HttpServletResponse
     */
    @Rel("/rel/jsonhome/registries")
    @RequestMapping(
            value = "/registries",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public Map<String, ?> getRegistries(final HttpServletResponse response) {
        response.setStatus(SC_OK);
        return registriesToJson(applicationBaseUri, registryRepository);
    }

    /**
     * Returns the contents of the registry in application/json format.
     *
     * <code><pre>
     *     GET /registries/live
     *
     *     {
     *         "name" : "live",
     *         "title" : "Live environment",
     *         "self" : "http://example.org/registries/live",
     *         "container" : "http://example.org/registries",
     *         "service" : [
     *              {
     *                  "title" : "Home document of application foo",
     *                  "href" : "http://example.org/foo/json-home"
     *              },
     *              {
     *                  "title" : "Home document of application bar",
     *                  "href" : "http://example.org/bar/json-home
     *              }
     *         ]
     *     }
     * </pre></code>
     *
     * The attributes 'name', 'self' and 'container' are added by the server and will be ignored during PUT operations.
     * <p/>
     * HTTP status codes returned by this method:
     * <ul>
     *     <li>200 OK: if the resource was found.</li>
     *     <li>404 NOT FOUND: if the document was not found.</li>
     * </ul>
     *
     * @param response HttpServletResponse with cache-control header and application/json in body.
     * @return application/json
     */
    @Rel("/rel/jsonhome/registry")
    @RequestMapping(
            value = "/registries/{registryName}",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public Map<String, ?> getRegistry(@PathVariable
                                      @Doc("The name of the requested registry.")
                                      final String registryName,
                                      final HttpServletResponse response) {
        final Registry registry = registryRepository.get(registryName);
        if (registry != null) {
            LOG.info("Returning links containing {} entries.", registry.getAll().size());
            response.setHeader("Cache-Control", "max-age=3600");
            return registryToJson(applicationBaseUri, registry);
        } else {
            LOG.info("Links {} does not exist", registryName);
            response.setStatus(SC_NOT_FOUND);
            return null;
        }
    }

    /**
     * Creates or updates a registry.
     *
     * <pre><code>
     *     PUT /registries/live
     *
     *     {
     *         "title" : "Live environment",
     *         "service" : [
     *              {
     *                  "title" : "Home document of application foo",
     *                  "href" : "http://example.org/foo/json-home"
     *              },
     *              {
     *                  "title" : "Home document of application bar",
     *                  "href" : "http://example.org/bar/json-home
     *              }
     *         ]
     *     }
     * </code></pre>
     *
     * The server will add the following attributes to the document: 'name', 'self', 'container'. These attributes
     * are overwritten, if provided by the caller.
     * <p/>
     * HTTP status codes returned by this method:
     * <ul>
     *     <li>201 CREATED: if the resource was successfully created.</li>
     *     <li>204 NO CONTENT: if the resource was successfully updated.</li>
     *     <li>400 BAD REQUEST: if the document was syntactically incorrect.</li>
     * </ul>
     */
    @Rel("/rel/jsonhome/registry")
    @RequestMapping(
            value = "/registries/{registryName}",
            method = RequestMethod.PUT)
    public void putRegistry(@PathVariable
                            @Doc("The name of registry.")
                            final String registryName,
                            @RequestBody
                            final Map<String, Object> registry,
                            final HttpServletResponse response) {
        if (registryRepository.get(registryName) == null) {
            response.setStatus(SC_CREATED);
        } else {
            response.setStatus(SC_NO_CONTENT);
        }

        registry.put("name", registryName);
        this.registryRepository.createOrUpdate(jsonToRegistry(registry));
    }

    /**
     * Deletes the specified registry.
     * <p/>
     * HTTP status codes returned by this method:
     * <ul>
     *     <li>204 NO CONTENT: if the resource was successfully deleted or did not exist.</li>
     * </ul>
     * @param registryName the name of the deleted registry
     * @param response the response object
     */
    @Rel("/rel/jsonhome/registry")
    @RequestMapping(
            value = "/registries/{registryName}",
            method = RequestMethod.DELETE)
    public void deleteRegistry(@PathVariable final String registryName,
                               final HttpServletResponse response) {
        this.registryRepository.delete(registryName);
        response.setStatus(SC_NO_CONTENT);
    }

    @ResponseStatus(value = BAD_REQUEST, reason = "Illegal resource format")
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public void handleBadRequest() {}

}
