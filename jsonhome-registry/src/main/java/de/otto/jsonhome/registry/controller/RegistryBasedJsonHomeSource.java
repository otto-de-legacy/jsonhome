package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.client.HttpJsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClientException;
import de.otto.jsonhome.client.NotFoundException;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import de.otto.jsonhome.registry.store.JsonHomeRef;
import de.otto.jsonhome.registry.store.Registries;
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
 * Provides access to a json-home document containing the merged json-home documents registered in the {@link de.otto.jsonhome.registry.store.Registries}.
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
    private Registries registries;

    public RegistryBasedJsonHomeSource() {
        this.client = new HttpJsonHomeClient();
    }

    @PreDestroy
    public void shutdown() {
        LOG.info("Shutting down JsonHomeClient");
        client.shutdown();
    }

    @Autowired
    public void setRegistries(final Registries registries) {
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
        if (registries.getRegistry(registryName) == null) {
            final String msg = "Registry '" + registryName + "' does not exist.";
            LOG.warn(msg);
            throw new IllegalArgumentException(msg);
        } else {
            final Map<URI, ResourceLink> allResourceLinks = new HashMap<URI, ResourceLink>();
            for (final JsonHomeRef jsonHomeRef : registries.getRegistry(registryName).getAll()) {
                try {
                    final JsonHome jsonHome = client.get(jsonHomeRef.getHref());
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
                    LOG.warn("Unable to get json-home document {}: {}", jsonHomeRef.getHref(), e.getMessage());
                    // After some retries, the json-home MAY automatically be unregistered here.
                } catch (final JsonHomeClientException e) {
                    LOG.warn("Unable to get json-home document {}: {}", jsonHomeRef.getHref(), e.getMessage());
                    // After some retries, the json-home MAY automatically be unregistered here.
                }
            }
            LOG.debug("Returning json-home instance containing {} relation types: {}",
                    allResourceLinks.size(), allResourceLinks.keySet());
            return jsonHome(allResourceLinks.values());
        }
    }

}
