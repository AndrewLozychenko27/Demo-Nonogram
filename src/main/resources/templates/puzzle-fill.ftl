<#import "parts/common.ftl" as c/>

<@c.page "Fill your puzzle">
    <div class="m-auto">
        <@c.formBody "Fill your puzzle">
            <@c.form "puzzle/" + puzzle.id + "/fill">
                <@c.field puzzle/>
                <@c.submitPair "Save" "puzzle/list"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>