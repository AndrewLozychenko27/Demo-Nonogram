<#import "parts/common.ftl" as c/>

<@c.page "Login">
    <div class="w-30 m-auto">
        <@c.formBody "Sign In">
            <#if RequestParameters.error??>
                <div class="alert alert-danger" role="alert">
                    <p class="text-center mb-0">Bad credentials</p>
                </div>
            </#if>
            <@c.form "login">
                <@c.input "Nickname"><#if nickname??>${nickname}</#if></@c.input>
                <@c.password/>
                <@c.submitPair "Log in"/>
                <br/>
                <@c.link "Sign up" "user/create" 'warning w-100'/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>