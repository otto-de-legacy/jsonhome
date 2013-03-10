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

package de.otto.jsonhome.controller;

import de.otto.jsonhome.generator.JsonHomeGenerator;
import de.otto.jsonhome.generator.JsonHomeSource;
import de.otto.jsonhome.generator.SpringJsonHomeGenerator;
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
public class HtmlControllerTest {

    @Test
    public void testGetRel() throws Exception {
        // given
        final HtmlController controller = relController(ControllerWithRequestMappingAndLinkRelationTypeAtClassLevel.class);
        // when
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/rel/foo");
        request.setServerName("rel.example.org");
        request.setScheme("http");
        final ModelAndView resourcesMap = controller.getRelationshipType(request);
        // then
        assertEquals(resourcesMap.getViewName(), "directresource");
        assertNotNull(resourcesMap.getModel().get("resource"));
        @SuppressWarnings("unchecked")
        final DirectLink model = (DirectLink) resourcesMap.getModel().get("resource");
        assertEquals(model.getHref(), URI.create("http://app.example.org/bar"));
    }

    private HtmlController relController(final Class<?> controllerType) {
        final HtmlController controller = new HtmlController();
        controller.setJsonHomeSource(getJsonHomeSource(controllerType));
        controller.setRelationTypeBaseUri("http://rel.example.org");
        return controller;
    }

    private JsonHomeSource getJsonHomeSource(final Class<?> controllerType) {
        final GeneratorBasedJsonHomeSource source = new GeneratorBasedJsonHomeSource();
        source.setControllerTypes(controllerType);
        source.setJsonHomeGenerator(jsonHomeGenerator());
        return source;
    }

    private JsonHomeGenerator jsonHomeGenerator() {
        final SpringJsonHomeGenerator jsonHomeGenerator = new SpringJsonHomeGenerator();
        jsonHomeGenerator.setApplicationBaseUri("http://app.example.org");
        jsonHomeGenerator.setRelationTypeBaseUri("http://rel.example.org:80");
        jsonHomeGenerator.postConstruct();
        return jsonHomeGenerator;
    }

}
