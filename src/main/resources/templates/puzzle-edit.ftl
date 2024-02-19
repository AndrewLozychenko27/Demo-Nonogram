<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>

<@c.page "Puzzle Edit">
    <div class="w-35 m-auto">
        <@f.formBody "Puzzle Edit">
            <@f.form "puzzle/" + puzzle.id + "/edit">
                <@f.hidden "id" puzzle.id/>
                <@f.bind "puzzle.name"/>
                <@f.inputValid "Name" "name"><#if changes??>${changes.name}<#else>${puzzle.name}</#if></@f.inputValid>
                <@f.submitPair "Save" "puzzle/list"/>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>