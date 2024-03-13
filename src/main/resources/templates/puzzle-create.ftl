<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>

<@c.page "Create puzzle">
    <div class="w-30 m-auto">
        <@f.formBody "New puzzle">
            <@f.form "puzzle/create">
                <@f.bind "puzzle.name"/>
                <@f.inputValid "Name" "name"><#if puzzle?? && puzzle.name??>${puzzle.name}</#if></@f.inputValid>
                <@f.bind "puzzle.width"/>
                <@f.inputValid "Width" "width"><#if puzzle?? && puzzle.width??>${puzzle.width}</#if></@f.inputValid>
                <@f.bind "puzzle.height"/>
                <@f.inputValid "Height" "height"><#if puzzle?? && puzzle.height??>${puzzle.height}</#if></@f.inputValid>
                <div class="mb-3 row">
                    <div class="col-sm-4">
                        <label>Fill mode</label>
                    </div>
                    <div class="col-sm-8">
                        <div class="row">
                            <div class="col-sm-6">
                                <input type="radio" class="btn-check" name="fill" id="manually" value="manually" checked>
                                <label class="btn btn-outline-success" for="manually">Manually</label>
                            </div>
                            <div class="col-sm-6 d-flex flex-row-reverse">
                                <input type="radio" class="btn-check" name="fill" id="generate" value="generate">
                                <label class="btn btn-outline-success" for="generate">Generate</label>
                            </div>
                        </div>
                    </div>
                </div>
                <@f.submitPair "Create" "puzzle/list"/>
            </@f.form>
        </@f.formBody>
    </div>
</@c.page>