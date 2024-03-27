<#import "parts/common.ftl" as c/>
<#import "parts/puzzle.ftl" as p/>
<#import "parts/form.ftl" as f/>

<@c.page "Generated">
    <#if generatedPuzzles[0].width lte 15>
        <#assign cols = 4/>
    <#elseif generatedPuzzles[0].width lte 25>
        <#assign cols = 3/>
    <#else>
        <#assign cols = 2/>
    </#if>

    <div class="w-90 m-auto">
        <@f.form "puzzle/save">
            <div class="row row-cols-1 row-cols-md-${cols} g-4 my-3">
                <#list generatedPuzzles as puzzle>
                    <div class="col">
                        <div class="card h-100 d-flex align-items-center justify-content-center border-none"
                             id="card-${puzzle?index}">
                            <div class="mt-3">
                                <@p.preview puzzle/>
                            </div>
                            <div class="card-body">
                                <input type="radio" class="btn-check" id="${puzzle?index}" name="puzzleId"
                                       value="${puzzle?index}" autocomplete="off">
                                <label class="btn btn-outline-success" for="${puzzle?index}"
                                       onclick="select(${puzzle?index})">Select</label>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
            <div class="w-13 mx-auto">
                <@f.submitPair "Save" "puzzle/list"/>
            </div>
        </@f.form>
    </div>
    <script>
        function select(id) {
            document.querySelectorAll("[id^='card-']").forEach(card => {
                if (card.id === "card-" + id) {
                    card.classList.remove("border-none");
                    card.classList.add("border-success");
                } else {
                    card.classList.remove("border-success");
                    card.classList.add("border-none");
                }
                console.log(card.classList);
            });
        }
    </script>
</@c.page>