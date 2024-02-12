<#import "parts/common.ftl" as c/>

<@c.page "Profile">
    <div class="w-30 m-auto">
        <@c.formBody "Profile">
            <@c.label "Email" user.email/>
            <@c.label "Nickname" user.nickname/>
            <div class="row my-3">
                <div class="row m-auto">
                    <@c.link "Edit" "user/" + user.id + "/edit" "warning"/>
                </div>
            </div>
            <div class="row my-3">
                <div class="row col-sm-6 m-auto">
                    <@c.link "Delete" "user/" + user.id + "/delete" "danger"/>
                </div>
                <div class="row col-sm-6 m-auto">
                    <@c.link "Reset password" "user/" + user.id + "/reset-password" "warning"/>
                </div>
            </div>
        </@c.formBody>
    </div>
</@c.page>