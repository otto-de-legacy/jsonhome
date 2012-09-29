<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Example Shop - Product List</title>
</head>
<body>

    <h1>Lots of wunderful products:</h1>

    <ul>
    <#list products as product>
        <li><a href="${product.link}">${product.title}</a> - ${product.price} &euro;</li>
    </#list>
    </ul>

</body>
</html>