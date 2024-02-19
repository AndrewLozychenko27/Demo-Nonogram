<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>

<@c.page "Delete " + puzzle.name "w-30">
    <@f.formBody "Delete " + puzzle.name>
        <@f.form "puzzle/" + puzzle.id + "/delete">
            <div class="mb-3">
                <p class="text-center">Enter your puzzle name <b>${puzzle.name}</b> to confirm that you want to delete it.</p>
            </div>
            <#if error??>
                <div class="mb-3">
                    <p class="text-center text-danger">${error}</p>
                </div>
            </#if>
            <@f.input "Confirmation"/>
            <@f.submitPair "Delete" "puzzle/list"/>
        </@f.form>
    </@f.formBody>
</@c.page>