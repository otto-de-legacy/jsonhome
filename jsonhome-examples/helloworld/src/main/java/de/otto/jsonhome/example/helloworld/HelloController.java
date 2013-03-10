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
package de.otto.jsonhome.example.helloworld;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Rel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller used to serve the hello-world resource.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@Rel("/rel/hello-world-example")
@Doc(rel = "/rel/hello-world-example",
     value = "The hello-world example resource"
)
public class HelloController {

    @RequestMapping(value = "/", produces = {"text/html", "*/*"})
    @ResponseBody
    public String getHelloWorld() {
        return "<h1>Hello World!</h1><p>Open <a href=\"json-home\">json-home</a></p>";
    }

    @RequestMapping(value = "/", produces = "text/plain")
    @ResponseBody
    public String getHelloWorldAsPlainText() {
        return "Hello World!";
    }
}
