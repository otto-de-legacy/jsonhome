<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>json-home</title>
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
    <#if resource.hints.docs.hasDescription()>
        <#list resource.hints.docs.description as doc>
                ${doc}<br/>
        </#list>
    </#if>
    <#if resource.hints.docs.hasLink()>
                See <a href="${resource.hints.docs.link}">${resource.hints.docs.link}</a>
    </#if>
            </td>
        </tr>
</#list>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="3">&nbsp;</td>
        </tr>
        </tfoot>
    </table>

</body>
</html>