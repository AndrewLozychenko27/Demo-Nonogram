<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>
<#import "parts/util.ftl" as u/>

<@c.page "Profile">
    <div class="w-30 m-auto">
        <@f.formBody "Profile">
            <@f.label "Email" user.email/>
            <@f.label "Nickname" user.nickname/>
            <div class="row my-3">
                <div class="row m-auto">
                    <@u.link "Edit" "user/" + user.id + "/edit" "warning"/>
                </div>
            </div>
            <div class="row my-3">
                <div class="row col-sm-6 m-auto">
                    <@u.link "Delete" "user/" + user.id + "/delete" "danger"/>
                </div>
                <div class="row col-sm-6 m-auto">
                    <@u.link "Reset password" "user/" + user.id + "/reset-password" "warning"/>
                </div>
            </div>
        </@f.formBody>
    </div>
</@c.page>