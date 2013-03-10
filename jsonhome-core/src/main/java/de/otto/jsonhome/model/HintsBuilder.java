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
package de.otto.jsonhome.model;

import java.util.*;

import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static de.otto.jsonhome.model.Hints.hints;
import static java.util.Arrays.asList;

/**
 * A builder used to build Hints instances.
 *
 * @author Guido Steinacker
 * @since 30.09.12
 */
public class HintsBuilder {

    private final Set<Allow> allows = EnumSet.noneOf(Allow.class);
    private final Set<String> representations = new LinkedHashSet<String>();
    private final Set<String> acceptPost = new LinkedHashSet<String>();
    private final Set<String> acceptPut = new LinkedHashSet<String>();
    private final Set<String> acceptPatch = new LinkedHashSet<String>();
    private final Set<String> acceptRanges = new LinkedHashSet<String>();
    private final Set<String> preferences = new LinkedHashSet<String>();
    private final List<Precondition> preconditionReq = new ArrayList<Precondition>();
    private final List<Authentication> authReq = new ArrayList<Authentication>();
    private Documentation docs = emptyDocs();
    private Status status = Status.OK;

    private HintsBuilder() {
    }

    public static HintsBuilder hintsBuilder() {
        return new HintsBuilder();
    }

    public HintsBuilder allowing(final Allow... allows) {
        this.allows.addAll(asList(allows));
        return this;
    }

    public HintsBuilder allowing(final Set<Allow> allows) {
        this.allows.addAll(allows);
        return this;
    }

    public HintsBuilder representedAs(final String... representations) {
        this.representations.addAll(asList(representations));
        return this;
    }

    public HintsBuilder representedAs(final List<String> representations) {
        this.representations.addAll(representations);
        return this;
    }

    public HintsBuilder acceptingForPut(final String... representations) {
        this.acceptPut.addAll(asList(representations));
        return this;
    }

    public HintsBuilder acceptingForPut(final List<String> representations) {
        this.acceptPut.addAll(representations);
        return this;
    }

    public HintsBuilder acceptingForPost(final String... representations) {
        acceptPost.addAll(asList(representations));
        return this;
    }

    public HintsBuilder acceptingForPost(final List<String> representations) {
        acceptPost.addAll(representations);
        return this;
    }

    public HintsBuilder acceptingForPatch(final String... representations) {
        acceptPatch.addAll(asList(representations));
        return this;
    }

    public HintsBuilder acceptingForPatch(final List<String> representations) {
        acceptPatch.addAll(representations);
        return this;
    }

    public HintsBuilder acceptingRanges(final String... ranges) {
        this.acceptRanges.addAll(asList(ranges));
        return this;
    }

    public HintsBuilder acceptingRanges(final List<String> ranges) {
        this.acceptRanges.addAll(ranges);
        return this;
    }

    public HintsBuilder preferring(final String... preferences) {
        this.preferences.addAll(asList(preferences));
        return this;
    }

    public HintsBuilder preferring(final List<String> preferences) {
        this.preferences.addAll(preferences);
        return this;
    }

    public HintsBuilder with(Documentation docs) {
        this.docs = docs;
        return this;
    }

    public HintsBuilder requiring(final Precondition... preconditions) {
        this.preconditionReq.addAll(asList(preconditions));
        return this;
    }

    public HintsBuilder requiring(final List<Precondition> preconditions) {
        this.preconditionReq.addAll(preconditions);
        return this;
    }

    public HintsBuilder withAuthRequired(final List<Authentication> authReq) {
        this.authReq.addAll(authReq);
        return this;
    }

    public HintsBuilder withStatus(final Status status) {
        this.status = status;
        return this;
    }

    public Hints build() {
        return hints(
                allows,
                new ArrayList<String>(representations),
                new ArrayList<String>(acceptPut),
                new ArrayList<String>(acceptPost),
                new ArrayList<String>(acceptPatch),
                new ArrayList<String>(acceptRanges),
                new ArrayList<String>(preferences),
                preconditionReq,
                authReq,
                status,
                docs
        );
    }
}