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
package de.otto.jsonhome.example.products;

import de.otto.jsonhome.annotation.Doc;
import de.otto.jsonhome.annotation.Rel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;


/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/products")
@Doc(value = {
        "A single product.",
        "This is a second paragraph, describing the link-relation type."},
     link = "http://de.wikipedia.org/wiki/Produkt_(Wirtschaft)"
)
public class ProductsController {

    @Autowired
    private ProductService productService;

    @RequestMapping(
            produces = "text/html"
    )
    @Rel("/rel/products")
    @Doc(value = {
            "The collection of products.",
            "This is a second paragraph, describing the collection of products."
    })
    public ModelAndView getProducts(final @RequestParam(required = false, defaultValue = "*") String query) {
        final List<Product> products = productService.findProducts(query);
        return new ModelAndView("example/products", singletonMap("products", products));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = "application/x-www-form-urlencoded"
    )
    @Rel("/rel/product/form")
    @Doc(value = {
            "Service to create a product.",
            "This is a second paragraph, describing the link-relation type."
    })

    public ModelAndView addProduct(final @RequestParam String title,
                                   final @RequestParam String price,
                                   final HttpServletResponse response) {
        final long id = productService.addProduct(title, price);
        final List<Product> products = productService.findProducts("*");
        response.setHeader("Location", String.valueOf(id));
        response.setStatus(201);
        return new ModelAndView("example/products", singletonMap("products", products));
    }

    @RequestMapping(
            value = "/{productId}",
            method = RequestMethod.GET,
            produces = "text/html"
    )
    @Rel("/rel/product")
    public ModelAndView getProductAsHtml(@Doc({"The unique identifier of the requested product.",
                                               "A second line of valuable documentation."})
                                         @PathVariable final long productId) {
        final Product product = productService.findProduct(productId);
        return new ModelAndView("example/product", singletonMap("product", product));
    }

    @RequestMapping(
            value = "/{productId}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    @Rel("/rel/product")
    public Map<String, Object> getProductAsJson(final @PathVariable long productId) {
        final Product product = productService.findProduct(productId);
        return Collections.<String, Object>singletonMap("product", product.toJson());
    }

    @RequestMapping(
            value = "/{productId}",
            method = RequestMethod.PUT,
            consumes = "application/json"
    )
    @Rel("/rel/product")
    public ModelAndView putProduct(final @PathVariable long productId,
                                   final Map<String, String> productData,
                                   final HttpServletResponse response) {
        response.setStatus(201);
        final String title = productData.get("title");
        final String price = productData.get("price");
        final Product product;
        if (title != null && price != null) {
            product = productService.createOrUpdateProduct(productId, title, price);
        } else {
            product = productService.findProduct(productId);
        }
        return new ModelAndView("example/product", singletonMap("product", product));
    }

}
