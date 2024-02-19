<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>
<#import "/spring.ftl" as s/>

<@c.page "Registration">
    <div class="w-30 m-auto">
        <@f.formBody "Registration" >
            <@f.form "user/create">
                <@f.bind "user.email"/>
                <@f.inputValid "Email" "email"><#if user?? && user.email??>${user.email}</#if></@f.inputValid>
                <@f.bind "user.nickname"/>
                <@f.inputValid "Nickname" "nickname"><#if user?? && user.nickname??>${user.nickname}</#if></@f.inputValid>
                <@f.bind "user.password"/>
                <@f.passwordValid><#if user?? && user.password??>${user.password}</#if></@f.passwordValid>
                <@f.bind "user.passwordConfirmation"/>
                <@f.passwordValid "Password confirmation" "passwordConfirmation"><#if user?? && user.passwordConfirmation??>${user.passwordConfirmation}</#if></@f.passwordValid>
                <@f.submitPair "Create" "login"/>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>