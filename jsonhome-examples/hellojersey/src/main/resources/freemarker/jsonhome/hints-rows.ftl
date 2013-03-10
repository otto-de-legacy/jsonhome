<#assign hints = resource.hints>
    <tr>
        <th>Allows</th>
        <td>
            <ul>
<#list hints.allows as allow>
                <li>${allow}</li>
</#list>
            </ul>
        </td>
        <td>The allowed http methods.</td>
    </tr>
    <tr>
        <th>Representations</th>
        <td>
            <ul>
<#list hints.representations as representation>
                <li>${representation}</li>
</#list>
            </ul>
        </td>
        <td>Available representations of the resource.</td>
    </tr>
    <#if hints.acceptPut?has_content>
    <tr>
        <th>Accept-Put</th>
        <td>
            <ul>
<#list hints.acceptPut as acceptPut>
                <li>${acceptPut}</li>
</#list>
            </ul>
        </td>
        <td>Available representations of the resource supported for PUT.</td>
    </tr>
    </#if>
    <#if hints.acceptPost?has_content>
    <tr>
        <th>Accept-Post</th>
        <td>
            <ul>
<#list hints.acceptPost as acceptPost>
                <li>${acceptPost}</li>
</#list>
            </ul>
        </td>
        <td>Available representations of the resource supported for POST.</td>
    </tr>
    </#if>
    <#if hints.acceptPatch?has_content>
    <tr>
        <th>Accept-Patch</th>
        <td>
            <ul>
                <#list hints.acceptPatch as acceptPatch>
                    <li>${acceptPatch}</li>
                </#list>
            </ul>
        </td>
        <td>Available representations of the resource supported for PATCH.</td>
    </tr>
    </#if>
    <#if hints.acceptRanges?has_content>
    <tr>
        <th>Accept-Ranges</th>
        <td>
            <ul>
                <#list hints.acceptRanges as range>
                    <li>${range}</li>
                </#list>
            </ul>
        </td>
        <td>Range requests accepted by the server.</td>
    </tr>
    </#if>
    <#if hints.preferences?has_content>
    <tr>
        <th>Prefer</th>
        <td>
            <ul>
                <#list hints.preferences as prefer>
                    <li>${prefer}</li>
                </#list>
            </ul>
        </td>
        <td>Hints the preferences supported by the resource.</td>
    </tr>
    </#if>
    <#if hints.preconditionReq?has_content>
    <tr>
        <th>Precondition-Req</th>
        <td>
            <ul>
            <#list hints.preconditionReq as precondition>
                <li>${precondition}</li>
            </#list>
            </ul>
        </td>
        <td>The required preconditions for PUT, POST or PATCH.</td>
    </tr>
    </#if>
    <#if hints.authReq?has_content>
    <tr>
        <th>Auth-Req</th>
        <td>
            <ul>
                <#list hints.authReq as authReq>
                    <li>Scheme: ${authReq.scheme}<#if authReq.realms?has_content>; Realms: <#list authReq.realms as realm>${realm}<#if realm_has_next>, </#if></#list></#if></li>
                </#list>
            </ul>
        </td>
        <td>The HTTP authentication used by this resource.</td>
    </tr>
    </#if>
    <#if hints.status?has_content>
    <tr>
        <th>Status</th>
        <td>
            ${hints.status}
        </td>
        <td>The status of the resource (ok, deprecated, gone)</td>
    </tr>
    </#if>