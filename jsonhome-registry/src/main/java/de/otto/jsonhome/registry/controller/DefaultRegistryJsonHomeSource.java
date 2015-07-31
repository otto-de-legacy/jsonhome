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

import de.otto.jsonhome.client.HttpJsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClientException;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import de.otto.jsonhome.registry.store.Link;
import de.otto.jsonhome.registry.store.RegistryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static de.otto.jsonhome.model.JsonHome.jsonHome;

/**
 * Provides access to a json-home document containing the merged json-home documents registered in the {@link de.otto.jsonhome.registry.store.RegistryRepository}.
 * <p/>
 * The service is responsible for retrieving all registered documents.
 *
 * @author Guido Steinacker
 * @since 20.11.12
 */
@Component
public class DefaultRegistryJsonHomeSource implements RegistryJsonHomeSource {

    private static Logger LOG = LoggerFactory.getLogger(DefaultRegistryJsonHomeSource.class);

    private final JsonHomeClient client;
    private RegistryRepository registries;

    public DefaultRegistryJsonHomeSource() {
        this.client = new HttpJsonHomeClient();
    }

    @PreDestroy
    public void shutdown() {
        LOG.info("Shutting down JsonHomeClient");
        client.shutdown();
    }

    @Autowired
    public void setRegistries(final RegistryRepository registries) {
        this.registries = registries;
    }

    /**
     * Returns the JsonHome document for the specified registryName.
     *
     * @param registryName the registryName (like develop, live) of this entry. Different registered environments
     *                    are used to access different versions of json-home documents during development.
     *                    The default registryName is ""; null is not accepted.
     * @return the json-home for the specified registryName.
     */
    public JsonHome getJsonHome(final String registryName) {
        if (registries.get(registryName) == null) {
            final String msg = "Registry '" + registryName + "' does not exist.";
            LOG.warn(msg);
            throw new IllegalArgumentException(msg);
        } else {
            final Map<URI, ResourceLink> allResourceLinks = new HashMap<>();
            for (final Link link : registries.get(registryName).getAll()) {
                try {
                    final JsonHome jsonHome = client.get(link.getHref());
                    final Map<URI, ResourceLink> resources = jsonHome.getResources();
                    for (final URI uri : resources.keySet()) {
                        if (allResourceLinks.containsKey(uri)) {
                            LOG.warn("Duplicate entries found for resource {}: entry '{}', is overridden by '{}'",
                                    uri, allResourceLinks.get(uri), resources.get(uri));
                        }
                        allResourceLinks.put(uri, resources.get(uri));
                    }
                    allResourceLinks.putAll(resources);
                } catch (final JsonHomeClientException e) {
                    LOG.warn("Unable to get json-home document {}: {}", link.getHref(), e.getMessage());
                    // After some retries, the json-home MAY automatically be unregistered here.
                }
            }
            LOG.debug("Returning json-home instance containing {} relation types: {}",
                    allResourceLinks.size(), allResourceLinks.keySet());
            return jsonHome(allResourceLinks.values());
        }
    }

}
