package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.client.HttpJsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClientException;
import de.otto.jsonhome.client.NotFoundException;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import de.otto.jsonhome.registry.store.Registry;
import de.otto.jsonhome.registry.store.RegistryEntry;
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
 * Provides access to a json-home document containing the merged json-home documents registered in the {@link de.otto.jsonhome.registry.store.Registry}.
 * <p/>
 * The service is responsible for retrieving all registered documents.
 *
 * @author Guido Steinacker
 * @since 20.11.12
 */
@Component
public class RegistryBasedJsonHomeSource implements JsonHomeEnvSource {

    private static Logger LOG = LoggerFactory.getLogger(RegistryBasedJsonHomeSource.class);

    private final JsonHomeClient client;
    private Registry registry;

    public RegistryBasedJsonHomeSource() {
        this.client = new HttpJsonHomeClient();
    }

    @PreDestroy
    public void shutdown() {
        LOG.info("Shutting down JsonHomeClient");
        client.shutdown();
    }

    @Autowired
    public void setRegistry(final Registry registry) {
        this.registry = registry;
    }

    /**
     * Returns the JsonHome document for the specified environment.
     *
     * @param environment the environment (like develop, live) of this entry. Different registered environments
     *                    are used to access different versions of json-home documents during development.
     *                    The default environment is ""; null is not accepted.
     * @return the json-home for the specified environment.
     */
    public JsonHome getJsonHome(final String environment) {
        final Map<URI, ResourceLink> allResourceLinks = new HashMap<URI, ResourceLink>();
        for (final RegistryEntry registryEntry : registry.getAllFrom(environment)) {
            try {
                final JsonHome jsonHome = client.get(registryEntry.getHref());
                final Map<URI, ResourceLink> resources = jsonHome.getResources();
                for (final URI uri : resources.keySet()) {
                    if (allResourceLinks.containsKey(uri)) {
                        LOG.warn("Duplicate entries found for resource {}: entry '{}', is overridden by '{}'",
                                new Object[] {uri, allResourceLinks.get(uri), resources.get(uri)});
                    }
                    allResourceLinks.put(uri, resources.get(uri));
                }
                allResourceLinks.putAll(resources);
            } catch (final NotFoundException e) {
                LOG.warn("Unable to get json-home document {}: {}", registryEntry.getHref(), e.getMessage());
                // After some retries, the json-home MAY automatically be unregistered here.
            } catch (final JsonHomeClientException e) {
                LOG.warn("Unable to get json-home document {}: {}", registryEntry.getHref(), e.getMessage());
                // After some retries, the json-home MAY automatically be unregistered here.
            }
        }
        LOG.debug("Returning json-home instance containing {} relation types: {}",
                allResourceLinks.size(), allResourceLinks.keySet());
        return jsonHome(allResourceLinks.values());
    }

    @Override
    public JsonHome getJsonHome() {
        return getJsonHome("");
    }
}
