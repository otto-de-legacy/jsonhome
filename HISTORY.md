# Version History

## Release 0.3.1

With beginning of release 0.3.0, the jsonhome library is used inside the otto.de development. This will bring
the library into a state that is applicable to use it in live real-world applications.

Beginning with release 0.3.1, this version history will be maintained on a regular basis. Incompatibilities to
earlier versions will be tracked, as well as new features, bug fixes, and so on.

### 0.3.1 Features

* Registry: Multiple environment support.
* Core: Configurable base URIs of href-var types in fragment or sub-resource notation.
  This requires a new property jsonhome.varTypeBaseUri to be configured in your application

### 0.3.1 Known Incompatibilities to 0.3.0

* You must add a new property to your application: jsonhome.varTypeBaseUri must be added to your properties.
The value may be null / empty, in this case the var-type URI is constructed as a fragment of the relation-type URIs as
before. If set to something more meaningful, the href-var type URIs are constructed as a sub-resource of the
relation-type URI.