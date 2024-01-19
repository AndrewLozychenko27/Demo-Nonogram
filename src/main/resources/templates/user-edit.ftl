<#import "parts/common.ftl" as c />

<@c.page "Edit profile" >
    <div class="w-30 m-auto">
        <@c.formBody "Edit" >
            <@c.form "user/edit" >
                <@c.hidden "source.email" user.email />
                <@c.hidden "source.nickname" user.nickname />
                <@c.input "Email" "changes.email" >${user.email}</@c.input>
                <@c.input "Nickname" "changes.nickname" >${user.nickname}</@c.input>
                <@c.submitPair "Save" "user/profile" />
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>