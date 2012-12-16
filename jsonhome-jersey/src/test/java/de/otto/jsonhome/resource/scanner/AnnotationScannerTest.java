package de.otto.jsonhome.resource.scanner;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class AnnotationScannerTest {

    @Test
    public void testScanningOnePackage() throws Exception {
        Collection<Class<?>> classes = new AnnotationScanner(new HashSet<String>(
                Arrays.asList("de.otto.jsonhome.fixtures.one"))).scanClasses();
        assertEquals(2, classes.size());
    }

    @Test
    public void testScanningTwoPackages() throws Exception {
        Collection<Class<?>> classes = new AnnotationScanner(new HashSet<String>(
                Arrays. asList("de.otto.jsonhome.fixtures.one", "de.otto.jsonhome.fixtures.two"))).scanClasses();
        assertEquals(4, classes.size());
    }

    @Test
    public void testScanningNotExistingPackage() throws Exception {
        Collection<Class<?>> classes = new AnnotationScanner(new HashSet<String>(
                Arrays. asList("de.otto.jsonhome.fixtures.none"))).scanClasses();
        assertTrue(classes.isEmpty());
    }
}
