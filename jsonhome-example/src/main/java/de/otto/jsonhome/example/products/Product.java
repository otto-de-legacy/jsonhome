package de.otto.jsonhome.example.products;

/**
 * @author Guido Steinacker
 * @since 29.09.12
 */
public final class Product {
    private final String link;
    private final String title;
    private final String price;

    public Product(final String link, final String title, final String price) {
        this.link = link;
        this.title = title;
        this.price = price;
    }

    public String getId() {
        return link.substring(link.lastIndexOf("/")+1);
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }
}
