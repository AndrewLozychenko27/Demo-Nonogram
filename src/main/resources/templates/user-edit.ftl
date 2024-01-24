<#import "parts/common.ftl" as c/>

<@c.page "Edit profile">
    <div class="w-30 m-auto">
        <@c.formBody "Edit">
            <@c.form "user/edit">
                <@c.bind "userEditForm.changes.email"/>
                <@c.inputValid "Email" "changes.email"><#if changes??>${changes.email}<#else>${user.email}</#if></@c.inputValid>
                <@c.bind "userEditForm.changes.nickname"/>
                <@c.inputValid "Nickname" "changes.nickname"><#if changes??>${changes.nickname}<#else>${user.nickname}</#if></@c.inputValid>
                <@c.submitPair "Save" "user/profile"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>