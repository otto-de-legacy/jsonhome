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
    <tr>
        <th>Status</th>
        <td>
            ${hints.status}
        </td>
        <td>The status of the resource (ok, deprecated, gone)</td>
    </tr>
