package de.steinacker.jsonhome.example.storefront;

import de.steinacker.jsonhome.annotation.LinkRelationType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/storefront")
@LinkRelationType("/rel/storefront")
public class StorefrontController {

    @RequestMapping(produces = "text/html")
    public String getStorefront() {
        return "storefront";
    }

}
