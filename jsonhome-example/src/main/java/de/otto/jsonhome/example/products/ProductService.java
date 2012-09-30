package de.otto.jsonhome.example.products;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * @author Guido Steinacker
 * @since 29.09.12
 */
@Service
public final class ProductService {

    private static final List<Product> PRODUCTS = unmodifiableList(asList(
            new Product("products/1", "Notebook", "999"),
            new Product("products/2", "PC", "897")
    ));

    public List<Product> findProducts(final String query) {
        return PRODUCTS;
    }

    public Product findProduct(final String id) {
        for (final Product product : PRODUCTS) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
}
