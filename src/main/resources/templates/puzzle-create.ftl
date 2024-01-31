<#import "parts/common.ftl" as c/>

<@c.page "Create puzzle">
    <div class="w-30 m-auto">
        <@c.formBody "New puzzle">
            <@c.form "puzzle/create">
                <@c.bind "puzzle.name"/>
                <@c.inputValid "Name" "name"><#if puzzle?? && puzzle.name??>${puzzle.name}</#if></@c.inputValid>
                <@c.bind "puzzle.width"/>
                <@c.inputValid "Width" "width"><#if puzzle?? && puzzle.width??>${puzzle.width}</#if></@c.inputValid>
                <@c.bind "puzzle.height"/>
                <@c.inputValid "Height" "height"><#if puzzle?? && puzzle.height??>${puzzle.height}</#if></@c.inputValid>
                <@c.submitPair "Create" "puzzle/list"/>
            </@c.form>
        </@c.formBody>
    </div>
</@c.page>