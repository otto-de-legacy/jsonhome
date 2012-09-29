package de.steinacker.jsonhome.generator;

import de.steinacker.jsonhome.annotation.LinkRelationType;
import de.steinacker.jsonhome.annotation.VarType;
import de.steinacker.jsonhome.annotation.VarTypes;
import de.steinacker.jsonhome.model.HrefVar;
import de.steinacker.jsonhome.model.JsonHome;
import de.steinacker.jsonhome.model.ResourceLink;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

import static de.steinacker.jsonhome.model.DirectLink.directLink;
import static de.steinacker.jsonhome.model.JsonHome.emptyJsonHome;
import static de.steinacker.jsonhome.model.JsonHome.jsonHome;
import static de.steinacker.jsonhome.model.JsonHomeBuilder.jsonHomeBuilder;
import static de.steinacker.jsonhome.model.TemplatedLink.templatedLink;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * @author Guido Steinacker
 * @since 15.09.12
 */

public class JsonHomeGenerator {

    private final URI rootUri;

    private JsonHomeGenerator(final URI rootUri) {
        if (rootUri == null) {
            throw new NullPointerException("Parameter rootUri must not be null.");
        }
        this.rootUri = rootUri;
    }

    public static JsonHomeGenerator jsonHomeFor(final URI rootUri) {
        return new JsonHomeGenerator(rootUri);
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
        List<ResourceLink> resources = new ArrayList<>();
        for (final Class<?> controllerClass : controller) {
            if (isSpringController(controllerClass)) {
                resources = mergeResources(resources, resourceLinksFor(controllerClass));
            }
        }
        return jsonHome(resources);
    }

    private List<ResourceLink> resourceLinksFor(final Class<?> controller) {
        final RequestMapping controllerRequestMapping = controller.getAnnotation(RequestMapping.class);
        final List<String> resourcePathPrefixes;
        if (controllerRequestMapping != null) {
            resourcePathPrefixes = asList(controllerRequestMapping.value());
        } else {
            resourcePathPrefixes = asList("");
        }
        List<ResourceLink> resourceLinks = emptyList();
        for (final Method method : controller.getMethods()) {
            resourceLinks = mergeResources(resourceLinks, resourceLinksForMethod(controller, method, resourcePathPrefixes));
        }
        return resourceLinks;
    }

    private List<ResourceLink> mergeResources(final List<ResourceLink> resourceLinks, final List<ResourceLink> other) {
        final List<ResourceLink> allCandidates = new ArrayList<>(resourceLinks.size() + other.size());
        allCandidates.addAll(resourceLinks);
        allCandidates.addAll(other);
        final Map<URI, ResourceLink> resourceLinkCandidates = new LinkedHashMap<>();
        for (final ResourceLink candidate : allCandidates) {
            final URI linkRelationType = candidate.getLinkRelationType();
            final ResourceLink existingCandidate = resourceLinkCandidates.get(linkRelationType);
            if (existingCandidate != null) {
                // merge the candidates, they are belonging to the same resource link
                resourceLinkCandidates.put(linkRelationType, existingCandidate.mergeWith(candidate));
            } else {
                resourceLinkCandidates.put(linkRelationType, candidate);
            }
        }
        return new ArrayList<>(resourceLinkCandidates.values());
    }

    private List<ResourceLink> resourceLinksForMethod(final Class<?> controller,
                                           final Method method,
                                           final List<String> resourcePathPrefixes) {
        final List<ResourceLink> resourceLinks = new ArrayList<>();
        final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        if (methodRequestMapping != null) {
            final List<String> representations = supportedRepresentationsOf(methodRequestMapping);
            final List<String> allows = supportedRequestMethodsOf(controller, methodRequestMapping);
            for (String resourcePathPrefix : resourcePathPrefixes) {
                final String[] resourcePathSuffixes = methodRequestMapping.value().length > 0
                        ? methodRequestMapping.value()
                        : new String[] {""};
                for (String resourcePathSuffix : resourcePathSuffixes) {
                    final String resourcePath = rootUri + resourcePathPrefix + resourcePathSuffix;
                    final String relationType = relationTypeFrom(controller, method);
                    if (!relationType.isEmpty()) {
                        if (resourcePath.matches(".*\\{.*\\}")) {
                            resourceLinks.add(templatedLink(
                                    URI.create(relationType),
                                    resourcePath,
                                    hrefVarsFor(method),
                                    allows,
                                    representations
                            ));
                        } else {
                            resourceLinks.add(directLink(
                                    URI.create(relationType),
                                    URI.create(resourcePath),
                                    allows,
                                    representations
                            ));
                        }
                    }
                }
            }
        }
        return resourceLinks;
    }

    private List<String> supportedRequestMethodsOf(final Class<?> controller, final RequestMapping methodRequestMapping) {
        List<String> allows = listOfStringsFrom(methodRequestMapping.method());
        if (allows.isEmpty()) {
            final RequestMapping controllerRequestMapping = controller.getAnnotation(RequestMapping.class);
            if (controllerRequestMapping != null) {
                allows = listOfStringsFrom(controllerRequestMapping.method());
            }
        }
        if (allows.isEmpty()) {
            allows = singletonList("GET");
        }
        return allows;
    }

    private List<String> listOfStringsFrom(Object[] array) {
        final List<String> result = new ArrayList<>(array.length);
        for (Object o : array) {
            result.add(o.toString());
        }
        return result;
    }

    private List<HrefVar> hrefVarsFor(final Method method) {
        final List<HrefVar> varBuilder = new ArrayList<>();
        final VarTypes varTypes = method.getAnnotation(VarTypes.class);
        if (varTypes != null) {
            for (final VarType varType : varTypes.value()) {
                varBuilder.add(new HrefVar(
                        varType.value(),
                        URI.create(varType.reference()),
                        varType.description())
                );
            }
        } else {
            final VarType varType = method.getAnnotation(VarType.class);
            if (varType != null) {
                varBuilder.add(new HrefVar(
                        varType.value(),
                        URI.create(varType.reference()),
                        varType.description())
                );
            }
        }
        return varBuilder;
    }

    private List<String> supportedRepresentationsOf(final RequestMapping methodRequestMapping) {
        final LinkedHashSet<String> representations = new LinkedHashSet<>();
        final String[] produces = methodRequestMapping.produces();
        if (produces != null) {
            representations.addAll(asList(produces));
        }
        final String[] consumes = methodRequestMapping.consumes();
        if (consumes != null) {
            // preserve order from methodRequestMapping:
            for (final String consumesRepresentation : consumes) {
                if (!representations.contains(consumesRepresentation)) {
                    representations.add(consumesRepresentation);
                }
            }
        }
        // default is HTTP GET
        if (representations.isEmpty()) {
            representations.add("text/html");
        }
        return new ArrayList<>(representations);
    }

    private String relationTypeFrom(final Class<?> controller, final Method method) {
        final LinkRelationType controllerLinkRelationType = controller.getAnnotation(LinkRelationType.class);
        final LinkRelationType methodLinkRelationType = method.getAnnotation(LinkRelationType.class);
        final String linkRelationType = methodLinkRelationType != null
                ? methodLinkRelationType.value()
                : controllerLinkRelationType != null ? controllerLinkRelationType.value() : "";
        if (!linkRelationType.isEmpty()) {
            return linkRelationType.startsWith("http://")
                    ? linkRelationType
                    : rootUri + linkRelationType;
        } else {
            return "";
        }
    }

    private boolean isSpringController(final Class<?> controller) {
        return controller.getAnnotation(Controller.class) != null;
    }

}
