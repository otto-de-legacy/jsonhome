package de.otto.jsonhome.resource.scanner;

import de.otto.jsonhome.JsonHomeProperties;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sebastian Schroeder
 * @since 15.12.2012
 */
public final class AnnotationScanner {

    private final Set<String> packages;

    public AnnotationScanner() {
        final String packages = JsonHomeProperties.getProperties().getProperty("resource.packages");
        if (packages == null) {
            throw new IllegalStateException("resource.packages property not set in jsonhome.properties");
        }
        this.packages = new HashSet<String>(Arrays.asList(packages.split("\\s*,\\s*")));
    }

    public AnnotationScanner(Set<String> packages) {
        this.packages = packages;
    }

    public Set<Class<?>> scanClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String pkg : packages) {
            final Reflections reflections = new Reflections(new ConfigurationBuilder().
                    addUrls(ClasspathHelper.forClass(Path.class)).
                    setScanners(new TypeAnnotationsScanner()).
                    addUrls(ClasspathHelper.forPackage(pkg)).
                    filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pkg))));
            classes.addAll(reflections.getTypesAnnotatedWith(Path.class));
        }
        return classes;
    }

}
