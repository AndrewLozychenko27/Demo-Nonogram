<#import "parts/common.ftl" as c/>
<#include "parts/security.ftl"/>

<@c.page "Leaderboard">
    <div class="w-50 m-auto">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Player</th>
                <th scope="col">Score</th>
            </tr>
            </thead>
            <#assign number = 1/>
            <tbody>
            <#list leaders as player>
                <#if number == 1>
                    <#assign rankStyle = "bg-gold"/>
                <#elseif number == 2>
                    <#assign rankStyle = "bg-silver"/>
                <#elseif number == 3>
                    <#assign rankStyle = "bg-bronze"/>
                <#else>
                    <#assign rankStyle = ""/>
                </#if>

                <#if currentUser.email == player.email>
                    <#assign rankStyle = rankStyle + " me"/>
                </#if>

                <#if number gt 10>
                    <#if player.email == currentUser.email>
                        <tr>
                            <td colspan="4"><h3 class="text-center">...</h3></td>
                        </tr>
                    <#else>
                        <#assign number++/>
                        <#continue/>
                    </#if>
                </#if>

                <tr class="${rankStyle}">
                    <th scope="row">
                        <#if number gt 3>
                            ${number}
                        <#else>
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                 class="bi bi-award" viewBox="0 0 16 16">
                                <path d="M9.669.864 8 0 6.331.864l-1.858.282-.842 1.68-1.337 1.32L2.6 6l-.306 1.854 1.337 1.32.842 1.68 1.858.282L8 12l1.669-.864 1.858-.282.842-1.68 1.337-1.32L13.4 6l.306-1.854-1.337-1.32-.842-1.68zm1.196 1.193.684 1.365 1.086 1.072L12.387 6l.248 1.506-1.086 1.072-.684 1.365-1.51.229L8 10.874l-1.355-.702-1.51-.229-.684-1.365-1.086-1.072L3.614 6l-.25-1.506 1.087-1.072.684-1.365 1.51-.229L8 1.126l1.356.702z"/>
                                <path d="M4 11.794V16l4-1 4 1v-4.206l-2.018.306L8 13.126 6.018 12.1z"/>
                            </svg>
                        </#if>

                    </th>
                    <#assign number++/>
                    <td>${player.nickname} (${player.email})</td>
                    <td>${player.score}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>