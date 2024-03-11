<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>

<@c.page "Edit profile">
    <div class="w-30 m-auto">
        <@f.formBody "Edit">
            <@f.form "user/" + user.id + "/edit">
                <@f.hidden "id" user.id/>
                <@f.label "Email" user.email/>
                <@f.bind "user.nickname"/>
                <@f.inputValid "Nickname" "nickname"><#if changes??>${changes.nickname}<#else>${user.nickname}</#if></@f.inputValid>
                <@f.submitPair "Save" "user/" + user.id + "/profile"/>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>