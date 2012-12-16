package de.otto.jsonhome.resource;

import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;
import de.otto.jsonhome.resource.scanner.AnnotationScanner;

import java.util.Collection;

/**
 * @author Sebastian Schroeder
 * @since 11.12.2012
 */
public final class JerseyJsonHomeSource implements JsonHomeSource {

    private final JsonHome jsonHome;

    public JerseyJsonHomeSource(JsonHomeGenerator jsonHomeGenerator, AnnotationScanner annotationScanner) {
        jsonHome = jsonHomeGenerator.with(annotationScanner.scanClasses()).generate();
    }

    public JerseyJsonHomeSource(JsonHomeGenerator jsonHomeGenerator, Collection<Class<?>> classes) {
        jsonHome = jsonHomeGenerator.with(classes).generate();
    }

    /**
     * Returns a JsonHome instance.
     *
     * @return JsonHome.
     */
    @Override
    public JsonHome getJsonHome() {
        return jsonHome;
    }

}
