<#import "parts/common.ftl" as c/>
<#import "parts/util.ftl" as u/>
<#include "parts/security.ftl"/>

<@c.page "Puzzles">
    <div class="w-75 m-auto">
        <div class="w-30 mx-auto mb-4 d-flex justify-content-center">
            <@c.pager "puzzle/list" puzzles sizes/>
        </div>
        <div class="row row-cols-1 row-cols-md-3 g-4 my-3">
            <div class="col">
                <div class="card h-100 d-flex align-items-center justify-content-center border-none">
                    <div class="row m-3">
                        <a class="btn btn-outline-success p-3" role="button" href="<@u.path "puzzle/create"/>">Create
                            puzzle</a>
                    </div>
                </div>
            </div>
            <#list puzzles.content as puzzle>
                <#assign
                optional = puzzle.getGame(currentUser)
                hasGame = optional.isPresent()/>
                <#if hasGame>
                    <#assign  game = optional.get()/>
                </#if>

                <#if puzzle.isEmpty()>
                    <#assign cardBorder = "danger"/>
                <#elseif puzzle.user.id == currentUser.id>
                    <#assign cardBorder = "primary"/>
                <#else>
                    <#if hasGame>
                        <#if game.state == "IN_PROGRESS">
                            <#assign cardBorder = "warning"/>
                        <#elseif game.state == "SOLVED">
                            <#assign cardBorder = "success"/>
                        </#if>
                    <#else>
                        <#assign cardBorder = "secondary"/>
                    </#if>
                </#if>

                <div class="col">
                    <div class="card h-100 border-${cardBorder}">
                        <div class="card-header bg-transparent border-${cardBorder} my-2">
                            <h5 class="card-title mb-0"><b>${puzzle.name}</b></h5>
                        </div>
                        <div class="card-body bg-transparent border-${cardBorder} card-height">
                            <p class="card-text"><b>Author: ${puzzle.user.nickname}</b></p>
                            <p class="card-text">Size: ${puzzle.width} x ${puzzle.height}</p>
                            <#if !puzzle.isEmpty()>
                                <p class="card-text">Cells: ${puzzle.cells?size}</p>
                                <#if hasGame>
                                    <p class="card-text">Attempts: ${game.attempts}</p>
                                    <p class="card-text">Hints: ${game.hints}</p>
                                </#if>
                            </#if>
                        </div>
                        <div class="card-footer bg-transparent border-${cardBorder}">
                            <#if puzzle.isEmpty()>
                                <#if puzzle.user.id == currentUser.id>
                                    <div class="row m-2 align-bottom">
                                        <button class="btn btn-outline-danger mb-2" disabled>
                                            Empty, you need to fill it
                                        </button>
                                        <a class="btn btn-outline-danger" role="button"
                                           href="<@u.path "puzzle/" + puzzle.id + "/fill"/>">Fill</a>
                                    </div>
                                </#if>
                            <#elseif puzzle.user.id == currentUser.id>
                                <div class="row m-2">
                                    <a class="btn btn-outline-primary mb-2" role="button"
                                       href="<@u.path "puzzle/" + puzzle.id + "/edit"/>">Edit</a>
                                    <a class="btn btn-outline-danger" role="button"
                                       href="<@u.path "puzzle/" + puzzle.id + "/delete"/>">Delete</a>
                                </div>
                            <#else>
                                <div class="row m-2">
                                    <#if hasGame>
                                        <#if game.state == "IN_PROGRESS">
                                            <button class="btn btn-outline-warning" disabled>In progress</button>
                                        <#else>
                                            <button class="btn btn-outline-success" disabled>Solved</button>
                                        </#if>
                                    <#else>
                                        <button class="btn btn-outline-secondary" disabled>No attempts</button>
                                    </#if>
                                </div>
                                <div class="row m-2">
                                    <a class="btn btn-outline-success" role="button"
                                       href="<@u.path "puzzle/" + puzzle.id + "/play"/>">Play</a>
                                </div>
                            </#if>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
        <div class="w-30 mx-auto my-4 pt-4 d-flex justify-content-center">
            <@c.pager "puzzle/list" puzzles sizes/>
        </div>
    </div>
</@c.page>