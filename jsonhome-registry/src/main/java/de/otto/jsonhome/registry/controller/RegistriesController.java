/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Docs;
import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.registry.store.JsonHomeRef;
import de.otto.jsonhome.registry.store.Registries;
import de.otto.jsonhome.registry.store.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static de.otto.jsonhome.registry.controller.RegistriesConverter.*;
import static java.util.UUID.randomUUID;
import static javax.servlet.http.HttpServletResponse.*;

/**
 * Controller responsible for requests to the <code>/registry</code> resource.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@Docs({
        @Doc(rel = "/rel/jsonhome/registries",
             value = "The collection of known registries."),
        @Doc(rel = "/rel/jsonhome/registry",
             value = {
                     "A registry of json-home documents",
                     "In order to registry a json-home document, POST an entry:",
                     " {'href' : 'http://example.org/json-home', 'title' : 'My example json-home document'}"
             }),
        @Doc(rel = "/rel/jsonhome/registry-entry",
             value = {"A single registry entry, referring to a json-home document."})
})
public class RegistriesController {

    private static final Logger LOG = LoggerFactory.getLogger(RegistriesController.class);

    private Registries registries;
    private URI applicationBaseUri;

    @Value("${jsonhome.applicationBaseUri}")
    public void setApplicationBaseUri(final String baseUri) {
        this.applicationBaseUri = URI.create(baseUri);
        LOG.info("ApplicationbaseUri is {}", applicationBaseUri.toString());
    }

    /**
     * Injects the registry implementation used to store registry entries.
     *
     * @param registries the Registry used by the controller.
     */
    @Autowired
    public void setRegistries(final Registries registries) {
        this.registries = registries;
    }

    @Rel("/rel/jsonhome/registries")
    @RequestMapping(
            value = "/registries",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public Map<String, List<String>> getRegistries() {
        final Set<String> registryNames = this.registries.getKnownRegistryNames();
        final List<String> registryNameList = new ArrayList<String>(registryNames);
        Collections.sort(registryNameList);
        return registriesToMap(applicationBaseUri, registryNameList);
    }

    @Rel("/rel/jsonhome/registry")
    @RequestMapping(
            value = "/registries/{registryName}",
            method = RequestMethod.PUT)
    public void createRegistry(@PathVariable final String registryName,
                               final HttpServletResponse response) {
        if (registries.getRegistry(registryName) == null) {
            this.registries.createRegistry(registryName);
            response.setStatus(SC_CREATED);
        } else {
            try {
                response.sendError(SC_CONFLICT, "Registry '" + registryName + "' already exists.");
            } catch (IOException e) { }
        }
    }

    @Rel("/rel/jsonhome/registry")
    @RequestMapping(
            value = "/registries/{registryName}",
            method = RequestMethod.DELETE)
    public void deleteRegistry(@PathVariable final String registryName,
                               final HttpServletResponse response) {
        this.registries.deleteRegistry(registryName);
        response.setStatus(SC_OK);
    }

    /**
     * Returns the contents of the registry in application/json format. The returned document will look like this:
     * <code><pre>
     *     {
     *         "&lt;registryName>" : [
     *              {
     *                  "item" : "&lt;uri of the entry>",
     *                  "title" : "Some title",
     *                  "href" : "&lt;uri of the json-home>",
     *              },
     *              {
     *                  ...
     *              }
     *         ]
     *     }
     * </pre></code>
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
        final Registry registry = registries.getRegistry(registryName);
        LOG.info("Returning registry containing {} entries.", registry.getAll().size());
        response.setHeader("Cache-Control", "max-age=3600");
        return registryEntriesToMap(registry);
    }

    /**
     * Returns a single registry entry in application/json format. The returned document will look like this:
     * <code><pre>
     *     {
     *          "title" : "Some title",
     *          "href" : "&lt;uri of the json-home>",
     *     }
     * </pre></code>
     * @param response HttpServletResponse with cache-control header and application/json in body.
     * @return application/json
     */
    @Rel("/rel/jsonhome/registry-entry")
    @RequestMapping(
            value = "/registries/{registryName}/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public Map<String, String> getEntry(@PathVariable
                                        @Doc("The name of the requested registry.")
                                        final String registryName,
                                        @PathVariable
                                        @Doc("Identifier of the registry entry.")
                                        final String id,
                                        final HttpServletResponse response) throws IOException {
        if (registries.getRegistry(registryName) == null) {
            response.sendError(SC_NOT_FOUND, "Registry " + registryName + " does not exist.");
            return null;
        } else {
            response.setHeader("Cache-Control", "max-age=3600");
            final URI uri = locationUri(registryName, id);
            final JsonHomeRef entry = registries.getRegistry(registryName).findBy(uri);
            if (entry == null) {
                LOG.info("Entry {} not found in registry.", uri);
                response.setStatus(SC_NOT_FOUND);
                return null;
            } else {
                LOG.info("Returning entry {}", uri);
                final Map<String, String> map = registryEntryToMap(entry);
                map.put("collection", applicationBaseUri.toString() + "/registries");
                return map;
            }
        }
    }

    /**
     * Registers a new json-home document.
     * <p/>
     * If registration is successful, the 'Location' header is used to return the URI of the generated registry entry.
     * In this case, HTTP 201 created is returned.
     * <p/>
     * If the URI of the json-home document is already registered, HTTP 409 conflict is returned.
     * In this case the registry is not changed.
     * <p/>
     * The body of the request is expected to have the following attributes in json format:
     * <code><pre>
     *     {
     *          "title" : "Some title",
     *          "href" : "&lt;uri of the json-home>",
     *     }
     * </pre></code>
     * <p/>
     * HTTP status codes returned by this method:
     * <ul>
     *     <li>201 created: If the entry was successfully registered.</li>
     *     <li>400 bad request: The body of the request is not conforming to the described format.</li>
     *     <li>409 conflict: The URI of the json-home document is already registered.</li>
     * </ul>
     * @param entry application/json document, like { href="http://example.org", title="Example" }
     * @param response HttpServletResponse
     */
    @Rel("/rel/jsonhome/registry")
    @RequestMapping(
            value = "/registries/{registryName}",
            method = RequestMethod.POST,
            consumes = "application/json")
    public void register(@PathVariable
                         @Doc("The name of registry.")
                         final String registryName,
                         @RequestBody
                         final Map<String, String> entry,
                         final HttpServletResponse response) throws IOException {
        if (registries.getRegistry(registryName) == null) {
            response.sendError(SC_NOT_FOUND, "Registry " + registryName + " does not exist.");
        } else if (!isValid(entry)) {
            LOG.info("Entry {} is not valid.", entry);
            response.sendError(SC_BAD_REQUEST, "The request does not contain a valid entry.");
        } else {
            final URI location = locationUri(registryName, randomUUID().toString());
            entry.put("self", location.toString());
            try {
                LOG.info("Registering new registry-entry {}", location);
                registries.getRegistry(registryName).put(registryEntryFromMap(entry));
                response.setHeader("Location", location.toString());
                response.setStatus(SC_CREATED);
            } catch (final IllegalArgumentException e) {
                LOG.info("Entry {} is already registered with a different id", location);
                // href is already registered under different URI
                response.sendError(SC_CONFLICT, "The referred json-home is already registered with a different id.");
            }
        }
    }

    /**
     * Registers or updates a json-home document.
     * <p/>
     * If the home document is registered, <code>HTTP 201 created</code> is returned,
     * if updated <code>HTTP 204 no content</code>.
     * <p/>
     * If the URI of the json-home document is already registered, <code>HTTP 409 conflict</code> is returned.
     * In this case the registry is not changed.
     * <p/>
     * The body of the request is expected to have the following attributes in json format:
     * <code><pre>
     *     {
     *          "title" : "Some title",
     *          "href" : "&lt;uri of the json-home>",
     *     }
     * </pre></code>
     * Attribute <code>self</code> inside the document will be ignored.
     * <p/>
     * HTTP status codes returned by this method:
     * <ul>
     *     <li>201 created: If the entry was registered.</li>
     *     <li>204 no content: The entry was updated.</li>
     *     <li>400 bad request: The body of the request is not conforming to the described format.</li>
     *     <li>409 conflict: The URI of the json-home document is already registered.</li>
     * </ul>
     * @param entry application/json document, like { href="http://example.org", title="Example" }
     * @param response HttpServletResponse
     */
    @Rel("/rel/jsonhome/registry-entry")
    @RequestMapping(
            value = "/{registryName}/{id}",
            method = RequestMethod.PUT,
            consumes = "application/json")
    public void registerOrUpdate(@PathVariable @Doc("The name of registry.")
                                 final String registryName,
                                 @PathVariable @Doc("Identifier of the registry entry.")
                                 final String id,
                                 @RequestBody
                                 final Map<String, String> entry,
                                 final HttpServletResponse response) throws IOException {
        if (registries.getRegistry(registryName) == null) {
            response.sendError(SC_CONFLICT, "The registry does not exist.");
        } else if (!isValid(entry)) {
            response.sendError(SC_BAD_REQUEST, "The request does not contain a valid entry.");
        } else {
            final URI location = locationUri(registryName, id);
            entry.put("self", location.toString());
            try {
                if (registries.getRegistry(registryName).put(registryEntryFromMap(entry))) {
                    response.setStatus(SC_CREATED);
                } else {
                    response.setStatus(SC_NO_CONTENT);
                }

            } catch (final IllegalArgumentException e) {
                // href is already registered under different URI
                response.sendError(SC_CONFLICT, "The referred json-home is already registered with a different id.");
            }
        }
    }

    @RequestMapping(
            value = "/{registryName}/{id}",
            method = RequestMethod.DELETE)
    public void unregister(@PathVariable @Doc("The name of registry.")
                           final String registryName,
                           @PathVariable @Doc("Identifier of the registry entry.")
                           final String id,
                           final HttpServletResponse response) {
        registries.getRegistry(registryName).remove(locationUri(registryName, id));
        response.setStatus(SC_NO_CONTENT);
    }

    private boolean isValid(Map<String, ?> entry) {
        try {
            final String title = entry.get("title").toString();
            final URI href = URI.create(entry.get("href").toString());
            return !title.isEmpty() && href.isAbsolute();
        } catch (final Exception e) {
            return false;
        }
    }

    private URI locationUri(final String registryName, final String id) {
        return URI.create(applicationBaseUri + "/registries/" + registryName + "/" + id);
    }
}
