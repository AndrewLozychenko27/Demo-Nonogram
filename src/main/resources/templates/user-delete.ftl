<#import "parts/common.ftl" as c/>

<@c.page "Delete profile">
    <div class="w-30 m-auto">
        <@c.formBody "Delete profile">
            <@c.form "user/" + user.id + "/delete">
                <h5>To confirm your account deletion you need to enter your password</h5>
                <#if error??>
                    <div class="alert alert-danger" role="alert">
                        <p class="text-center mb-0"><b>${error}</b></p>
                    </div>
                </#if>
                <@c.password/>
                <@c.submitPair "Delete" "user/" + user.id + "/profile"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>