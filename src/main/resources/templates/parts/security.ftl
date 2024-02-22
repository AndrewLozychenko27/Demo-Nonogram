<#if SPRING_SECURITY_CONTEXT?? && SPRING_SECURITY_CONTEXT.authentication.principal.id??>
    <#assign
    currentUser = SPRING_SECURITY_CONTEXT.authentication.principal />
<#elseif oauthUser??>
    <#assign
    currentUser = oauthUser/>
</#if>