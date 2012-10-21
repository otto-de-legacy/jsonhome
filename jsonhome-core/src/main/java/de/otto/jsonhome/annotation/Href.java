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
 * A link relation type according to http://tools.ietf.org/html/rfc5988#section-4.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Href {

    /**
     * The fully qualified or relative URI uniquely identifying the href of a linked resource.
     * <p/>
     * Use this annotation if you have to override the behaviour of the generator in use (like, for example,
     * the SpringJsonHomeGenerator). <strong>Generally, you should not use this annotation</strong>, because
     * if you are specifying the wrong URI, the href link in your json-home will be resolvable by your application.
     *
     * @return URI of the linked resource
     */
    String value();

}
