<#import "parts/common.ftl" as c/>
<#import "/spring.ftl" as s/>

<@c.page "Registration">
    <div class="w-30 m-auto">
        <@c.formBody "Registration" >
            <@c.form "user/create">
                <@c.bind "user.email"/>
                <@c.inputValid "Email" "email"><#if user?? && user.email??>${user.email}</#if></@c.inputValid>
                <@c.bind "user.nickname"/>
                <@c.inputValid "Nickname" "nickname"><#if user?? && user.nickname??>${user.nickname}</#if></@c.inputValid>
                <@c.bind "user.password"/>
                <@c.passwordValid><#if user?? && user.password??>${user.password}</#if></@c.passwordValid>
                <@c.bind "user.passwordConfirmation"/>
                <@c.passwordValid "Password confirmation" "passwordConfirmation"><#if user?? && user.passwordConfirmation??>${user.passwordConfirmation}</#if></@c.passwordValid>
                <@c.submitPair "Create" "login"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>