# JSONHOME

Libraries to publish and use [json-home](http://tools.ietf.org/html/draft-nottingham-json-home-02) documents.

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
```
```json
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

Module jsonhome-spring is used to serve json-home documents from your application. Section 2.1
illustrates how to do this.

There is also a module jsonhome-client, used to access remote or local json-home documents. You may
find an example in Section 2.2.

### 2.1 Serving json-home documents

A simple Spring MVC controller is looking like this:
```java
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

### 2.1.1 Add a dependency to jsonhome-spring to your application.

### 2.1.2 Have a look at the hello-world example. You will have to:

  * Specify the properties used by jsonhome (helloworld.properties):

  ```
  jsonhome.applicationBaseUri = <base uri of the resources of your application>
  jsonhome.relationTypeBaseUri = <base uri of the link-relation types of your application>
  ```

  * Import the jsonhome-beans and add a property-placeholder configurer in your Spring bean config:

  ```xml
  <import resource="classpath*:jsonhome/jsonhome-beans.xml" />
  <context:property-placeholder location="/WEB-INF/properties/helloworld.properties" />
  ```

### 2.1.3 Identify the entry-point resources of your RESTful application. If your resources are using hypermedia intensively,
these will be only a few. If not, you will end up with many entry-point resources (resources that are not
reachable by following links contained in other resources). If you find lots of them, this is
most likely a hint that your REST API is not at level 3 of Richardsons Maturity Model
(see http://martinfowler.com/articles/richardsonMaturityModel.html).

### 2.1.4 Add a @Rel annotation to all controller methods, dealing with your entry-point resources:
```java
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

### 2.1.5 Result
That's basically all you have to do to get a json-home document like this:
```json
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
```java
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
```java
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
```java
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

There are some more features like:
* jsonhome-jersey: a Jersey based implementation of jsonhome.
* jsonhome-registry: a standalone server used to serve json-home documents for different environments (develop, test, live). The registry is also able to aggregate multiple json-home documents into one single document.
* DocController: a (currently only Spring-based) controller used to serve Markdown documents.
* HtmlController: a (currently only Spring-based) controller used serve a HTML representation of your json-home, enriched with documentation.

## 4. Project Status

As of draft-nottingham-json-home-02, only the accept-patch hint is not fully supported. Spring is supporting
HTTP PATCH with release 3.2. Because we are currently using Spring 3.1.*, accept-patch is only available with
jsonhome-jersey.

Draft-nottingham-json-home-03 should be published in a few days or weeks. This will open more 'to be implemented' features.

A full support of the json-home spec is already planned, please have a look at the GitHub Issues. Release
1.0 should support the final specification. Until then, minor releases of the jsonhome project will be published. 

The library is actively used (and developed) at otto (http://www.otto.de). You may consider it as "Beta" software.

## 5. Licensing

The project is released under version 2.0 of the Apache License. See LICENSE.txt for details.

## 6. Maven, Gradle

You can find all releases in Maven Central and in the public Sonatype repository:

https://oss.sonatype.org/content/repositories/releases

The current release is 0.3.0:

* de.otto:jsonhome-core:0.3.0
* de.otto:jsonhome-generator:0.3.0
* de.otto:jsonhome-spring:0.3.0

**Because the development is still involving incompatible changes, you may want to use the current 0.3.1-SNAPSHOT**
instead of using the 0.3 release.

Snapshot releases will be published here:

https://oss.sonatype.org/content/repositories/snapshots

The current snapshot-release is 0.3.1-SNAPSHOT:

* de.otto:jsonhome-core:0.3.1-SNAPSHOT
* de.otto:jsonhome-generator:0.3.1-SNAPSHOT
* de.otto:jsonhome-spring:0.3.1-SNAPSHOT

There is no de.otto:jsonhome-example:* because this is only an example, you should not depend on it.

## 7. Contributing

Every kind of feedback - also negative - is appreciated. Even more appreciated are contributions to the code base.

To contact us, please send an email to guido.steinacker@gmail.com

In order to contribute source code:

* Fork it
* Create your feature branch (git checkout -b my-new-feature)
* Commit your changes (git commit -am 'Added some feature')
* Push to the branch (git push origin my-new-feature)
* Create new Pull Request

## 8. Links and Documentation

* **Json-Home draft specification**:
[http://tools.ietf.org/html/draft-nottingham-json-home-02](http://tools.ietf.org/html/draft-nottingham-json-home-02)

* **URI Templates**:
[http://tools.ietf.org/html/rfc6570](http://tools.ietf.org/html/rfc6570)

* **Link-Relation Types**:
[http://tools.ietf.org/html/rfc5988](http://tools.ietf.org/html/rfc5988)

**Please also have a look at the project's [wiki pages](https://github.com/otto-de/jsonhome/wiki).**
