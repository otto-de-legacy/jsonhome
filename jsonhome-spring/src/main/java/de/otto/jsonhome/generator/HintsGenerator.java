package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.Hints;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.*;

public class HintsGenerator {

    public Hints hintsOf(final Method method) {
        return new Hints(
                allowedHttpMethodsOf(method),
                supportedRepresentationsOf(method)
        );
    }

    /**
     * Analyses the method with a RequestMapping and returns a list of allowed http methods (GET, PUT, etc.).
     * <p/>
     * If the RequestMapping does not specify the allowed HTTP methods, "GET" is returned in a singleton list.
     *
     * @return list of allowed HTTP methods.
     * @throws NullPointerException if method is not annotated with @RequestMapping.
     */
    protected List<String> allowedHttpMethodsOf(final Method method) {
        final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        final List<String> allows = listOfStringsFrom(methodRequestMapping.method());
        if (allows.isEmpty()) {
            return Collections.singletonList("GET");
        } else {
            return allows;
        }
    }

    /**
     * Analyses the method with a RequestMapping and returns a list of supported representations.
     * <p/>
     * If the RequestMapping does not specify the produced or consumed representations,
     * "text/html" is returned in a singleton list.
     * <p/>
     * TODO: in case of a POST, text/html is not correct.
     *
     * @return list of allowed HTTP methods.
     * @throws NullPointerException if method is not annotated with @RequestMapping.
     */
    protected List<String> supportedRepresentationsOf(final Method method) {
        final RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        final LinkedHashSet<String> representations = new LinkedHashSet<String>();
        final String[] produces = methodRequestMapping.produces();
        if (produces != null) {
            representations.addAll(Arrays.asList(produces));
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
        return new ArrayList<String>(representations);
    }

    private List<String> listOfStringsFrom(Object[] array) {
        final List<String> result = new ArrayList<String>(array.length);
        for (Object o : array) {
            result.add(o.toString());
        }
        return result;
    }
}