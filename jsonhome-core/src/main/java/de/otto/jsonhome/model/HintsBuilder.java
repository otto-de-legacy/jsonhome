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
package de.otto.jsonhome.model;

import java.util.*;

import static de.otto.jsonhome.model.Docs.emptyDocumentation;
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
    private Docs docs = emptyDocumentation();

    private HintsBuilder() {
    }

    public static HintsBuilder hints() {
        return new HintsBuilder();
    }

    public static HintsBuilder copyOf(final Hints hints) {
        return new HintsBuilder()
                .allowing(hints.getAllows())
                .representedAs(hints.getRepresentations())
                .acceptingForPut(hints.getAcceptPut())
                .acceptingForPost(hints.getAcceptPost())
                .with(hints.getDocs());
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

    public HintsBuilder with(Docs docs) {
        this.docs = docs;
        return this;
    }

    public Hints build() {
        return new Hints(
                allows,
                new ArrayList<String>(representations),
                new ArrayList<String>(acceptPut),
                new ArrayList<String>(acceptPost),
                docs);
    }
}
