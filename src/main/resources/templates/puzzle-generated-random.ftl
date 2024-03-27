<#import "parts/common.ftl" as c/>
<#import "parts/puzzle.ftl" as p/>
<#import "parts/form.ftl" as f/>
<#import "parts/util.ftl" as u/>

<@c.page "Generated">
    <div class="w-75 m-auto">
        <@f.form "puzzle/save/random">
            <@f.hidden "name" generatedPuzzle.name/>
            <@f.hidden "width" generatedPuzzle.width/>
            <@f.hidden "height" generatedPuzzle.height/>
            <div class="mt-3">
                <@p.preview generatedPuzzle/>
            </div>
            <div class="w-13 mx-auto">
                <@f.submitPair "Save" "puzzle/list"/>
            </div>
        </@f.form>
        <div class="w-13 mx-auto">
            <button class="btn btn-outline-warning" onclick="regenerate()">Regenerate</button>
        </div>
    </div>
    <script>
        function regenerate() {
            doPost('<@u.path "puzzle/create"/>', {
                name: document.getElementsByName("name").value,
                width: document.getElementsByName("width").value,
                height: document.getElementsByName("height").value,
                fill: "random"
            })
                .then(() => {
                    window.location.reload();
                });
        }
    </script>
</@c.page>