<#macro field puzzle keys="empty" suggestions=[] isSolved=false>
    <#if keys == "empty">
        <#assign  showKeys=false/>
    <#else>
        <#assign  showKeys=true/>
    </#if>

    <#if suggestions?size == 0>
        <#assign  isPlaying=false/>
    <#else>
        <#assign  isPlaying=true/>
    </#if>

    <div class="d-flex justify-content-center">
        <table class="table table-bordered border-secondary field">
            <thead>
            <tr>
                <th scope="col" class="table-active p-0"></th>
                <#list 0..<puzzle.width as x>
                    <th scope="col"
                        class="table-active p-0 <#if !showKeys>table-cell</#if> <#if x !=0 && (x + 1) % 5 == 0>b-r</#if> b-b">
                        <#if showKeys>
                            <#list keys.horizontal as col, sequences>
                                <#if col == x>
                                    <#list sequences as s>
                                        <p class="text-center mb-0">${s}</p>
                                    </#list>
                                </#if>
                            </#list>
                        </#if>
                    </th>
                </#list>
            </tr>
            </thead>
            <tbody>
            <#list 0..<puzzle.height as y>
                <tr>
                    <th scope="row"
                        class="table-active p-0 <#if !showKeys>table-cell</#if> <#if y !=0 && (y + 1) % 5 == 0>b-b</#if> b-r align-middle">
                        <p class="text-end mb-0 mx-2">
                            <#if showKeys>
                                <#list keys.vertical as row, sequences>
                                    <#if row == y>
                                        <#list sequences as s>
                                            ${s}
                                            <#if s_has_next> </#if>
                                        </#list>
                                    </#if>
                                </#list>
                            </#if>
                        </p>
                    </th>
                    <#list 0..<puzzle.width as x>
                        <#assign cellStyle="cell"/>
                        <td class="table-cell p-0 <#if x !=0 && (x + 1) % 5 == 0>b-r</#if> <#if y !=0 && (y + 1) % 5 == 0>b-b</#if>">
                            <input type="checkbox" id="${y}:${x}" name="cell" value="${y}:${x}"
                                    <#if isPlaying??>
                                        <#if suggestions?seq_contains(y + ":" + x) || suggestions?seq_contains(y + ":" + x + " hint")>
                                            checked
                                        </#if>
                                        <#if isSolved>
                                            disabled
                                        </#if>
                                        <#if suggestions?seq_contains(y + ":" + x + " hint")>
                                            disabled
                                            <#assign cellStyle="hint"/>
                                        </#if>
                                    </#if>
                                   class=${cellStyle}
                            >
                        </td>
                    </#list>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>