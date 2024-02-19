<#import "util.ftl" as u/>

<#macro label label value>
    <div class="mb-3 row">
        <label class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <span class="form-control">${value}</span>
        </div>
    </div>
</#macro>

<#macro hidden name value>
    <input type="hidden" name="${name}" value="${value}">
</#macro>

<#macro bind path>
    <#assign
    status = springMacroRequestContext.getBindStatus(path)>
</#macro>

<#macro input label name=label?c_lower_case>
    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" name="${name}" value="<#nested>">
        </div>
    </div>
</#macro>

<#macro inputValid label name>
    <#local nestedContent><#nested></#local>

    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="text" name="${name}" value="${nestedContent}"
                   class="form-control <#if nestedContent?has_content || status.isError()>${status.isError()?then('is-invalid', 'is-valid')}</#if>">
            <#if status.isError()>
                <div class="invalid-feedback">
                    <#list status.errorMessages as error>
                        ${error}
                    </#list>
                </div>
            <#else>
                <div class="invalid-feedback">
                    Correct
                </div>
            </#if>
        </div>
    </div>
</#macro>

<#macro password label="Password" name="password">
    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="password" class="form-control" name="${name}">
        </div>
    </div>
</#macro>

<#macro passwordValid label="Password" name="password">
    <#local nestedContent><#nested></#local>

    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="password" name="${name}" value="${nestedContent}"
                   class="form-control <#if nestedContent?has_content || status.isError()>${status.isError()?then('is-invalid', 'is-valid')}</#if>">
            <#if status.isError()>
                <div class="invalid-feedback">
                    <#list status.errorMessages as error>
                        ${error}
                    </#list>
                </div>
            <#else>
                <div class="invalid-feedback">
                    Correct
                </div>
            </#if>
        </div>
    </div>
</#macro>

<#macro submit label style="success">
    <input type="submit" class="btn btn-outline-${style}" value="${label}">
</#macro>

<#macro submitPair label cancel="" cancelLabel="Cancel">
    <div class="mb-3 mt-4 row">
        <div class="col-sm-6">
            <@u.link cancelLabel cancel "btn btn-outline-danger"/>
        </div>
        <div class="col-sm-6 d-flex flex-row-reverse">
            <button type="submit" class="btn btn-outline-success">${label}</button>
        </div>
    </div>
</#macro>

<#macro formBody label>
    <div class="m-3 p-3 border border-success-subtle rounded-3 px-4">
        <div class="row">
            <h3 class="text-center my-3">${label}</h3>
        </div>
        <div class="my-3">
            <#nested/>
        </div>
    </div>
</#macro>

<#macro form action method="POST">
    <form action="<@u.path action/>" method="${method}" class="m-0">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#nested/>
    </form>
</#macro>