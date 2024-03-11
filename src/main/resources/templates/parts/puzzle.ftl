<#include "security.ftl"/>

<#macro field puzzle keys="empty" game="empty">

    <#assign showKeys = !keys.equals("empty")/>
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
                    <#if currentUser.id == puzzle.user.id>
                        <#list 0..<puzzle.width as x>
                            <#assign cellStyle="cell"/>
                            <td class="table-cell p-0 <#if x !=0 && (x + 1) % 5 == 0>b-r</#if> <#if y !=0 && (y + 1) % 5 == 0>b-b</#if>">
                                <input type="checkbox" name="cell" class="cell"
                                        <#if puzzle.containsCell(y, x)>
                                            checked
                                        </#if>
                                >
                            </td>
                        </#list>
                    <#else>
                        <#list 0..<puzzle.width as x>
                            <#assign cellStyle="cell"/>
                            <td class="table-cell p-0 <#if x !=0 && (x + 1) % 5 == 0>b-r</#if> <#if y !=0 && (y + 1) % 5 == 0>b-b</#if>">
                                <input type="checkbox" id="${y}:${x}" name="cell" value="${y}:${x}"
                                        <#if isPlaying>
                                            <#if game.containsCell(y, x) || game.containsHint(y, x)>
                                                checked
                                            </#if>
                                            <#if isSolved>
                                                disabled
                                            </#if>
                                            <#if game.containsHint(y, x)>
                                                disabled
                                                <#assign cellStyle="hint"/>
                                            </#if>
                                            <#if game.containsRemoved(y, x)>
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