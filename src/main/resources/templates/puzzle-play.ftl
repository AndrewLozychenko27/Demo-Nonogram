<#import "parts/common.ftl" as c/>

<@c.page "Puzzle " + puzzle.name>
    <div class="w-75 m-auto">
        <@c.field puzzle hints/>
    </div>
</@c.page>