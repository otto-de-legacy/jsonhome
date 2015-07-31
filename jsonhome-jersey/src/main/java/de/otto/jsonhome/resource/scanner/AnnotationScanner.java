/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.otto.jsonhome.resource.scanner;

import de.otto.jsonhome.JsonHomeProperties;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.reflections.util.ClasspathHelper.forClass;
import static org.reflections.util.ClasspathHelper.forPackage;

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
            final Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .addUrls(forClass(Path.class))
                    .addUrls(forPackage(pkg))
                    .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
                    .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pkg))));
            classes.addAll(reflections.getTypesAnnotatedWith(Path.class));
        }
        return classes;
    }

}
