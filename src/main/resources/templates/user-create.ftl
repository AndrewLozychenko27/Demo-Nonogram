<#import "parts/common.ftl" as c />

<@c.page "Registration" >
    <div class="w-30 m-auto">
        <@c.formBody "Registration"  >
            <@c.form "user/create" >
                <@c.input "Email" />
                <@c.input "Nickname" />
                <@c.password />
                <@c.submitPair "Create" "login" />
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>