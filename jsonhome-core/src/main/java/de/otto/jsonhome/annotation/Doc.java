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
 * Documentation of a link relation type or uri-template variable.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target({TYPE, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Doc {

    /**
     * Optional value, consisting of one or more paragraphs.
     */
    String[] value() default "";

    /**
     * Optional fully qualified URI pointing to external documentation.
     */
    String link() default "";

}
