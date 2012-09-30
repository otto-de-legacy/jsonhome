package de.otto.jsonhome.example.products;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Arrays.asList;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

/**
 * @author Guido Steinacker
 * @since 29.09.12
 */
@Service
public final class ProductService {

    private static final AtomicLong nextId = new AtomicLong(0);
    private static final ConcurrentMap<Long, Product> PRODUCTS = new ConcurrentHashMap<Long, Product>();
    static {
        Product p = new Product(nextId.incrementAndGet(), "Notebook", "999");
        PRODUCTS.put(p.getId(), p);
        p = new Product(nextId.incrementAndGet(), "PC", "897");
        PRODUCTS.put(p.getId(), p);
    }

    public long addProduct(final String title, final String price) {
        final long id = nextId.incrementAndGet();
        PRODUCTS.put(id, new Product(id, title, price));
        return id;
    }

    public List<Product> findProducts(final String query) {
        return new ArrayList<Product>(PRODUCTS.values());
    }

    public Product findProduct(final long id) {
        return PRODUCTS.get(id);
    }

    public Product createOrUpdateProduct(final long id, final String title, final String price) {
        final Product product = new Product(id, title, price);
        PRODUCTS.put(id, product);
        return product;
    }
}
