<#import "macros/common.ftl" as c />
<#import "macros/parts.ftl" as p />

<@c.page "Login">
    <#if error??>error</#if>
    <div class="w-30 m-auto">
        <@p.form "Sign In" "login" "POST" >
            <@p.input "nickname" "Nickname" />
            <@p.input "password" "Password" true />
            <@p.submit "Accept" />
        </@p.form>
    </div>
</@c.page>