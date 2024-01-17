<#import "util.ftl" as u />

<#macro input name label isPassword=false >
    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="${isPassword?then('password', 'text')}" class="form-control" name="${name}">
        </div>
    </div>
</#macro>

<#macro submit label >
    <input type="submit" class="btn btn-outline-success" value="${label}">
</#macro>

<#macro form label action method >
    <div class="m-3 p-3 border border-success-subtle bg-success bg-opacity-10 border-start-0 border-end-0">
        <div class="row">
            <h3 class="text-center my-3">Sign In</h3>
        </div>
        <div class="my-3">
            <form action="<@u.path action />" method="${method}">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <#nested>
            </form>
        </div>
    </div>
</#macro>