package de.otto.jsonhome.example.storefront;

import de.otto.jsonhome.annotation.Rel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/storefront")
@Rel("/rel/storefront")
public class StorefrontController {

    @RequestMapping(produces = "text/html")
    public String getStorefront() {
        return "example/storefront";
    }

}
