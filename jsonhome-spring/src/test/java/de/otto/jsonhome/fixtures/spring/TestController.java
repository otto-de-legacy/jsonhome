package de.otto.jsonhome.fixtures.spring;

import de.otto.jsonhome.annotation.Rel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

        @TestAspectAnnotation
        @Rel("/rel/foo")
        @RequestMapping(value = "/{fooId}", produces = "text/plain")
        @ResponseBody
        public String getAFoo(@PathVariable String fooId) {
            return fooId;
        }

}

