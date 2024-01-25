<#import "parts/common.ftl" as c/>

<@c.page "Reset password">
    <div class="w-30 m-auto">
        <@c.formBody "Reset password">
            <@c.form "user/reset-password">
                <@c.bind "userEditForm.changes.password"/>
                <@c.passwordValid "Password" "changes.password"><#if changes??>${changes.password}</#if></@c.passwordValid>
                <@c.bind "userEditForm.changes.passwordConfirmation"/>
                <@c.passwordValid "Password Confirmation" "changes.passwordConfirmation"><#if changes??>${changes.passwordConfirmation}</#if></@c.passwordValid>
                <@c.submitPair "Reset" "user/profile"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>