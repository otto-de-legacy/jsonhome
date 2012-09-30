package de.otto.jsonhome.example.products;

import de.otto.jsonhome.annotation.LinkRelationType;
import de.otto.jsonhome.annotation.VarType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @RequestMapping(
            produces = "text/html"
    )
    @LinkRelationType(
            value = "/rel/products",
            description = "Returns the collection of all products."
    )
    public ModelAndView getProducts(final @RequestParam(required = false, defaultValue = "*") String query) {
        final List<Product> products = productService.findProducts(query);
        return new ModelAndView("products", singletonMap("products", products));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = "application/x-www-form-urlencoded"
    )
    @LinkRelationType(
            value = "/rel/products",
            description = "Adds a new product to the collection of all products and returns the id of the new product in the Location header."
    )
    public ModelAndView addProduct(final @RequestParam String title,
                                   final @RequestParam String price,
                                   final HttpServletResponse response) {
        final long id = productService.addProduct(title, price);
        final List<Product> products = productService.findProducts("*");
        response.setHeader("Location", String.valueOf(id));
        response.setStatus(201);
        return new ModelAndView("products", singletonMap("products", products));
    }

    @RequestMapping(
            value = "/{productId}",
            method = RequestMethod.GET,
            produces = "text/html"
    )
    @LinkRelationType(
            value = "/rel/product",
            description = "Retrieves a single product from the collection of all products."
    )
    @VarType(
            value = "productId",
            description = "The id of the product",
            reference = "/rel/product-id"
    )
    public ModelAndView getProduct(final @PathVariable long productId) {
        final Product product = productService.findProduct(productId);
        return new ModelAndView("product", singletonMap("product", product));
    }

    @RequestMapping(
            value = "/{productId}",
            method = RequestMethod.PUT,
            consumes = "application/x-www-form-urlencoded"
    )
    @LinkRelationType(
            value = "/rel/product",
            description = "Updates a product."
    )
    public ModelAndView putProduct(final @PathVariable long productId,
                                   final @RequestParam String title,
                                   final @RequestParam String price,
                                   final HttpServletResponse response) {
        response.setStatus(201);
        final Product product = productService.createOrUpdateProduct(productId, title, price);
        return new ModelAndView("product", singletonMap("product", product));
    }

}
