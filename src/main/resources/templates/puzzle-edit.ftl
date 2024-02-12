<#import "parts/common.ftl" as c/>

<@c.page "Puzzle Edit">
    <div class="w-35 m-auto">
        <@c.formBody "Puzzle Edit">
            <@c.form "puzzle/" + puzzle.id + "/edit">
                <@c.hidden "id" puzzle.id/>
                <@c.bind "puzzle.name"/>
                <@c.inputValid "Name" "name"><#if changes??>${changes.name}<#else>${puzzle.name}</#if></@c.inputValid>
                <@c.submitPair "Save" "puzzle/list"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>