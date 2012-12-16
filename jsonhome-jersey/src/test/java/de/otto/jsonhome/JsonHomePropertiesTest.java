package de.otto.jsonhome;


import org.testng.annotations.Test;

import java.util.Properties;

import static org.testng.AssertJUnit.assertEquals;

public class JsonHomePropertiesTest {

    @Test
    public void testReadingProperties() throws Exception {
        Properties properties = JsonHomeProperties.getProperties();
        assertEquals(3, properties.size());
    }
}
