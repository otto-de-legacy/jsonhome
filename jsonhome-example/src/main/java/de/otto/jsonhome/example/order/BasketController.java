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
package de.otto.jsonhome.example.order;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Rel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller used to handle requests to the shopping basket of the example web-shop.
 * <p/>
 * TODO Currently not yet implemented.
 *
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/baskets")
@Doc(value = "Shopping basket, used to hold products a customer is about to buy.",
     rel = "/rel/get-shopping-basket")
public class BasketController {

    @RequestMapping(
            value = "/{basketId}",
            method = RequestMethod.GET,
            produces = "text/html"
    )
    @Rel("/rel/get-shopping-basket")
    public ModelAndView getBasketAsHtml(@Doc("The unique identifier of the requested shopping basket.")
                                        @PathVariable final long basketId) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @RequestMapping(
            value = "/{basketId}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    @Rel("/rel/get-shopping-basket")
    public ModelAndView getBasketAsJson(@Doc("The unique identifier of the requested shopping basket.")
                                        @PathVariable final long basketId) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
