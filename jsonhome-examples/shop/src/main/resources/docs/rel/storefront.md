/rel/storefront
===============

This is an example for a link-relation type defining a link to the storefront of an online shop.

You could add loads of documentation here, including yUML

%%% yuml
[Customer]<>-orders*>[Order]
[Order]++-0..*>[LineItem]
[Order]-[note:Aggregate root.]
%%%

See [markdown4j project](http://code.google.com/p/markdown4j/) for details.