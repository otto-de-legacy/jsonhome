# Version History

## Release 0.3.1

With beginning of release 0.3.0, the jsonhome library is used inside the otto.de development. This will bring
the library into a state that is applicable to use it in live real-world applications.

Beginning with release 0.3.1, this version history will be maintained on a regular basis. Incompatibilities to
earlier versions will be tracked, as well as new features, bug fixes, and so on.

### 0.3.1 Features

* Feature: It is now possible to create multiple registries, containing different sets of json-home documents.
* Feature: The registry is now able to serve json-home documents, describing the link-relations of all registered applications.
* Feature: New DocController with Markdown support. This may be used to serve documentation of vendor-specific
  media-types, link-relation types, and so on. The DocController is currently available in the jsonhome-spring module
  and can also be used in combination with the jsonhome-registry. 
* Fearure: Configurable base URIs of href-var types in fragment or sub-resource notation.
  This requires a new property jsonhome.varTypeBaseUri to be configured in your application
* Feature: Support of the 'auth-req' hint.

### 0.3.1 Known Incompatibilities to 0.3.0

* You must add a new property to your application: jsonhome.varTypeBaseUri must be added to your properties.
The value may be null / empty, in this case the var-type URI is constructed as a fragment of the relation-type URIs as
before. If set to something more meaningful, the href-var type URIs are constructed as a sub-resource of the
relation-type URI.
