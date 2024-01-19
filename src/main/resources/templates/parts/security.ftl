<#if Session?? && Session.SPRING_SECURITY_CONTEXT??>
    <#assign
    currentUser = Session.SPRING_SECURITY_CONTEXT.authentication.principal />
</#if>