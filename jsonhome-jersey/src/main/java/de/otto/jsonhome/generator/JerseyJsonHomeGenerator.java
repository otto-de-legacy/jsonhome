package de.otto.jsonhome.generator;

import javax.ws.rs.Path;
import java.net.URI;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
public final class JerseyJsonHomeGenerator extends JsonHomeGenerator {

    public JerseyJsonHomeGenerator(URI applicationBaseUri, URI relationTypeBaseUri) {
        setResourceLinkGenerator(new JerseyResourceLinkGenerator(applicationBaseUri, relationTypeBaseUri));
    }

    public JerseyJsonHomeGenerator(String applicationBaseUri, String relationTypeBaseUri) {
        setResourceLinkGenerator(new JerseyResourceLinkGenerator(
                URI.create(applicationBaseUri), URI.create(relationTypeBaseUri)));
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
