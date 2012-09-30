package de.otto.jsonhome.example.products;

/**
 * @author Guido Steinacker
 * @since 29.09.12
 */
public final class Product {
    private final long id;
    private final String title;
    private final String price;

    public Product(final long id, final String title, final String price) {
        this.id = id;
        this.title = title;
        this.price = price;
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
}
