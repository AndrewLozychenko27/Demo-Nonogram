<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>

<@c.page "Create puzzle">
    <div class="w-30 m-auto">
        <@f.formBody "New puzzle">
            <@f.form "puzzle/create">
                <@f.bind "puzzle.name"/>
                <@f.inputValid "Name" "name"><#if puzzle?? && puzzle.name??>${puzzle.name}</#if></@f.inputValid>
                <@f.bind "puzzle.width"/>
                <@f.inputValid "Width" "width"><#if puzzle?? && puzzle.width??>${puzzle.width}</#if></@f.inputValid>
                <@f.bind "puzzle.height"/>
                <@f.inputValid "Height" "height"><#if puzzle?? && puzzle.height??>${puzzle.height}</#if></@f.inputValid>
                <@f.submitPair "Create" "puzzle/list"/>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>