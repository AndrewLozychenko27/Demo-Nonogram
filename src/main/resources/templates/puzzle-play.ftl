<#import "parts/common.ftl" as c/>
<#import "parts/puzzle.ftl" as pz/>
<#import "parts/util.ftl" as u/>
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
        <#if hasGame>
            <@pz.field puzzle keys game.getCellsAsStrings() game.state == "SOLVED"/>
        <#else>
            <@pz.field puzzle keys />
        </#if>
        <#if hasGame && game.state == "SOLVED">
            <div class="w-13 m-auto mt-3">
                <div class="row">
                    <div class="col-sm-6">
                        <p class="text-center">Hints: ${game.getHintsCount()}%</p>
                    </div>
                    <div class="col-sm-6">
                        <p class="text-center">Attempts: ${game.attempts}</p>
                    </div>
                </div>
                <p class="text-success"><b>Congrats! You've solved it!</b></p>
                <div class="row">
                    <input type="button" class="btn btn-outline-primary" value="Leave" onclick="leave()">
                </div>
            </div>
        <#else>
            <div class="w-25 m-auto mt-3">
                <div class="row">
                    <div class="col-sm-6">
                        <#if hasGame>
                            <p class="text-center">Hints: ${game.getHintsCount()}%</p>
                        <#else>
                            <p class="text-center">Hints: 0</p>
                        </#if>
                    </div>
                    <div class="col-sm-6">
                        <#if hasGame>
                            <p class="text-center">Attempts: ${game.attempts}</p>
                        <#else>
                            <p class="text-center">Attempts: 0</p>
                        </#if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6 d-flex flex-row-reverse">
                        <input type="button" class="btn btn-outline-warning w-100" value="Hint" onclick="hint()">
                    </div>
                    <div class="col-sm-6">
                        <input type="button" class="btn btn-outline-success w-100" value="Check" onclick="check()">
                    </div>
                </div>
                <div class="row mt-3">
                    <div class="col">
                        <input type="button" class="btn btn-outline-danger w-100" value="Leave" onclick="leave()">
                    </div>
                </div>
            </div>
        </#if>
    </div>
    <script>
        function getChecked() {
            let cells = document.getElementsByName("cell");
            let checked = [];

            for (let i in cells) {
                if (cells[i].checked === true) {
                    checked.push(cells[i].value);
                }
            }

            return checked;
        }

        function check() {
            doPost('<@u.path "api/game/" + puzzle.id  + "/check"/>', {
                cells: getChecked()
            })
                .then(() => {
                    window.location.reload();
                });
        }

        function leave() {
            doPost('<@u.path "api/game/" + puzzle.id + "/save-state"/>', {
                cells: getChecked()
            })
                .then(() => {
                    window.location.href = '<@u.path "puzzle/list"/>';
                });
        }

        function hint() {
            doPost('<@u.path "api/game/" + puzzle.id + "/hint"/>', {
                cells: getChecked()
            })
                .then(() => {
                    window.location.reload();
                });
        }
    </script>
</@c.page>