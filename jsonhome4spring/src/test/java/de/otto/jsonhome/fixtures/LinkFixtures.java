package de.otto.jsonhome.fixtures;

import de.otto.jsonhome.model.DirectLink;
import de.otto.jsonhome.model.HrefVar;
import de.otto.jsonhome.model.TemplatedLink;

import java.net.URI;

import static java.util.Arrays.asList;

/**
 * @author Guido Steinacker
 * @since 16.09.12
 */
public class LinkFixtures {

    public static final URI RESOURCELINK_SHOP_STOREFRONT = URI.create("http://example.org/json-home/rel/shop/storefront");
    public static final URI RESOURCELINK_SHOP_PAGES = URI.create("http://example.org/json-home/rel/shop/pages");
    public static final URI RESOURCELINK_SHOP_PAGE = URI.create("http://example.org/json-home/rel/shop/page");
    public static final URI ABS_STOREFRONT_HREF = URI.create("http://example.org/storefront");
    public static final URI ABS_SHOPPAGES_HREF = URI.create("http://example.org/pages");
    public static final URI VAR_TYPE_PAGEID = URI.create("http://example.org/json-home/vartype/shop/page/pageId");
    public static final URI REL_PAGES_HREF = URI.create("/pages");
    public static final String REL_PAGE_HREF = "/pages/{pageId}";

    public static final DirectLink STOREFRONT_LINK = DirectLink.directLink(
            RESOURCELINK_SHOP_STOREFRONT,
            ABS_STOREFRONT_HREF,
            asList("GET"),
            asList("text/html", "application/json")
    );

    public static final DirectLink SHOPPAGES_LINK = DirectLink.directLink(
            RESOURCELINK_SHOP_PAGES,
            ABS_SHOPPAGES_HREF,
            asList("GET"),
            asList("text/html", "application/json")
    );

    public static final DirectLink PAGES_LINK_WITH_RELATIVE_HREF = DirectLink.directLink(
            RESOURCELINK_SHOP_PAGES,
            REL_PAGES_HREF,
            asList("GET"),
            asList("text/html", "application/json")
    );

    public static final TemplatedLink ABOUTPAGE_LINK = TemplatedLink.templatedLink(
            RESOURCELINK_SHOP_PAGE,
            REL_PAGE_HREF,
            asList(new HrefVar("pageId", VAR_TYPE_PAGEID, "")),
            asList("GET"),
            asList("text/html", "application/json")
    );

}
