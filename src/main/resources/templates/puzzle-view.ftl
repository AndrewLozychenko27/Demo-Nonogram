<#import "parts/common.ftl" as c/>
<#import "parts/puzzle.ftl" as pz/>

<@c.page "Puzzle " + puzzle.name>
    <div class="m-auto">
        <div class="row my-3">
            <h5 class="text-center"><b>${puzzle.name}</b></h5>
        </div>
        <@pz.field puzzle keys/>
        <div class="w-25 m-auto">
            <div class="row">
                <div class="col-sm-10">Played:</div>
                <div class="col-sm-2">${puzzleStats.played}</div>
            </div>
            <div class="row">
                <div class="col-sm-10">Solved:</div>
                <div class="col-sm-2">${puzzleStats.solved}</div>
            </div>
            <div class="row">
                <div class="col-sm-10">Best attempts count:</div>
                <div class="col-sm-2">${puzzleStats.bestAttempts}</div>
            </div>
            <div class="row">
                <div class="col-sm-10">Best hints count:</div>
                <div class="col-sm-2">${puzzleStats.bestHints}</div>
            </div>
        </div>
    </div>
</@c.page>