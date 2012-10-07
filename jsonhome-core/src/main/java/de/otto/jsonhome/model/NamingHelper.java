package de.otto.jsonhome.model;

import java.net.URI;

/**
 * @author Guido Steinacker
 * @since 08.10.12
 */
public final class NamingHelper {

    private NamingHelper() {
    }

    public static String toName(final URI uri) {
        final String path = uri.getPath();
        final String s = path.substring(path.indexOf("/rel/") + 4);
        final String[] words = s.split("/");
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final String word : words) {
            if (!first) {
                sb.append(" ").append(word);
            } else {
                first = false;
                sb.append(word);
            }
        }
        return sb.toString();
    }
}
