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
