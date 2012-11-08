# JSONHOME

Libraries to publish and use json-home documents.

## 1. Json-Home?

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

## 2. Usage

### 2.1 Serving json-home documents

A simple Spring MVC controller is looking like this:
```
@Controller
@RequestMapping(value = "/helloworld")
public class HelloWorldController {

    @RequestMapping(produces = "text/plain")
    @ResponseBody
    public String sayHello() {
        return "Hello World!";
    }
    
}
```

If you want to have a json-home document for the different entry-point resources of your application, you have to
do the following:

1. Add a dependency to jsonhome-spring to your application.

2. Have a look to the example application and adopt the json-home related things to your application (configuration, 
css, templates, properties). If you think that this is far too complicated - well, I think you are right. 
I promise you to improve this in a few days.

3. Identify the entry-point resources of your RESTful application. If your resources are using hypermedia intensively,
these will be only a few. If not, you will end up with many entry-point resources (resources that are not
reachable by following links contained in other resources). If you find lots of them, this is
most likely a hint that your REST API is not at level 3 of Richardsons Maturity Model
(see http://martinfowler.com/articles/richardsonMaturityModel.html).

4. Add a @Rel annotation to all controller methods, dealing with your entry-point resources:
```
@Controller
@RequestMapping(value = "/helloworld")
public class HelloWorldController {

    @Rel("/rel/hello-world")
    @RequestMapping(produces = "text/plain")
    @ResponseBody
    public String sayHello() {
        return "Hello World!";
    } 
}
```

That's basically all you have to do to get a json-home document like this:
```
{ "resources" : { 
   "http://localhost:8080/jsonhome-example/rel/hello-world" : {
      "href" : "http://localhost:8080/jsonhome-example/helloworld",
      "hints" : {
        "allow" : [
          "GET"
        ],
        "representations" : [
          "text/plain"
        ]
      }
   }
}}
```
The "http://localhost:8080" part of the URI is surely not a good idea in practice. In a real application, 
you should use absolute URIs like "http://mycompany.com/rel/hello-world".

The example application is using the RelController in order to resolve the link-relation URIs. Just open
the resource from /jsonhome-example/json-home using your browser, and you will see the result.

The RelController is able to serve a human-readable representation of the json-home document (as HTML). 
This is especially important for developers using your REST resources, because it is easier to read than 
JSON and - in contrast to hand-written documentation - never out-to-date.

You may want to enrich this documentation by adding @Doc annotations to your controller (or href-variables):
```
@Controller
@RequestMapping(value = "/helloworld")
@Doc(value = {"A link to a hello-world resource", 
              "Multiple lines of documentation are also supported"},
     link = "http://example.org/some-external-documentation.html"
     rel = "/rel/hello-world"
)
public class HelloWorldController {
...
```
The rel attribute of the @Doc is referring to the link-relation type. The value and/or the link to the documentation
will be rendered into the HTML documentation, rendered by the RelController using some Freemarker templates. Feel free
to modify the templates to your needs.

There are some more possibilities like overriding href or href-template (using @Href or @HrefTemplate), specify required
preconditions (etag, last-modified) or set the status (deprecated, gone) of a resource using @Hints.

Please have a look at the example again, or download the sources and check the unit-tests for more examples.

### 2.2 Consuming json-home documents

There is also a very small client-library and a json-home parser available. If you want to consume a json-home document
(access the linked resources without building "known" URIs using string-magic), you may try the following:
```
final URI rel = URI.create("http://example.org/rel/foo");

final JsonHomeClient client = new HttpJsonHomeClient();
final JsonHome jsonHome = client.get(URI.create("http://example.org/json-home");
if (jsonHome.hasResourceFor(rel)) {
   final DirektLink link = jsonHome.getResourceFor(rel).asDirectLink();
   final URI uri = link.getHref();
   // GET the uri using your favorite HTTP client...
   ...
}
```

Accessing a templated link like http://example.org/{foo} is nearly as easy. Remember that in json-home, the variable foo
is not identified by name, but by an URI (=>URI varType).
```
final URI rel = URI.create("http://example.org/rel/foo");
final URI varType = URI.create("http://example.org/var-type/foo");

final JsonHomeClient client = new HttpJsonHomeClient();
final JsonHome jsonHome = client.get(URI.create("http://example.org/json-home");
if (jsonHome.hasResourceFor(rel)) {
   final TemplatedLink templatedLink = jsonHome.getResourceFor(rel).asTemplatedLink();
   final URI uri = templatedLink.expandToUri(varType, "42");
   // GET the uri using your favorite HTTP client...
   ...
}
```

The HttpJsonHomeClient is supporting HTTP caching so the client is not hitting the server all the time. You only
should reuse the client instance, otherwise the caching (at least in the default in-memory implementation) will
not work. You may want to force an update of a cached resource (for example, if a resource is not accessible 
anymore): in this case you should call client.updateAndGet() instead of get().

Internally, the client is based on Apache's CachingHttpClient. You may want to use the same client to access
the resource itself - but this is up to you. Providing a full "REST client" is out of scope of this project. 

## 3. More Features

A more complete support of the json-home spec is already planned, please have a look at the GitHub Issues. Release
1.0 should support the full specification, as soon as the specification itself will be final. 

For example, there will be a separate module used to serve json-home documents that are aggregated from different
sources. This is intended for situations, where your API consists of resources handled by separate servers. For example:
a web shop, having a product-system and a separate order-system. 

Some things from the specification are still missing. Only "optional" information contained in the hints, but anyway.
The missing parts will be added before release 1.0.

## 4. Work in Progress!

* The project is in an early state. Many details will change in the next weeks, possibly in an incompatible way.
* The json-home specification is still a draft, it might change (and will be extended) itself in the next months.
* This library does not yet fully support the current draft specification.
* But: it is already working. You can use it to easily generate json-home documents for your RESTful Spring application.
JAX-RS support is already planned and will be added to version 0.2 in the next few weeks.

The library is actively used (and developed) at otto (http://www.otto.de).

## 5. Licensing

The project is released under version 2.0 of the Apache License. See LICENSE.txt for details.

## 6. Maven, Gradle

You can find all releases in Maven Central and in the public Sonatype repository:

https://oss.sonatype.org/content/repositories/releases

The current release is 0.1.0:

* de.otto:jsonhome-core:0.1.0
* de.otto:jsonhome-generator:0.1.0
* de.otto:jsonhome-spring:0.1.0

Snapshot releases will be published here:

https://oss.sonatype.org/content/repositories/snapshots

The current snapshot-release is 0.2.0-SNAPSHOT:

* de.otto:jsonhome-core:0.2.0-SNAPSHOT
* de.otto:jsonhome-generator:0.2.0-SNAPSHOT
* de.otto:jsonhome-spring:0.2.0-SNAPSHOT

There is no de.otto:jsonhome-example:* because this is only an example, you should not depend on it.

## 7. Feedback + Help Wanted

Every kind of feedback - also negative - is appreciated. Even more appreciated are contributions to the code base.

To contact us, please send an email to guido.steinacker@gmail.com

## 8. Links and Documentation

Have a look at the wiki pages.

You can find information about json-home in the draft specification:
Json-Home: http://tools.ietf.org/html/draft-nottingham-json-home-02

The concept of URI Templates is defined here:
http://tools.ietf.org/html/rfc6570

For information about the concept of link-relation types:
http://tools.ietf.org/html/rfc5988
