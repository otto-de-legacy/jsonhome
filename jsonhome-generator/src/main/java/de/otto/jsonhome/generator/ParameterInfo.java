/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.generator;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Information about a parameter of a method.
 *
 * @author Guido Steinacker
 * @since 03.10.12
 */
public final class ParameterInfo {
    private final String name;
    private final Class<?> type;
    private final List<Annotation> annotations;

    /**
     * Creates a ParameterInfo.
     *
     * @param name name of the parameter. Must neither be null nor empty.
     * @param type type of the parameter. Must not be null.
     * @param annotations annotations of the parameter. Must not be null.
     */
    public ParameterInfo(final String name, final Class<?> type, final List<Annotation> annotations) {
        this.name = name;
        this.type = type;
        this.annotations = annotations;
    }

    /**
     * Returns the name of the method parameter.
     *
     * @return neither null nor empty.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the parameter.
     *
     * @return never null.
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Returns the annotations of the parameter.
     *
     * @return list of annotations, never null.
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * Returns true if the parameter is annotated with the specified annotationType, false otherwise.
     *
     * @param annotationType the checked type of annotation.
     * @return boolean
     */
    public boolean hasAnnotation(final Class<? extends Annotation> annotationType) {
        for (final Annotation annotation : annotations) {
            if (annotationType.isAssignableFrom(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the specified annotation or null, if the parameter is not annotated with the annotationType.
     *
     * @param annotationType the type of the expected annotation.
     * @param <T> the type of the expected annotation.
     * @return Annotation or null.
     */
    public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
        for (final Annotation annotation : annotations) {
            if (annotationType.isAssignableFrom(annotation.annotationType())) {
                return annotationType.cast(annotation);
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterInfo that = (ParameterInfo) o;

        if (annotations != null ? !annotations.equals(that.annotations) : that.annotations != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (annotations != null ? annotations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ParameterInfo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", annotations=" + annotations +
                '}';
    }
}
