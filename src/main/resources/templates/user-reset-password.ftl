<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>

<@c.page "Reset password">
    <div class="w-30 m-auto">
        <@f.formBody "Reset password">
            <@f.form "user/" + user.id + "/reset-password">
                <@f.hidden "id" user.id/>
                <@f.bind "user.password"/>
                <@f.passwordValid "Password" "password"><#if changes??>${changes.password}</#if></@f.passwordValid>
                <@f.bind "user.passwordConfirmation"/>
                <@f.passwordValid "Password Confirmation" "passwordConfirmation"><#if changes??>${changes.passwordConfirmation}</#if></@f.passwordValid>
                <@f.submitPair "Reset" "user/" + user.id + "/profile"/>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>