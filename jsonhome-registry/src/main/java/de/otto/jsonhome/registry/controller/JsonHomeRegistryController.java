/**
 Copyright 2012 Guido Steinacker

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.otto.jsonhome.registry.controller;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Rel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Controller used to serve the hello-world resource.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@Rel("/rel/json-home/registry")
@Doc(rel = "/rel/json-home/registry",
     value = "A registry of json-home documents"
)
public class JsonHomeRegistryController {

    private URI applicationBaseUri = null;

    @Value("${jsonhome.applicationBaseUri}")
    public void setApplicationBaseUri(final String baseUri) {
        this.applicationBaseUri = URI.create(baseUri);
    }

    @RequestMapping(
            value = "/registry",
            produces = "application/json")
    @ResponseBody
    public Map<String, ?> getJsonHome(final HttpServletResponse response) {
        final File file = new File(System.getProperty("user.home") + "/.jsonhome/registry.json");
        try {
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.append("foo");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        response.setHeader("Cache-Control", "max-age=3600");
        final Map<String, String> link = new HashMap<String, String>();
        link.put("href", applicationBaseUri.toString() + "/json-home");
        link.put("title", "Json-home document of the json-home registry.");
        link.put("path", file.getAbsolutePath());
        final Map<String, Map<String, String>> entries = new HashMap<String, Map<String, String>>();
        entries.put(UUID.randomUUID().toString(), link);
        final Map<String, Object> json = new HashMap<String, Object>();
        json.put("registry", entries);
        return json;
    }

}
