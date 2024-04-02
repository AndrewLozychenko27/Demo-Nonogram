<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>
<#import "parts/puzzle.ftl" as pz/>

<@c.page "Fill " + emptyPuzzle.name "w-84">
    <div class="m-auto">
        <div class="row my-3">
            <h5 class="text-center">Fill <b>${emptyPuzzle.name}</b></h5>
        </div>
        <@f.form "puzzle/fill">
            <@pz.field emptyPuzzle/>
            <div class="w-13 m-auto mt-3">
                <#if error??>
                    <p class="text-danger text-center"><b>${error}</b></p>
                </#if>
                <@f.submitPair "Save" "puzzle/list"/>
            </div>
        </@f.form>
    </div>
</@c.page>