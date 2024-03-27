<#include "security.ftl"/>

<#macro field puzzle keys="empty" game="empty">
    <#assign showKeys = keys != "empty"/>
    <#if game == "empty">
        <#assign
        isPlaying = false
        isSolved = false/>
    <#else>
        <#assign
        isPlaying = true
        isSolved = game.getState().equals("SOLVED")/>
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
                            <#list keys.vertical as col, sequences>
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
                                <#list keys.horizontal as row, sequences>
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
                    <#if currentUser.id == puzzle.user.id && !puzzle.isEmpty() && puzzle.isGenerated() == false>
                        <#list 0..<puzzle.width as x>
                            <td class="table-cell p-0 <#if x !=0 && (x + 1) % 5 == 0>b-r</#if> <#if y !=0 && (y + 1) % 5 == 0>b-b</#if>">
                                <input type="checkbox" name="cell" class="cell"
                                        <#if puzzle.containsCell(x, y)>
                                            checked
                                        </#if>
                                       disabled
                                >
                            </td>
                        </#list>
                    <#else>
                        <#list 0..<puzzle.width as x>
                            <#assign cellStyle="cell"/>
                            <td class="table-cell p-0 <#if x !=0 && (x + 1) % 5 == 0>b-r</#if> <#if y !=0 && (y + 1) % 5 == 0>b-b</#if>">
                                <input type="checkbox" id="${x}:${y}" name="cell" value="${x}:${y}"
                                        <#if isPlaying>
                                            <#if game.containsCell(x, y) || game.containsHint(x, y)>
                                                checked
                                            </#if>
                                            <#if isSolved>
                                                disabled
                                            </#if>
                                            <#if game.containsHint(x, y)>
                                                disabled
                                                <#assign cellStyle="hint"/>
                                            </#if>
                                            <#if game.containsRemoval(x, y)>
                                                disabled
                                                <#assign cellStyle="removed"/>
                                            </#if>
                                        </#if>
                                       class=${cellStyle}
                                >
                            </td>
                        </#list>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>

<#macro preview puzzle>
    <div class="d-flex justify-content-center">
        <table class="table table-bordered border-secondary field">
            <tbody>
            <#list 0..<puzzle.height as y>
                <tr>
                    <#list 0..<puzzle.width as x>
                        <td class="table-cell-preview p-0">
                            <input type="checkbox" name="cell" class="preview"
                                    <#if puzzle.containsCell(x, y)>
                                        checked
                                    </#if>
                                   disabled
                            >
                        </td>
                    </#list>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>