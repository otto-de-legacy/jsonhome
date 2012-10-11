<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Example Shop - REST API</title>
    <link href="${contextpath}/resources/css/jsonhome.css" rel="stylesheet" type="text/css">
</head>
<body>

    <h1>Resource Overview</h1>
    <table>
        <thead>
        <tr>
            <th>Link-Relation Type</th>
            <th>Documentation</th>
        </tr>
        </thead>
        <tbody>
<#list resources as resource>
        <tr>
            <td><a href="${resource.linkRelationType}">${resource.linkRelationType}</a></td>
            <td>
    <#list resource.documentation.description as doc>
                ${doc}<br/>
    </#list>

            </td>
        </tr>
</#list>
        </tbody>
    </table>

</body>
</html>