package de.otto.jsonhome.generator;

import de.otto.jsonhome.model.Allow;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
public final class JerseyHintsGenerator extends HintsGenerator {

    protected JerseyHintsGenerator(final URI relationTypeBaseUri) {
        super(relationTypeBaseUri);
    }

    @Override
    protected Set<Allow> allowedHttpMethodsOf(final Method method) {
        final Annotation[] annotations = method.getDeclaredAnnotations();
        final Set<Allow> allows = EnumSet.noneOf(Allow.class);
        for (final Annotation annotation : annotations) {
            if (HttpMethods.isHttpMethod(annotation)) {
                allows.add(Allow.valueOf(annotation.annotationType().getSimpleName()));
            }
        }
        return allows;
    }

    @Override
    protected List<String> producedRepresentationsOf(final Method method) {
        final Produces produces = method.getAnnotation(Produces.class);
        return produces == null
                ? Collections.<String>emptyList()
                : Arrays.asList(produces.value());
    }

    @Override
    protected List<String> consumedRepresentationsOf(final Method method) {
        final Consumes consumes = method.getAnnotation(Consumes.class);
        return consumes == null
                ? Collections.<String>emptyList()
                : Arrays.asList(consumes.value());
    }

}
