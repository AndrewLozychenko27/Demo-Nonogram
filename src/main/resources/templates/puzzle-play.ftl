<#import "parts/common.ftl" as c/>
<#include "parts/security.ftl"/>

<@c.page "Puzzle " + puzzle.name "w-84">
    <#assign
    optional = puzzle.getGame(currentUser)/>
    <#if optional.isPresent()>
        <#assign
        hasGame = true
        game = optional.get()/>
    <#else>
        <#assign
        hasGame = false
        game = ""/>
    </#if>

    <div class="m-auto">
        <div class="row my-3">
            <h5 class="text-center"><b>${puzzle.name}</b></h5>
        </div>
        <@c.form "puzzle/" + puzzle.id + "/check">
            <#if hasGame>
                <@c.field puzzle hints game.getCellsAsStrings()/>
            <#else>
                <@c.field puzzle hints />
            </#if>
            <div class="w-13 m-auto mt-3">
                <#if hasGame>
                    <p class="text-center">Attempts: ${game.attempts}</p>
                <#else>
                    <p class="text-center">Attempts: 0</p>
                </#if>
                <#if hasGame && game.state == "SOLVED">
                    <p class="text-success"><b>Congrats! You've solved it!</b></p>
                    <div class="row">
                        <@c.link "Leave" "puzzle/list" "primary"/>
                    </div>
                <#else>
                    <@c.submitPair "Check" "puzzle/list" "Leave"/>
                </#if>
            </div>
        </@c.form>
    </div>
</@c.page>