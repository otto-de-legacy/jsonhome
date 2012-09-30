JSONHOME4SPRING
===============

A json-home generator for RESTful web applications.

JSON-HOME???
============

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

MORE FEATURES
=============

The json-home format is nice for automatic discovery of RESTful services. Generating this format directly from source
code is nice, because it will always be consistent with your application. Being consistent is also important for≤
human-readable documentation - and the information needed to generate such kind of documentation is more or less
available in your source code. Because of this, we will provide an optional module used to generate such kind of
documentation from the code itself.

A more complete support of the json-home spec is already planned, please have a look at the GitHub Issues. If you
need something else, please give us a note (or open an issue) or, even better, implemented it and send us a pull
request.

In order to consume json-home documents, a client-side library is needed. This will be implemented in the future.

THIS IS WORK IN PROGRESS!
=========================

* The project is in an very early state. Everything will change in the next weeks, most of it in an incompatible way.
* The json-home specification is still a draft, it will possibly change itself in the next months.
* This library does not yet fully support the current draft specification.
* The current implementation is using a number of annotations to provide the information needed to generate the
json-home document. These annotations will possibly change in the next few weeks, as the current implementation is
too verbose.
* But: it is already working. You can use it to easily generate json-home documents for your RESTful Spring application.
The library is actively used (and developed) at otto.

LICENSE
=======

Well - we have to think about it. If you care about licenses, please come back in a few weeks. In the meantime, feel
free to evaluate (and use) the code. It will definitly remain open source. Currently, the full code-base is still owned
by me, so no evil company lawyer will be able to do any harm.

FEEDBACK + HELP WANTED
======================

Every kind of feedback - also negative - is appreciated. Even more appreciated are contributions to the code base.

LINKS & DOCUMENTATION
=====================

You can find information about json-home in the draft specification:
Json-Home: http://tools.ietf.org/html/draft-nottingham-json-home-02

The concept of URI Templates is defined here:
http://tools.ietf.org/html/rfc6570

For information about the concept of link-relation types:
http://tools.ietf.org/html/rfc5988
