<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>

<@c.page "Create puzzle">
    <div class="w-35 m-auto">
        <@f.formBody "New puzzle">
            <@f.form "puzzle/create" "POST" "multipart/form-data">
                <@f.bind "puzzle.name"/>
                <@f.inputValid "Name" "name"><#if puzzle?? && puzzle.name??>${puzzle.name}</#if></@f.inputValid>
                <@f.bind "puzzle.width"/>
                <@f.inputValid "Width" "width"><#if puzzle?? && puzzle.width??>${puzzle.width}</#if></@f.inputValid>
                <@f.bind "puzzle.height"/>
                <@f.inputValid "Height" "height"><#if puzzle?? && puzzle.height??>${puzzle.height}</#if></@f.inputValid>
                <div class="mb-3 row">
                    <div class="col-sm-4 my-auto">
                        <label>Fill mode</label>
                    </div>
                    <div class="col-sm-8">
                        <div class="row">
                            <div class="col-sm-4">
                                <input type="radio" class="btn-check" name="fill" id="manuallyOption" value="manually"
                                       onclick="turnOffImage()" checked>
                                <label class="btn btn-outline-success" for="manuallyOption">Manually</label>
                            </div>
                            <div class="col-sm-4">
                                <input type="radio" class="btn-check" name="fill" id="imageOption" value="image"
                                       onclick="turnOnImage()">
                                <label class="btn btn-outline-success" for="imageOption">Image</label>
                            </div>
                            <div class="col-sm-4 d-flex flex-row-reverse">
                                <input type="radio" class="btn-check" name="fill" id="randomOption" value="random"
                                       onclick="turnOffImage()">
                                <label class="btn btn-outline-success" for="randomOption">Random</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col">
                        <input class="form-control" type="file" id="image" name="image" accept="image/*" disabled>
                    </div>
                </div>
                <@f.submitPair "Create" "puzzle/list"/>
            </@f.form>
        </@f.formBody>
    </div>
    <script>
        function turnOnImage() {
            document.getElementById("image").disabled = false;
        }

        function turnOffImage() {
            document.getElementById("image").disabled = true;
        }
    </script>
</@c.page>