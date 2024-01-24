<#import "parts/common.ftl" as c/>

<@c.page "Profile">
    <div class="w-30 m-auto">
        <@c.formBody "Profile">
            <@c.field "Email" user.email/>
            <@c.field "Nickname" user.nickname/>
            <div>
                <@c.link "Edit" "user/edit" "warning"/>
            </div>
        </@c.formBody>
    </div>
</@c.page>