<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>
<#import "parts/util.ftl" as u/>

<@c.page "Login">
    <div class="w-30 m-auto">
        <@f.formBody "Log in">
            <#if RequestParameters?? && RequestParameters.error??>
                <div class="alert alert-danger" role="alert">
                    <p class="text-center mb-0">Bad credentials</p>
                </div>
            </#if>
            <@f.form "login">
                <@f.input "Nickname"><#if nickname??>${nickname}</#if></@f.input>
                <@f.password/>
                <div class="my-4 pt-4">
                    <@f.submit "Log in" "primary w-100"/>
                </div>
                <hr/>
                <div class="mt-4">
                    <@u.link "Create account" "user/create" 'primary w-100'/>
                    <div class="w-100">
                        <div class="row my-3">
                            <h5 class="text-center">or sign in with</h5>
                        </div>
                        <div class="row">
                            <div class="d-flex justify-content-center">
                                <@u.google/>
                            </div>
                        </div>
                    </div>
                </div>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>