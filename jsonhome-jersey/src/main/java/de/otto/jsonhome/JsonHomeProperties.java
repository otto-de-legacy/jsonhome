package de.otto.jsonhome;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Sebastian Schroeder
 * @since 15.12.2012
 */
public final class JsonHomeProperties {

    private JsonHomeProperties() {}

    public static Properties getProperties() {
        final InputStream is = JsonHomeProperties.class.getResourceAsStream("jsonhome.properties");
        final Properties properties = new Properties();
        if (is == null) {
            return properties;
        } else {
            try {
                properties.load(is);
                is.close();
            } catch (IOException e) {}
        }
        return properties;
    }

}
