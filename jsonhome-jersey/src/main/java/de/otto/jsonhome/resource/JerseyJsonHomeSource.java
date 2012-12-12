package de.otto.jsonhome.resource;

import de.otto.jsonhome.generator.JerseyJsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.model.JsonHome;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.ws.rs.Path;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Sebastian Schroeder
 * @since 11.12.2012
 */
public class JerseyJsonHomeSource implements JsonHomeSource {

    private final JsonHome jsonHome;

    public JerseyJsonHomeSource(URI applicationBaseUri, URI relationTypeBaseUri, List<String> packages) {
        final ConfigurationBuilder configBuilder = new ConfigurationBuilder().
                addUrls(ClasspathHelper.forClass(Path.class)).
                setScanners(new TypeAnnotationsScanner());
        for (String pkg : packages) {
            configBuilder.addUrls(ClasspathHelper.forPackage(pkg));
        }
        final Reflections reflections = new Reflections(configBuilder);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Path.class);
        jsonHome = new JerseyJsonHomeGenerator(applicationBaseUri, relationTypeBaseUri).with(classes).generate();
    }

    public JerseyJsonHomeSource(String applicationBaseUri, String relationTypeBaseUri, List<String> packages) {
        this(URI.create(applicationBaseUri), URI.create(relationTypeBaseUri), packages);
    }

    public JerseyJsonHomeSource(URI applicationBaseUri, URI relationTypeBaseUri, Collection<Class<?>> classes) {
        jsonHome = new JerseyJsonHomeGenerator(applicationBaseUri, relationTypeBaseUri).with(classes).generate();
    }

    public JerseyJsonHomeSource(String applicationBaseUri, String relationTypeBaseUri, Collection<Class<?>> classes) {
        jsonHome = new JerseyJsonHomeGenerator(applicationBaseUri, relationTypeBaseUri).with(classes).generate();
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
