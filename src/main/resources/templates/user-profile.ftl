<#import "parts/common.ftl" as c />

<@c.page "Profile" >
    <div class="w-30 m-auto">
        <@c.field "Email" user.email />
        <@c.field "Nickname" user.nickname />
        <@c.field "Role" user.role.name() />
        <div>
            <@c.link "Edit" "user/edit" "warning" />
        </div>
    </div>
</@c.page>