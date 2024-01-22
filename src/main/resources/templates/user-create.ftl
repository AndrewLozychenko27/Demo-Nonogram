<#import "parts/common.ftl" as c />
<#import "/spring.ftl" as s />

<@c.page "Registration" >
    <div class="w-30 m-auto">
        <@c.formBody "Registration"  >
            <@c.form "user/create" >
                <@c.inputValid "Email" "user.email" />
                <@c.inputValid "Nickname" "user.nickname" />
                <@c.passwordValid />
                <@c.passwordValid "Password confirmation" "user.passwordConfirmation" />
                <@c.submitPair "Create" "login" />
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>