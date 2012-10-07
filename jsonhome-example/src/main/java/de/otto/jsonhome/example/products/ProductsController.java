package de.otto.jsonhome.example.products;

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
public class ProductsController {

    @Autowired
    private ProductService productService;

    @RequestMapping(
            produces = "text/html"
    )
    @Rel("/rel/products")
    public ModelAndView getProducts(final @RequestParam(required = false, defaultValue = "*") String query) {
        final List<Product> products = productService.findProducts(query);
        return new ModelAndView("example/products", singletonMap("products", products));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = "application/x-www-form-urlencoded"
    )
    @Rel("/rel/add-product")
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
    public ModelAndView getProductAsHtml(final @PathVariable long productId) {
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
