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
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rel {

    /**
     * The URI uniquely identifying the link relation type in a given context.
     *
     *
     * @return URI of the link relation type
     */
    String value();

}
