<#import "parts/common.ftl" as c/>

<@c.page "Fill your puzzle">
    <div class="w-75 m-auto">
        <@c.formBody "Fill your puzzle">
            <@c.form "puzzle/fill/" + puzzle.id>
                <@c.field puzzle/>
                <@c.submitPair "Save" "puzzle/list/" + puzzle.id/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>