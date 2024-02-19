<#import "parts/common.ftl" as c/>
<#import "parts/form.ftl" as f/>
<#import "parts/pager.ftl" as pg/>

<@c.page "User list">
    <div class="w-75 m-auto">
        <div>
            <@f.form "user/list" "GET">
                <div class="row">
                    <div class="col-auto">
                        <@f.input "Nickname" "nickname"/>
                    </div>
                    <div class="col-auto">
                        <@f.submit "Search" "primary"/>
                    </div>
                </div>
            </@f.form>
        </div>
        <div class="w-30 mx-auto mb-4 d-flex justify-content-center">
            <@pg.pager "user/list" users sizes/>
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
                        <@f.form "user/change-role">
                            <@f.hidden "userId" user.id/>
                            <@f.hidden "page" users.getNumber()/>
                            <#if user.role == "ADMIN">
                                <@f.hidden "role" "PLAYER"/>
                                <@f.submit "Make player" "warning"/>
                            <#else>
                                <@f.hidden "role" "ADMIN"/>
                                <@f.submit "Make admin" "warning"/>
                            </#if>
                        </@f.form>
                    </td>
                    <td>
                        <@f.form "user/change-status">
                            <@f.hidden "userId" user.id/>
                            <@f.hidden "page" users.getNumber()/>
                            <#if user.isActivated()>
                                <@f.hidden "status" "false"/>
                                <@f.submit "   Block    " "danger"/>
                            <#else>
                                <@f.hidden "status" "true"/>
                                <@f.submit "Activate" "success"/>
                            </#if>
                        </@f.form>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
        <div class="w-30 mx-auto mt-4 d-flex justify-content-center">
            <@pg.pager "user/list" users sizes/>
        </div>
    </div>
</@c.page>