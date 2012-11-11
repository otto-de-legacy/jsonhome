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

import java.util.UUID;

/**
 * A product, having only a few attributes.
 *
 * @author Guido Steinacker
 * @since 29.09.12
 */
public final class Product {
    private final long id;
    private final String title;
    private final String price;
    private final String etag;

    public Product(final long id, final String title, final String price) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.etag = UUID.randomUUID().toString();
    }

    public long getId() {
        return id;
    }

    public String getLink() {
        return "products/" + id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getETag() {
        return etag;
    }
}
