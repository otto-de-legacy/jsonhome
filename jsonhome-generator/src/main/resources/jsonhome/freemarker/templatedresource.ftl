<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>json-home</title>
    <link href="${contextpath}/jsonhome/css/jsonhome.css" rel="stylesheet" type="text/css">
</head>
<body>
    <h1>${resource.linkRelationType}</h1>

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
                <th>Rel</th>
                <td>${resource.linkRelationType}</td>
                <td>
<#list resource.hints.docs.description as descr>
                    <p>${descr}</p>
</#list>
<#if resource.hints.docs.detailedDescription?has_content>
                    ${resource.hints.docs.detailedDescription}
                    <br/>
</#if>
<#if resource.hints.docs.link?has_content>
                    <p>See <a href="${resource.hints.docs.link}">${resource.hints.docs.link}</a></p>
</#if>
                </td>
        </tr>
        <tr>
                <th>Href-template</th>
                <td>${resource.hrefTemplate}</td>
                <td>The href-template used to build links to the resource.</td>
        </tr>
        <tr>
            <th>Href-vars</th>
            <td colspan="2">
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
                        <td>
    <#if hrefVar.docs.hasDescription()>
        <#list hrefVar.docs.description as descr>
                            <p>${descr}</p>
        </#list>
    </#if>
    <#if hrefVar.docs.hasDetailedDescription()>
                            ${hrefVar.docs.detailedDescription}
                            <br/>
    </#if>
    <#if hrefVar.docs.hasLink()>
                            <p>See <a href="${hrefVar.docs.link}">${hrefVar.docs.link}</a></p>
    </#if>
                        </td>
                    </tr>
</#list>
                </table>
            </td>
        </tr>
        <#include "hints-rows.ftl">
        </tbody>
        <tfoot>
        <tr>
            <td colspan="3">&nbsp;</td>
        </tr>
        </tfoot>
    </table>
</body>
</html>