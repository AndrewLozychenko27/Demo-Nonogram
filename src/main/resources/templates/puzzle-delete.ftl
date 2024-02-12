<#import "parts/common.ftl" as c/>

<@c.page "Delete " + puzzle.name "w-30">
    <@c.formBody "Delete " + puzzle.name>
        <@c.form "puzzle/" + puzzle.id + "/delete">
            <div class="mb-3">
                <p class="text-center">Enter your puzzle name <b>${puzzle.name}</b> to confirm that you want to delete it.</p>
            </div>
            <#if error??>
                <div class="mb-3">
                    <p class="text-center text-danger">${error}</p>
                </div>
            </#if>
            <@c.input "Confirmation"/>
            <@c.submitPair "Delete" "puzzle/list"/>
        </@c.form>
    </@c.formBody>
</@c.page>