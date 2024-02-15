<#import "parts/common.ftl" as c/>

<@c.page "User list">
    <div class="w-75 m-auto">
        <div>
            <@c.form "user/list" "GET">
                <div class="row">
                    <div class="col-auto">
                        <@c.input "Nickname" "nickname"/>
                    </div>
                    <div class="col-auto">
                        <@c.submit "Search" "primary"/>
                    </div>
                </div>
            </@c.form>
        </div>
        <div class="w-30 mx-auto mb-4 d-flex justify-content-center">
            <@c.pager "user/list" users sizes/>
        </div>
        <table class="table table-bordered">
            <thead class="table-success">
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Nickname</th>
                <th scope="col">Email</th>
                <th scope="col">Role</th>
                <th scope="col">Status</th>
                <th scope="col"></th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <#list users.content as user>
                <tr>
                    <th scope="row">${user.id}</th>
                    <td>${user.nickname}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.isActivated()?string('Activated', 'Blocked')}</td>
                    <td>
                        <@c.form "user/change-role">
                            <@c.hidden "userId" user.id/>
                            <@c.hidden "page" users.getNumber()/>
                            <#if user.role == "ADMIN">
                                <@c.hidden "role" "PLAYER"/>
                                <@c.submit "Make player" "warning"/>
                            <#else>
                                <@c.hidden "role" "ADMIN"/>
                                <@c.submit "Make admin" "warning"/>
                            </#if>
                        </@c.form>
                    </td>
                    <td>
                        <@c.form "user/change-status">
                            <@c.hidden "userId" user.id/>
                            <@c.hidden "page" users.getNumber()/>
                            <#if user.isActivated()>
                                <@c.hidden "status" "false"/>
                                <@c.submit "   Block    " "danger"/>
                            <#else>
                                <@c.hidden "status" "true"/>
                                <@c.submit "Activate" "success"/>
                            </#if>
                        </@c.form>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
        <div class="w-30 mx-auto mt-4 d-flex justify-content-center">
            <@c.pager "user/list" users sizes/>
        </div>
    </div>
</@c.page>