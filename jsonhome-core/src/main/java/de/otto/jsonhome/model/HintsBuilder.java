package de.otto.jsonhome.model;

import java.util.*;

import static java.util.Arrays.asList;

/**
 * A builder used to build Hints instances.
 *
 * @author Guido Steinacker
 * @since 30.09.12
 */
public class HintsBuilder {

    private final Set<String> allows = new LinkedHashSet<String>();
    private final Set<String> representations = new LinkedHashSet<String>();
    private final Set<String> acceptPost = new LinkedHashSet<String>();
    private final Set<String> acceptPut = new LinkedHashSet<String>();

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
                .acceptingForPost(hints.getAcceptPost());
    }

    public HintsBuilder allowing(final String... allows) {
        this.allows.addAll(asList(allows));
        return this;
    }

    public HintsBuilder allowing(final Collection<String> allows) {
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

    public Hints build() {
        return new Hints(allows, new ArrayList<String>(representations), new ArrayList<String>(acceptPut), new ArrayList<String>(acceptPost));
    }
}
