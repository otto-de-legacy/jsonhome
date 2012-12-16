package de.otto.jsonhome.generator;

import de.otto.jsonhome.JsonHomeProperties;

import javax.ws.rs.Path;
import java.net.URI;
import java.util.Properties;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
public final class JerseyJsonHomeGenerator extends JsonHomeGenerator {

    public JerseyJsonHomeGenerator() {
        final Properties properties = JsonHomeProperties.getProperties();
        final String applicationBaseUri = properties.getProperty("applicationBaseUri");
        if (applicationBaseUri == null) {
            throw new IllegalStateException("applicationBaseUri property not set in jsonhome.properties");
        }
        final String relationTypeBaseUri = properties.getProperty("relationTypeBaseUri");
        if (relationTypeBaseUri == null) {
            throw new IllegalStateException("relationTypeBaseUri property not set in jsonhome.properties");
        }
        setResourceLinkGenerator(new JerseyResourceLinkGenerator(
                URI.create(applicationBaseUri), URI.create(relationTypeBaseUri)));
    }

    public JerseyJsonHomeGenerator(URI applicationBaseUri, URI relationTypeBaseUri) {
        setResourceLinkGenerator(new JerseyResourceLinkGenerator(applicationBaseUri, relationTypeBaseUri));
    }

    public JerseyJsonHomeGenerator(String applicationBaseUri, String relationTypeBaseUri) {
        this(URI.create(applicationBaseUri), URI.create(relationTypeBaseUri));
    }

    /**
     * Returns true if the resource is a candidate for further processing, false otherwise.
     *
     * @param resource the resource to check
     * @return boolean
     */
    @Override
    protected boolean isCandidateForAnalysis(Class<?> resource) {
        return resource.getAnnotation(Path.class) != null;
    }

}
