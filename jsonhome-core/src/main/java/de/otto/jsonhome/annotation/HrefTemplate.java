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

/**
 * Overrides the href-template and href-vars of a templated resource link.
 * <p/>
 * Use this annotation if you have to override the behaviour of the generator in use (like, for example,
 * the SpringJsonHomeGenerator). <strong>Use this annotation with care</strong>, because
 * if you are specifying the wrong URI, the href link in your json-home will be resolvable by your application.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HrefTemplate {

    /**
     * The templated URI uniquely identifying the href-template of a linked resource.
     * <p/>
     * The href-vars are parsed from the specified template.
     * <p/>
     * Because it is not possible to associate {@link Doc documentation} with the variables used in the
     * href-template, you should consider adding some appropriate documentation to the resource link itself.
     *
     * @return URI of the linked resource
     */
    String value();

}
