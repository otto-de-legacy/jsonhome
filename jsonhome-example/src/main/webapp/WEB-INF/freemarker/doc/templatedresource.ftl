<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Example Shop - REST API</title>
    <link href="${contextpath}/resources/css/jsonhome.css" rel="stylesheet" type="text/css">
</head>
<body>
    <h1>${resource.name}</h1>

    <table>
        <thead>
            <tr>
                <th>Attribute</th>
                <th>Value</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
        <tr>
                <th>Rel:</th>
                <td>${resource.linkRelationType}</td>
                <td>The link-relation type of the resource.</td>
        </tr>
        <tr>
                <th>Href-template</th>
                <td>${resource.hrefTemplate}</td>
                <td>The href-template to create links to the resource.</td>
        </tr>
        <tr>
            <th>Href-vars</th>
            <td>
                <table>
                    <tr>
                        <th>Variable</th>
                        <th>Type</th>
                        <th>Description</th>
                    </tr>
                <#list resource.hrefVars as hrefVar>
                    <tr>
                        <th>${hrefVar.var}</th>
                        <td>${hrefVar.varType}</td>
                        <td>${hrefVar.description}</td>
                    </tr>
                </#list>
                </table>
            </td>
            <td>The variables used in the href-template.</td>
        </tr>
        <#include "hints-rows.ftl">
        </tbody>
    </table>
</body>
</html>