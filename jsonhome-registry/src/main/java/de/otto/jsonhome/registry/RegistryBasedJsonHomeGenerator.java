package de.otto.jsonhome.registry;

import de.otto.jsonhome.client.HttpJsonHomeClient;
import de.otto.jsonhome.client.JsonHomeClient;
import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.generator.SpringResourceLinkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;

/**
 * @author Guido Steinacker
 * @since 20.11.12
 */
@Service
public class RegistryBasedJsonHomeGenerator extends JsonHomeGenerator {

    private final JsonHomeClient client;

    private URI applicationBaseUri;
    private URI relationTypeBaseUri;
    private Registry registry;

    public RegistryBasedJsonHomeGenerator() {
        this.client = new HttpJsonHomeClient();
    }

    @Value("${jsonhome.applicationBaseUri}")
    public void setApplicationBaseUri(final String applicationBaseUri) {
        this.applicationBaseUri = URI.create(applicationBaseUri);
    }

    @Value("${jsonhome.relationTypeBaseUri}")
    public void setRelationTypeBaseUri(final String relationTypeBaseUri) {
        this.relationTypeBaseUri = URI.create(relationTypeBaseUri);
    }

    @Autowired
    public void setRegistry(final Registry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void postConstruct() {
        setResourceLinkGenerator(new SpringResourceLinkGenerator(applicationBaseUri, relationTypeBaseUri));
    }

    @Override
    protected boolean isCandidateForAnalysis(final Class<?> controller) {
        return controller.getAnnotation(Controller.class) != null;
    }


}
