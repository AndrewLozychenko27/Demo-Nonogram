<#import "parts/common.ftl" as c />

<@c.page "Login">
    <div class="w-30 m-auto">
        <@c.formBody "Sign In" >
            <@c.form "login" >
                <@c.input "Nickname" />
                <@c.password />
                <@c.submitPair "Log in" />
                <@c.link "Sign up" "user/create" 'warning w-100' />
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>