It is possible to include Markdown documents into the description of your API, like this one.

You could add loads of documentation here, including yUML

%%% yuml
[Customer]<>-orders*>[Order]
[Order]++-0..*>[LineItem]
[Order]-[note:Aggregate root.]
%%%

or WebSequence diagrams:

%%% sequence style=default
title Authentication Sequence

Alice->Bob: Authentication Request
note right of Bob: Bob thinks about it
Bob->Alice: Authentication Response
%%%

See [markdown4j project](http://code.google.com/p/markdown4j/) for details.