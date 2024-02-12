<#import "parts/common.ftl" as c/>

<@c.page "Reset password">
    <div class="w-30 m-auto">
        <@c.formBody "Reset password">
            <@c.form "user/" + user.id + "/reset-password">
                <@c.hidden "id" user.id/>
                <@c.bind "user.password"/>
                <@c.passwordValid "Password" "password"><#if changes??>${changes.password}</#if></@c.passwordValid>
                <@c.bind "user.passwordConfirmation"/>
                <@c.passwordValid "Password Confirmation" "passwordConfirmation"><#if changes??>${changes.passwordConfirmation}</#if></@c.passwordValid>
                <@c.submitPair "Reset" "user/" + user.id + "/profile"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>