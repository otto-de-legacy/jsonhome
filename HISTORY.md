# Version History

## Release 0.3.4
* Bugfix: Small change in JacksonJsonHomeParser to be able to use jsonhome
  with Java 6

## Release 0.3.3

* Bugfix: Solved problems with duplicate slashes in different URIs
* Bugfix: Using request parameters instead of path variables to specify the registry. Fixed usage of default registries.

## Release 0.3.1

### 0.3.1 Features

* Feature: New DocController with Markdown support. This may be used to serve documentation of vendor-specific
  media-types, link-relation types, and so on. The DocController is currently available in the jsonhome-spring module
  and can also be used in combination with the jsonhome-registry.
* Feature: The accept-ranges hint is now supported.
* Feature: The prefer hint is now supported.

### 0.3.1 Known Incompatibilities to 0.3.0

* There is a new property `jsonhome.docRootDir` that must either be empty (if you are not using Markdown) or
point to the root classpath containing the Markdown documents: `jsonhome.docRootDir=/docs/*`
* Documentation has a new attribute `detailedDescription`. If you have a copy of the FTL templates to
render json-home documents as HTML, you should consider to add the new attribute to your template.
You can find an example in the jsonhome-generator templates (directresource.ftl and templatedresource.ftl)

## Release 0.3.0

With beginning of release 0.3.0, the jsonhome library is used inside the otto.de development. This will bring
the library into a state that is applicable to use it in live real-world applications.

From now on, this version history will be maintained on a regular basis. Incompatibilities to
earlier versions will be tracked, as well as new features, bug fixes, and so on.

### 0.3.0 Features

* Feature: It is now possible to create multiple registries, containing different sets of json-home documents.
* Feature: The registry is now able to serve json-home documents, describing the link-relations of all registered applications.
* Fearure: Configurable base URIs of href-var types in fragment or sub-resource notation.
  This requires a new property jsonhome.varTypeBaseUri to be configured in your application
* Feature: Support of the 'auth-req' hint.

### 0.3.0 Known Incompatibilities to earlier versions

* You must add a new property to your application: jsonhome.varTypeBaseUri must be added to your properties.
The value may be null / empty, in this case the var-type URI is constructed as a fragment of the relation-type URIs as
before. If set to something more meaningful, the href-var type URIs are constructed as a sub-resource of the
relation-type URI.
