<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>
<#import "parts/util.ftl" as u/>

<@c.page "Login">
    <div class="w-30 m-auto">
        <@f.formBody "Sign In">
            <#if RequestParameters.error??>
                <div class="alert alert-danger" role="alert">
                    <p class="text-center mb-0">Bad credentials</p>
                </div>
            </#if>
            <@f.form "login">
                <@f.input "Nickname"><#if nickname??>${nickname}</#if></@f.input>
                <@f.password/>
                <@f.submitPair "Log in"/>
                <br/>
                <@u.link "Sign up" "user/create" 'warning w-100'/>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>