package de.otto.jsonhome.generator;

import com.sun.jersey.api.uri.UriBuilderImpl;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriBuilder;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
public final class JerseyResourceLinkGenerator extends ResourceLinkGenerator {

    private final URI applicationBaseUri;

    public JerseyResourceLinkGenerator(final URI applicationBaseUri,
                                          final URI relationTypeBaseUri) {
        super(applicationBaseUri,
                relationTypeBaseUri,
                new JerseyHintsGenerator(relationTypeBaseUri),
                new JerseyHrefVarsGenerator(relationTypeBaseUri));
        this.applicationBaseUri = applicationBaseUri;
    }

    /**
     * Returns true if the method is a candidate for further processing, false otherwise.
     *
     * @param method the method to check
     * @return boolean
     */
    @Override
    protected boolean isCandidateForAnalysis(final Method method) {
        for (final Annotation annotation : method.getDeclaredAnnotations()) {
            if (HttpMethods.isHttpMethod(annotation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the resource paths for the given method.
     * <p/>
     * The resource paths are the paths of the URIs the given method is responsible for.
     *
     * @param method the method of the controller, possibly handling one or more REST resources.
     * @return list of resource paths
     */
    @Override
    protected List<String> resourcePathsFor(final Method method) {
        final List<String> resourcePaths = new ArrayList<String>();
        if (isCandidateForAnalysis(method)) {
            final UriBuilder uriBuilder = new UriBuilderImpl()
                    .uri(applicationBaseUri)
                    .path(method.getDeclaringClass());
            if (method.getAnnotation(Path.class) != null) {
                uriBuilder.path(method);
            }
            try {
                resourcePaths.add(
                        URLDecoder.decode(uriBuilder.build().toString(), "UTF-8") + queryTemplateFrom(method));
            } catch (UnsupportedEncodingException e) {
                //UTF-8 should be known
            }
        }
        return resourcePaths;
    }

}
