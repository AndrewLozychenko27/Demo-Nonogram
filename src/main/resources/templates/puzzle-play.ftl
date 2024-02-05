<#import "parts/common.ftl" as c/>

<@c.page "Puzzle " + puzzle.name>
    <div class="w-75 m-auto">
        <@c.form "puzzle/" + puzzle.id + "/check">
            <#if game.isPresent()>
                <@c.field puzzle hints game.get().getCellsAsStrings()/>
            <#else>
                <@c.field puzzle hints />
            </#if>
            <@c.submitPair "Check" "puzzle/list"/>
        </@c.form>
        <#if game.cells??>da</#if>
    </div>
</@c.page>