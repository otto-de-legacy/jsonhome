It is possible to include Markdown documents into the description of your API, like this one.

You could add loads of documentation here, including yUML

%%% yuml
[Customer]<>-orders*>[Order]
[Order]++-0..*>[LineItem]
[Order]-[note:Aggregate root.]
%%%

See [markdown4j project](http://code.google.com/p/markdown4j/) for details.