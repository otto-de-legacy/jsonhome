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
 * A container annotation used to add documentation of multiple link-relation types to a single controller.
 * <p/>
 * Usage:
 * <code><pre>
 *      &#064;Documentation({
 *          &#064;Doc(rel="/rel/foo-type", value="A reference to a foo"),
 *          &#064;Doc(rel="/rel/bar-type", value="A reference to a bar")
 *      })
 * </pre></code>
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Docs {

    /**
     * One or more RelDoc annotations, describing link-relation types.
     *
     * @return array of RelDoc annotations.
     */
    Doc[] value();

}
