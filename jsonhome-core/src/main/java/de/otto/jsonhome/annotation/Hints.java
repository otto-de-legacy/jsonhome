/*
 * *
 *  Copyright 2012 Guido Steinacker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package de.otto.jsonhome.annotation;

import de.otto.jsonhome.model.Precondition;
import de.otto.jsonhome.model.Status;

import java.lang.annotation.*;

/**
 * A link relation type according to http://tools.ietf.org/html/rfc5988#section-4.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Hints {

    /**
     * From the draft json-home spec:
     * "Hints that the resource requires state-changing requests (e.g., PUT,
     * PATCH) to include a precondition, as per
     * [I-D.ietf-httpbis-p4-conditional], to avoid conflicts due to
     * concurrent updates.
     *
     * Content MUST be an array of strings, with possible values "etag" and
     * "last-modified" indicating type of precondition expected."
     *
     * @return String the required preconditions
     * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5.9">json-home spec</a>
     * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#ref-I-D.ietf-httpbis-p4-conditional">I-D.ietf-httpbis-p4-conditional</a>
     */
    Precondition[] preconditionReq() default Precondition.NONE;

    /**
     * From the draft json-home spec:
     * "Hints the status of the resource.
     *
     * Content MUST be a string; possible values are:
     * o  "deprecated" - indicates that use of the resource is not recommended, but it is still available.
     * o  "gone" - indicates that the resource is no longer available; i.e., it will return a 410 Gone
     * HTTP status code if accessed."
     * @return String
     * * @see <a href="http://tools.ietf.org/html/draft-nottingham-json-home-02#section-5.11">json-home spec</a>
     */
    Status status() default Status.OK;

}
