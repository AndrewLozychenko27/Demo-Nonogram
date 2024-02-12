<#import "util.ftl" as u/>
<#include "security.ftl"/>

<#macro page title width="w-75">
    <html lang="en">
    <head>
        <title>${title}</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="../../static/style.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
              crossorigin="anonymous">
    </head>
    <body>
    <div id="header">
        <nav class="navbar navbar-expand-lg bg-bamboo-green-light">
            <div class="container-fluid w-75">
                <a class="navbar-brand" href="<@u.path ""/>">Nonogram</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                        </li>
                    </ul>
                    <#if currentUser??>
                        <a class="inline navbar-brand" href="<@u.path "user/" + currentUser.id + "/profile"/>">${currentUser.nickname}</a>
                        <@form "logout">
                            <@submit "Log out" "dark"/>
                        </@form>
                    <#else>
                        <@link "Sign in" "login" "dark"/>
                    </#if>
                </div>
            </div>
        </nav>
    </div>
    <div id="menu" class="fixed-top-on-scroll z-3">
        <nav class="navbar navbar-expand-lg bg-bamboo-green">
            <div class="container-fluid w-75">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <#if currentUser??>
                            <#if currentUser.role == "ADMIN">
                                <li class="nav-item">
                                    <a class="nav-link active" aria-current="page"
                                       href="<@u.path "user/list"/>">Users</a>
                                </li>
                            <#else>
                                <li class="nav-item">
                                    <a class="nav-link active" aria-current="page" href="#">My games</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="<@u.path "puzzle/list"/>">Puzzles</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link disabled" aria-disabled="true">Disabled</a>
                                </li>
                            </#if>
                        <#else>
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="#">How to play</a>
                            </li>
                        </#if>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
    <div id="content" class="${width} h-78 mx-auto my-4 p-4 z-0">
        <#nested>
        <div class="h-25"></div>
    </div>
    <div class="z-3 mt-4 pt-4" id="footer">
        <nav class="navbar navbar-expand-lg fixed-bottom bg-bamboo-green-light">
            <div class="container-fluid w-75">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link">About</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    </body>
    </html>
</#macro>

<#macro label label value>
    <div class="mb-3 row">
        <label class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <span class="form-control">${value}</span>
        </div>
    </div>
</#macro>

<#macro hidden name value>
    <input type="hidden" name="${name}" value="${value}">
</#macro>

<#macro bind path>
    <#assign
    status = springMacroRequestContext.getBindStatus(path)>
</#macro>

<#macro input label name=label?c_lower_case>
    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" name="${name}" value="<#nested>">
        </div>
    </div>
</#macro>

<#macro inputValid label name>
    <#local nestedContent><#nested></#local>

    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="text" name="${name}" value="${nestedContent}"
                   class="form-control <#if nestedContent?has_content || status.isError()>${status.isError()?then('is-invalid', 'is-valid')}</#if>">
            <#if status.isError()>
                <div class="invalid-feedback">
                    <#list status.errorMessages as error>
                        ${error}
                    </#list>
                </div>
            <#else>
                <div class="invalid-feedback">
                    Correct
                </div>
            </#if>
        </div>
    </div>
</#macro>

<#macro password label="Password" name="password">
    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="password" class="form-control" name="${name}">
        </div>
    </div>
</#macro>

<#macro passwordValid label="Password" name="password">
    <#local nestedContent><#nested></#local>

    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="password" name="${name}" value="${nestedContent}"
                   class="form-control <#if nestedContent?has_content || status.isError()>${status.isError()?then('is-invalid', 'is-valid')}</#if>">
            <#if status.isError()>
                <div class="invalid-feedback">
                    <#list status.errorMessages as error>
                        ${error}
                    </#list>
                </div>
            <#else>
                <div class="invalid-feedback">
                    Correct
                </div>
            </#if>
        </div>
    </div>
</#macro>

<#macro submit label style="success">
    <input type="submit" class="btn btn-outline-${style}" value="${label}">
</#macro>

<#macro submitPair label cancel="" cancelLabel="Cancel">
    <div class="mb-3 mt-4 row">
        <div class="col-sm-6">
            <@link cancelLabel cancel "btn btn-outline-danger"/>
        </div>
        <div class="col-sm-6 d-flex flex-row-reverse">
            <button type="submit" class="btn btn-outline-success">${label}</button>
        </div>
    </div>
</#macro>

<#macro formBody label>
    <div class="m-3 p-3 border border-success-subtle rounded-3 px-4">
        <div class="row">
            <h3 class="text-center my-3">${label}</h3>
        </div>
        <div class="my-3">
            <#nested/>
        </div>
    </div>
</#macro>

<#macro form action method="POST">
    <form action="<@u.path action/>" method="${method}" class="m-0">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#nested/>
    </form>
</#macro>

<#macro link label path style="secondary">
    <a class="btn btn-outline-${style}" role="button" href="<@u.path path />">${label}</a>
</#macro>

<#macro pager url page>
    <nav>
        <ul class="pagination">
            <#if page.getNumber() gte 3>
                <li class="page-item"><a class="page-link" href="<@u.path "user/list?page=0"/>">1</a></li>
                <#if page.getNumber() gt 3>
                    <li class="page-item disabled"><a class="page-link">...</a></li>
                </#if>
            </#if>
            <#if page.getNumber() - 1 gte 1>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path "user/list?page=" + (page.getNumber() - 2)/>">${page.getNumber() - 1}</a>
                </li>
            </#if>
            <#if page.getNumber() gte 1>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path "user/list?page=" + (page.getNumber() - 1)/>">${page.getNumber()}</a>
                </li>
            </#if>
            <li class="page-item active"><a class="page-link">${page.getNumber() + 1}</a></li>
            <#if page.getNumber() + 1 lt page.getTotalPages()>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path "user/list?page=" + (page.getNumber() + 1)/>">${page.getNumber() + 2}</a>
                </li>
            </#if>
            <#if page.getNumber() + 2 lt page.getTotalPages()>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path "user/list?page=" + (page.getNumber() + 2)/>">${page.getNumber() + 3}</a>
                </li>
            </#if>
            <#if page.getNumber() lt page.getTotalPages() - 3>
                <#if page.getNumber() lt page.getTotalPages() - 4>
                    <li class="page-item disabled"><a class="page-link">...</a></li>
                </#if>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path "user/list?page=" + (page.getTotalPages() - 1)/>">${page.getTotalPages()}</a>
                </li>
            </#if>
        </ul>
    </nav>
</#macro>

<#macro field puzzle hints="empty" suggestions=[]>
    <#if hints == "empty">
        <#assign  showHints=false/>
    <#else>
        <#assign  showHints=true/>
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
                        class="table-active p-0 <#if !showHints>table-cell</#if> <#if x !=0 && (x + 1) % 5 == 0>b-r</#if> b-b">
                        <#if showHints>
                            <#list hints.vertical as col, sequences>
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
                        class="table-active p-0 <#if !showHints>table-cell</#if> <#if y !=0 && (y + 1) % 5 == 0>b-b</#if> b-r align-middle">
                        <p class="text-end mb-0 mx-2">
                            <#if showHints>
                                <#list hints.horizontal as row, sequences>
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
                        <td class="table-cell p-0 <#if x !=0 && (x + 1) % 5 == 0>b-r</#if> <#if y !=0 && (y + 1) % 5 == 0>b-b</#if>">
                            <input type="checkbox" name="cell" class="cell" value="${y}:${x}"
                                   <#if isPlaying?? && suggestions?seq_contains(y + ":" + x)>checked</#if>>
                        </td>
                    </#list>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>