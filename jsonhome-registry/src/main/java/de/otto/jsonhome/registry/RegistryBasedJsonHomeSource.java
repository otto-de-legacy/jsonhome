package de.otto.jsonhome.registry;

import de.otto.jsonhome.client.HttpJsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClientException;
import de.otto.jsonhome.client.NotFoundException;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.net.URI;
import java.util.*;

import static de.otto.jsonhome.model.JsonHome.jsonHome;

/**
 * Provides access to a json-home document containing the merged json-home documents registered in the {@link Registry}.
 * <p/>
 * The service is responsible for retrieving all registered documents.
 *
 * @author Guido Steinacker
 * @since 20.11.12
 */
@Component
public class RegistryBasedJsonHomeSource implements JsonHomeSource {

    private final JsonHomeClient client;
    private Registry registry;

    public RegistryBasedJsonHomeSource() {
        this.client = new HttpJsonHomeClient();
    }

    @PreDestroy
    public void shutdown() {
        client.shutdown();
    }

    @Autowired
    public void setRegistry(final Registry registry) {
        this.registry = registry;
    }

    public JsonHome getJsonHome() {
        final Map<URI, ResourceLink> allResourceLinks = new HashMap<URI, ResourceLink>();
        for (final RegistryEntry registryEntry : registry.getAll()) {
            try {
                final JsonHome jsonHome = client.get(registryEntry.getHref());
                // TODO: add some logging if there are collisions, i.e. link relations from different registry entries with same URI.
                allResourceLinks.putAll(jsonHome.getResources());
            } catch (final NotFoundException e) {
                // TODO: add some logging.
                // TODO: handle not found. After some retries, the json-home MAY automatically be unregistered.
            } catch (final JsonHomeClientException e) {
                // TODO: add some logging.
                // TODO: handle not found. After some retries, the json-home MAY automatically be unregistered.
            }
        }
        return jsonHome(allResourceLinks.values());
    }
}
