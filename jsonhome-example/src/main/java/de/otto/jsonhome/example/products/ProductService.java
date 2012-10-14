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

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

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

    public Product createOrUpdateProduct(final Product product) {
        return PRODUCTS.put(product.getId(), product);
    }
}
