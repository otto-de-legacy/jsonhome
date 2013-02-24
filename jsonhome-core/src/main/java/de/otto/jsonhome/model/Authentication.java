package de.otto.jsonhome.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Hints that the resource requires authentication using the HTTP Authentication Framework.
 *
 * @author Guido Steinacker
 * @since 16.02.13
 */
public final class Authentication {
    private final String scheme;
    private final List<String> realms;


    private Authentication(final String scheme, final List<String> realms) {
        this.scheme = scheme;
        this.realms = unmodifiableList(new ArrayList<String>(realms));
    }

    public static Authentication authReq(final String scheme, final List<String> realms) {
        return new Authentication(scheme, realms);
    }

    public static Authentication authReq(final String scheme) {
        return new Authentication(scheme, Collections.<String>emptyList());
    }

    public String getScheme() {
        return scheme;
    }

    public List<String> getRealms() {
        return realms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authentication auth = (Authentication) o;

        if (realms != null ? !realms.equals(auth.realms) : auth.realms != null) return false;
        if (scheme != null ? !scheme.equals(auth.scheme) : auth.scheme != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scheme != null ? scheme.hashCode() : 0;
        result = 31 * result + (realms != null ? realms.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "scheme='" + scheme + '\'' +
                ", realms=" + realms +
                '}';
    }
}
