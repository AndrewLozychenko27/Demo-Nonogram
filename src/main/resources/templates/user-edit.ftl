<#import "parts/common.ftl" as c/>

<@c.page "Edit profile">
    <div class="w-30 m-auto">
        <@c.formBody "Edit">
            <@c.form "user/" + user.id + "/edit">
                <@c.hidden "id" user.id/>
                <@c.bind "user.email"/>
                <@c.inputValid "Email" "email"><#if changes??>${changes.email}<#else>${user.email}</#if></@c.inputValid>
                <@c.bind "user.nickname"/>
                <@c.inputValid "Nickname" "nickname"><#if changes??>${changes.nickname}<#else>${user.nickname}</#if></@c.inputValid>
                <@c.submitPair "Save" "user/" + user.id + "/profile"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>