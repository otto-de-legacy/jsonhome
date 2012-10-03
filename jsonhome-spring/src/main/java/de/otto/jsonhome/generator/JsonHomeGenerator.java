package de.otto.jsonhome.generator;

import de.otto.jsonhome.annotation.Rel;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.model.ResourceLink;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.JsonHome.emptyJsonHome;
import static de.otto.jsonhome.model.JsonHome.jsonHome;
import static de.otto.jsonhome.model.JsonHomeBuilder.jsonHomeBuilder;
import static de.otto.jsonhome.model.ResourceLinkHelper.mergeResources;
import static de.otto.jsonhome.model.TemplatedLink.templatedLink;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * A generator used to create JsonHome documents from Spring controllers.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */

public class JsonHomeGenerator {

    private final URI rootUri;
    private final HrefVarsGenerator hrefVarsGenerator = new HrefVarsGenerator();
    private final HintsGenerator hintsGenerator = new HintsGenerator();

    public static JsonHomeGenerator jsonHomeFor(final URI rootUri) {
        return new JsonHomeGenerator(rootUri);
    }

    protected JsonHomeGenerator(final URI rootUri) {
        if (rootUri == null) {
            throw new NullPointerException("Parameter rootUri must not be null.");
        }
        this.rootUri = rootUri;
    }

    public JsonHome with(final Class<?> controller) {
        if (isSpringController(controller)) {
            return jsonHomeBuilder()
                    .addResources(resourceLinksFor(controller))
                    .build();
        } else {
            return emptyJsonHome();
        }
    }

    public JsonHome with(final Collection<Class<?>> controller) {
        List<ResourceLink> resources = new ArrayList<ResourceLink>();
        for (final Class<?> controllerClass : controller) {
            if (isSpringController(controllerClass)) {
                resources = mergeResources(resources, resourceLinksFor(controllerClass));
            }
        }
        return jsonHome(resources);
    }

    protected List<ResourceLink> resourceLinksFor(final Class<?> controller) {
        List<ResourceLink> resourceLinks = emptyList();
        for (final Method method : controller.getMethods()) {
            resourceLinks = mergeResources(resourceLinks, resourceLinksForMethod(controller, method));
        }
        return resourceLinks;
    }

    /**
     * Analyses the a method of a controller (having a RequestMapping) and returns the list of ResourceLinks of this method.
     *
     * @param controller the controller of the method.
     * @param method the method
     * @return list of resource links.
     */
    protected List<ResourceLink> resourceLinksForMethod(final Class<?> controller,
                                                        final Method method) {
        final List<String> parentResourcePaths = parentResourcePathsFrom(controller);
        final List<ResourceLink> resourceLinks = new ArrayList<ResourceLink>();
        final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        if (methodRequestMapping != null) {
            for (String resourcePathPrefix : parentResourcePaths) {
                final String[] resourcePathSuffixes = methodRequestMapping.value().length > 0
                        ? methodRequestMapping.value()
                        : new String[] {""};
                for (String resourcePathSuffix : resourcePathSuffixes) {
                    final String resourcePath = rootUri + resourcePathPrefix + resourcePathSuffix;
                    final URI relationType = relationTypeFrom(controller, method);
                    if (relationType != null) {
                        if (resourcePath.matches(".*\\{.*\\}")) {
                            resourceLinks.add(templatedLink(
                                    relationType,
                                    resourcePath,
                                    hrefVarsGenerator.hrefVarsFor(method),
                                    hintsGenerator.hintsOf(method)
                            ));
                        } else {
                            resourceLinks.add(directLink(
                                    relationType,
                                    URI.create(resourcePath),
                                    hintsGenerator.hintsOf(method)
                            ));
                        }
                    }
                }
            }
        }
        return resourceLinks;
    }

    /**
     * Analyses the controller (possibly annotated with RequestMapping) and returns the list of resource paths defined by the mapping.
     *
     * @param controller the controller.
     * @return list of resource paths.
     */
    protected List<String> parentResourcePathsFrom(final Class<?> controller) {
        final RequestMapping controllerRequestMapping = controller.getAnnotation(RequestMapping.class);
        final List<String> resourcePathPrefixes;
        if (controllerRequestMapping != null) {
            resourcePathPrefixes = asList(controllerRequestMapping.value());
        } else {
            resourcePathPrefixes = asList("");
        }
        return resourcePathPrefixes;
    }

    /**
     * Analyses a method of a controller and returns the fully qualified URI of the link-relation type.
     *
     * If the neither the method, nor the controller is annotated with Rel, null is returned.
     *
     * The Rel of the method is overriding the Rel of the Controller.
     *
     * @param controller the controller
     * @param method the method
     * @return URI of the link-relation type, or null
     */
    protected URI relationTypeFrom(final Class<?> controller, final Method method) {
        final Rel controllerRel = controller.getAnnotation(Rel.class);
        final Rel methodRel = method.getAnnotation(Rel.class);
        if (controllerRel == null && methodRel == null) {
            return null;
        } else {
            final String linkRelationType = methodRel != null
                    ? methodRel.value()
                    : controllerRel.value();
            return URI.create(linkRelationType.startsWith("http://")
                    ? linkRelationType
                    : rootUri + linkRelationType);
        }
    }

    private boolean isSpringController(final Class<?> controller) {
        return controller.getAnnotation(Controller.class) != null;
    }

}
