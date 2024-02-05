<#import "parts/common.ftl" as c/>
<#import "parts/util.ftl" as u/>
<#include "parts/security.ftl"/>

<@c.page "Puzzles">
    <div class="w-75 m-auto">
        <div class="my-3">
            <a class="btn btn-outline-primary" role="button" href="<@u.path "puzzle/create"/>">Create puzzle</a>
        </div>
        <div class="row row-cols-1 row-cols-md-3 g-4">
            <#list puzzles.content as puzzle>
                <div class="col">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title mb-3">${puzzle.name}</h5>
                            <p class="card-text"><b>Author: ${puzzle.user.nickname}</b></p>
                            <p class="card-text">Size: ${puzzle.width} x ${puzzle.height}</p>
                            <#if puzzle.isEmpty()>
                                <#if puzzle.user.id == currentUser.id>
                                    <p class="card-text">Your puzzle is empty, you need to fill it</p>
                                    <a class="btn btn-outline-primary" role="button"
                                       href="<@u.path "puzzle/" + puzzle.id + "/fill"/>">Fill</a>
                                <#else>
                                    <p class="card-text">Currently, it's empty</p>
                                </#if>
                            <#else>
                                <p class="card-text">Cells: ${puzzle.cells?size}</p>
                                <a class="btn btn-outline-primary" role="button"
                                   href="<@u.path "puzzle/" + puzzle.id + "/play"/>">Play</a>
                            </#if>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </div>
</@c.page>