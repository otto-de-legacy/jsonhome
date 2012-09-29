package de.otto.jsonhome.example.products;

import de.otto.jsonhome.annotation.LinkRelationType;
import de.otto.jsonhome.annotation.VarType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.singletonMap;


/**
 * @author Guido Steinacker
 * @since 15.09.12
 */
@Controller
@RequestMapping(value = "/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @RequestMapping(produces = "text/html")
    @LinkRelationType(
            value = "/rel/products",
            description = "The collection of all products."
    )
    public ModelAndView getProducts(final @RequestParam(required = false, defaultValue = "*") String query) {
        final List<Product> products = productService.findProducts(query);
        return new ModelAndView("products", singletonMap("products", products));
    }

    @RequestMapping(value = "/{productId}", produces = "text/html")
    @LinkRelationType(
            value = "/rel/product",
            description = "A single product"
    )
    @VarType(
            value = "productId",
            description = "The id of the product",
            reference = "/rel/product-id")
    public ModelAndView getProduct(final @PathVariable String productId) {
        final Product product = productService.findProduct(productId);
        return new ModelAndView("product", singletonMap("product", product));
    }
}
