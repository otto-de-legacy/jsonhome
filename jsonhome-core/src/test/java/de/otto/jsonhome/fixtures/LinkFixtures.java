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
package de.otto.jsonhome.fixtures;

import de.otto.jsonhome.model.*;

import java.net.URI;
import java.util.Collections;

import static de.otto.jsonhome.model.Allow.GET;
import static de.otto.jsonhome.model.DirectLink.directLink;
import static de.otto.jsonhome.model.Documentation.emptyDocs;
import static de.otto.jsonhome.model.HintsBuilder.hintsBuilder;
import static de.otto.jsonhome.model.HrefVar.hrefVar;
import static java.util.Arrays.asList;
import static java.util.EnumSet.of;

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
    public static final String REL_PAGE_HREF = "/pages/{pageId}";

    public static final DirectLink STOREFRONT_LINK = directLink(
            RESOURCELINK_SHOP_STOREFRONT,
            ABS_STOREFRONT_HREF,
            Hints.hints(of(GET),
                    asList("text/html", "application/json"),
                    Collections.<String>emptyList(),
                    Collections.<String>emptyList(),
                    Collections.<String>emptyList(),
                    Collections.<Precondition>emptyList(),
                    Status.OK,
                    emptyDocs()
            ));

    public static final DirectLink SHOPPAGES_LINK = directLink(
            RESOURCELINK_SHOP_PAGES,
            ABS_SHOPPAGES_HREF,
            Hints.hints(
                    of(GET),
                    asList("text/html", "application/json"),
                    Collections.<String>emptyList(),
                    Collections.<String>emptyList(),
                    Collections.<String>emptyList(),
                    Collections.<Precondition>emptyList(),
                    Status.OK,
                    emptyDocs()));

    public static final TemplatedLink ABOUTPAGE_LINK = TemplatedLink.templatedLink(
            RESOURCELINK_SHOP_PAGE,
            REL_PAGE_HREF,
            asList(hrefVar("pageId", VAR_TYPE_PAGEID)),
            hintsBuilder()
                    .allowing(GET)
                    .representedAs("text/html", "application/json")
                    .build()
    );

}
