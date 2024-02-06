<#import "parts/common.ftl" as c/>

<@c.page "Fill " + puzzle.name "w-84">
    <div class="m-auto">
        <div class="row my-3">
            <h5 class="text-center">Fill <b>${puzzle.name}</b></h5>
        </div>
        <@c.form "puzzle/" + puzzle.id + "/fill">
            <@c.field puzzle/>
            <div class="w-13 m-auto mt-3">
                <#if error??>
                    <p class="text-danger text-center"><b>${error}</b></p>
                </#if>
                <@c.submitPair "Save" "puzzle/list"/>
            </div>
        </@c.form>
    </div>
</@c.page>