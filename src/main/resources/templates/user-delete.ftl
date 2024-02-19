<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>

<@c.page "Delete profile">
    <div class="w-30 m-auto">
        <@f.formBody "Delete profile">
            <@f.form "user/" + user.id + "/delete">
                <h5>To confirm your account deletion you need to enter your password</h5>
                <#if error??>
                    <div class="alert alert-danger" role="alert">
                        <p class="text-center mb-0"><b>${error}</b></p>
                    </div>
                </#if>
                <@f.password/>
                <@f.submitPair "Delete" "user/" + user.id + "/profile"/>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>