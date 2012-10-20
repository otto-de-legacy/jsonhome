# JSONHOME

Libraries to publish and use json-home documents.

## Json-Home?

Think of an machine-readable alternative to an index.html in json format, describing the REST resources of an
application. If the caller knows the format, no URIs must be constructed using string-magic. The resources
of the application will become navigable.

An example from the draft specification:

```
   GET / HTTP/1.1
   Host: example.org
   Accept: application/json-home

   HTTP/1.1 200 OK
   Content-Type: application/json-home
   Cache-Control: max-age=3600
   Connection: close

   {
     "resources": {
       "http://example.org/rel/widgets": {
         "href": "/widgets/"
       },
       "http://example.org/rel/widget": {
         "href-template": "/widgets/{widget_id}",
         "href-vars": {
           "widget_id": "http://example.org/param/widget"
         },
         "hints": {
           "allow": ["GET", "PUT", "DELETE", "PATCH"],
           "representations": ["application/json"],
           "accept-patch": ["application/json-patch"],
           "accept-post": ["application/xml"],
           "accept-ranges": ["bytes"]
         }
       }
     }
   }
```

## More Features

The json-home format is nice for automatic discovery of RESTful services. Generating this format directly from source
code is nice, because it will always be consistent with your application. Being consistent is also important for
human-readable documentation - and the information needed to generate such kind of documentation is more or less
available in your source code.

A more complete support of the json-home spec is already planned, please have a look at the GitHub Issues. 

In order to consume json-home documents, a client-side library is needed. This will be implemented in the future.

## Work in Progress!

* The project is in an early state. Many details will change in the next weeks, possibly in an incompatible way.
* The json-home specification is still a draft, it might change itself in the next months.
* This library does not yet fully support the current draft specification.
* But: it is already working. You can use it to easily generate json-home documents for your RESTful Spring application.
The library is actively used (and developed) at otto (http://www.otto.de).

## Licensing

The project is released under version 2.0 of the Apache License. See LICENSE.txt for details.

## Feedback + Help Wanted

Every kind of feedback - also negative - is appreciated. Even more appreciated are contributions to the code base.

To contact us, please send an email to guido.steinacker@gmail.com

## Links and Documentation

Have a look to the wiki pages.

You can find information about json-home in the draft specification:
Json-Home: http://tools.ietf.org/html/draft-nottingham-json-home-02

The concept of URI Templates is defined here:
http://tools.ietf.org/html/rfc6570

For information about the concept of link-relation types:
http://tools.ietf.org/html/rfc5988
