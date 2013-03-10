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
package de.otto.jsonhome.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Documentation of a link-relation type (&#064;Rel) or a variable used in an uri-template.
 * <p/>
 * Link-relation types must be documented at class-level and must specify the documented
 * type using the {@link #rel()} attribute.
 * <p/>
 * If you want to document more than one link-relation type in a single controller, you must
 * use the {@link Docs} annotation to add multiple Doc instances to the class.
 * <p/>
 * In case of documenting a href-template variable, the {@code rel} attribute is ignored.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target({TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Doc {

    /**
     * Optional value, consisting of one or more paragraphs.
     */
    String[] value() default "";

    /**
     * Optional relative URI pointing to a Markdown document to include into the
     * HTML description of the documentation.
     */
    String include() default "";

    /**
     * Optional fully qualified URI pointing to external documentation.
     */
    String link() default "";

    /**
     * The URI uniquely identifying the link relation type.
     * <p/>
     * If this is a documentation of a link-relation type, this attribute must be provided. Otherwise, the
     * documentation can not be attached to the proper link-relation type.
     *
     * @return URI of the link relation type
     */
    String rel() default "";

}
