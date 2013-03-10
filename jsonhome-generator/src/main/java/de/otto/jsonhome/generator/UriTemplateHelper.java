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
package de.otto.jsonhome.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a URI template. A URI template is a URI-like String that contains variables enclosed
 * by braces (<code>{</code>, <code>}</code>), which can be expanded to produce an actual URI.
 *
 * @see <a href="http://tools.ietf.org/html/rfc6570">URI Templates</a>
 */
public class UriTemplateHelper implements Serializable {

    /** Captures URI template variable names. */
    private static final Pattern NAMES_PATTERN = Pattern.compile("\\{[\\/+#;\\.\\?&]?([^/]+?)[\\*]?\\}");

    public static List<String> variableNamesFrom(final String uriTemplate) {
        final List<String> variableNames = new ArrayList<String>();
        Matcher m = NAMES_PATTERN.matcher(uriTemplate);
        while (m.find()) {
            String match = m.group(1);
            String[] parts = match.split(",");
            for (final String part : parts) {
                int colonIdx = part.indexOf(':');
                if (colonIdx == -1) {
                    variableNames.add(part);
                }
                else {
                    if (colonIdx + 1 == part.length()) {
                        throw new IllegalArgumentException("No custom regular expression specified after ':' in \"" + part	+ "\"");
                    }
                    variableNames.add(part.substring(0, colonIdx));
                }
            }
        }
        return variableNames;
    }

}
