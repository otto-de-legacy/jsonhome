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

    public ParameterInfo(String name, Class<?> type, List<Annotation> annotations) {
        this.name = name;
        this.type = type;
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public boolean hasAnnotation(final Class<? extends Annotation> annotationType) {
        for (final Annotation annotation : annotations) {
            if (annotationType.isAssignableFrom(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

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
