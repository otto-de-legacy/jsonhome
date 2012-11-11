/*
 * *
 *  Copyright 2012 Guido Steinacker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.otto.jsonhome.example.products;

import java.util.*;

/**
 * Converter used to convert products to json and vice versa.
 * <p/>
 * TODO: Using the Jackson library would make this looking better...
 *
 * @author Guido Steinacker
 * @since 14.10.12
 */
public final class ProductConverter {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String PRICE = "price";
    public static final String ETAG = "etag";
    public static final String HREF = "href";
    public static final String REL = "rel";
    public static final String LINK = "link";
    public static final String PRODUCTS = "products";

    private ProductConverter() {
    }

    public static Map<String, Object> productToJson(final Product product, final String contextPath) {
        final Map<String, Object> json = new LinkedHashMap<String, Object>();
        json.put(ID, product.getId());
        json.put(TITLE, product.getTitle());
        json.put(PRICE, product.getPrice());
        json.put(ETAG, product.getETag());
        final Map<String, String> link = new LinkedHashMap<String, String>();
        link.put(HREF, contextPath + "/products/" + product.getId());
        link.put(REL, contextPath + "/rel/product");
        json.put(LINK, link);
        final Map<String, String> collectionLink = new LinkedHashMap<String, String>();
        collectionLink.put(HREF, contextPath + "/products");
        collectionLink.put(REL, contextPath + "/rel/products");
        json.put(PRODUCTS, collectionLink);
        return json;
    }

    public static Map<String, Object> productsToJson(final Collection<Product> products, final String contextPath) {
        final List<Object> jsonProducts = new ArrayList<Object>();
        for (final Product product : products) {
            final Map<String, Object> jsonProduct = productToJson(product, contextPath);
            jsonProduct.remove(PRODUCTS);
            jsonProducts.add(jsonProduct);
        }
        return Collections.<String,Object>singletonMap(PRODUCTS, jsonProducts);
    }

    @SuppressWarnings("unchecked")
    public static Product jsonToProduct(final long id, final Map<String, String> document) {
        try {
            final String title = document.get(TITLE);
            final String price = document.get(PRICE);
            return new Product(id, title, price);
        } catch (final NullPointerException e) {
            throw new IllegalArgumentException("Illegal format.");
        }
    }

    @SuppressWarnings("unchecked")
    public static Product jsonToProduct(final Map<String, Object> document) {
        try {
            return new Product(
                    (Long) document.get(ID),
                    (String) document.get(TITLE),
                    (String) document.get(PRICE)
            );
        } catch (final Exception e) {
            throw new IllegalArgumentException("Illegal format.");
        }
    }
}
