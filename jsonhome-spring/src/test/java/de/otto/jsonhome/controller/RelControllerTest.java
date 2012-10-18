/*
 * *
 *  Copyright 2012 Guido Steinacker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package de.otto.jsonhome.controller;

import de.otto.jsonhome.generator.*;
import de.otto.jsonhome.model.DirectLink;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.Test;

import java.net.URI;

import static de.otto.jsonhome.fixtures.ControllerFixtures.ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Guido Steinacker
 * @since 05.10.12
 */
public class RelControllerTest {

    @Test
    public void testGetRel() throws Exception {
        // given
        final RelController controller = relController(ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class);
        // when
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/rel/foo");
        final ModelAndView resourcesMap = controller.getRelationshipType(request);
        // then
        assertEquals(resourcesMap.getViewName(), "doc/directresource");
        assertNotNull(resourcesMap.getModel().get("resource"));
        @SuppressWarnings("unchecked")
        final DirectLink model = (DirectLink) resourcesMap.getModel().get("resource");
        assertEquals(model.getHref(), URI.create("http://example.org/bar"));
    }

    private RelController relController(final Class<?> controllerType) {
        final RelController controller = new RelController();
        controller.setControllerTypes(controllerType);
        controller.setApplicationBaseUri("http://example.org");
        controller.setJsonHomeGenerator(getJsonHomeGenerator());
        return controller;
    }

    private JsonHomeGenerator getJsonHomeGenerator() {
        JsonHomeGenerator jsonHomeGenerator = new SpringJsonHomeGenerator();
        jsonHomeGenerator.setResourceLinkGenerator(getResourceLinkGenerator());
        return jsonHomeGenerator;
    }

    private ResourceLinkGenerator getResourceLinkGenerator() {
        final SpringResourceLinkGenerator resourceLinkgGenerator = new SpringResourceLinkGenerator();
        resourceLinkgGenerator.setApplicationBaseUri("http://example.org");
        resourceLinkgGenerator.setRelationTypeBaseUri("http://example.org");
        resourceLinkgGenerator.setHintsGenerator(new SpringHintsGenerator());
        return resourceLinkgGenerator;
    }


}
